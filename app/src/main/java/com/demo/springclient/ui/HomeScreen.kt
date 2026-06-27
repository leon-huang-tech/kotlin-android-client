package com.demo.springclient.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.demo.springclient.Screen
import com.demo.springclient.api.ApiClient
import com.demo.springclient.api.TokenManager
import com.demo.springclient.model.LoginRequest
import com.demo.springclient.model.Order
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val token = "Bearer ${TokenManager.getToken(context)}"
            } finally {
                loading = false
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Home", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0x88FF5722))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { navController.navigate(Screen.Users.route) }) {
                Text(Screen.Users.title)
            }
            Button(onClick = { navController.navigate(Screen.Orders.route) }) {
                Text(Screen.Orders.title)
            }
            Button(onClick = { navController.navigate(Screen.AI.route) }) {
                Text(Screen.AI.title)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(
) {
    HomeScreen(rememberNavController())
}