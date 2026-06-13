package com.demo.springclient.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.demo.springclient.api.ApiClient
import com.demo.springclient.api.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

data class Message(val role: String, val content: String)

@Composable
fun ChatScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var input by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val sessionId = remember { "android_session_${System.currentTimeMillis()}" }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("AI Assistant", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0x88FF5722))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { navController.navigate("users") }) {
                    Text("Users")
                }
                Button(onClick = { navController.navigate("orders") }) {
                    Text("Orders")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center) {
                        Text("Ask me about your orders or account...",
                            color = Color.Gray)
                    }
                }
            }
            items(messages) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.role == "user")
                        Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (msg.role == "user")
                                Color(0xFF1890FF) else Color(0xFFF0F0F0)
                        ),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(
                            text = msg.content,
                            modifier = Modifier.padding(12.dp),
                            color = if (msg.role == "user") Color.White else Color.Black
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                enabled = !loading,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (input.isBlank()) return@Button
                    val userMessage = input
                    input = ""
                    messages = messages + Message("user", userMessage)
                    loading = true

                    scope.launch {
                        try {
                            val token = "Bearer ${TokenManager.getToken(context)}"
                            val responseBody = withContext(Dispatchers.IO) {
                                ApiClient.service.chatStream(token, userMessage, sessionId)
                            }

                            var aiText = ""
                            messages = messages + Message("ai", "")

                            withContext(Dispatchers.IO) {
                                val reader = BufferedReader(
                                    InputStreamReader(responseBody.byteStream()))
                                var line: String?
                                var buffer = ""

                                while (reader.readLine().also { line = it } != null) {
                                    buffer += line + "\n"
                                    val lines = buffer.split("\n")
                                    buffer = lines.last()

                                    for (l in lines.dropLast(1)) {
                                        val trimmed = l.trim()
                                        if (trimmed.startsWith("data:")) {
                                            val text = trimmed.substring(5).trimStart()
                                            if (text.isNotEmpty()) {
                                                aiText += text
                                                val finalText = aiText
                                                withContext(Dispatchers.Main) {
                                                    messages = messages.dropLast(1) +
                                                            Message("ai", finalText)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            messages = messages + Message("ai", "Error: ${e.message}")
                        } finally {
                            loading = false
                        }
                    }
                },
                enabled = !loading
            ) {
                Text(if (loading) "..." else "Send")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen(rememberNavController())
}