package com.demo.springclient.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.demo.springclient.Screen
import com.demo.springclient.api.ApiClient
import com.demo.springclient.api.TokenManager
import com.demo.springclient.model.User
import kotlinx.coroutines.launch

@Composable
fun UsersScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            val rawToken = TokenManager.getToken(context)
            if (rawToken == null) {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
                return@launch
            }
            try {
                val token = "Bearer ${TokenManager.getToken(context)}"
                users = ApiClient.service.getUsers(token)
            } catch (e: Exception) {
                Log.e("UsersScreen", "Failed to load users: ${e.message}")
                TokenManager.clearToken(context) // remove the bad token
                navController.navigate(Screen.Login.route) {
                    popUpTo(0)
                }
            }finally {
                loading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("User List", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(users) { user ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${user.id}", fontWeight = FontWeight.Bold)
                            Text("Name: ${user.name}")
                            Text("Email: ${user.email}")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    UsersScreen(rememberNavController())
}