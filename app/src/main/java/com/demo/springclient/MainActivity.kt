package com.demo.springclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.demo.springclient.ui.LoginScreen
import com.demo.springclient.ui.OrdersScreen
import com.demo.springclient.ui.UsersScreen
import com.demo.springclient.ui.ChatScreen
import com.demo.springclient.ui.HomeScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //AppNavigation()
            MainScreen()
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Default.Lock)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Users : Screen("users", "Users", Icons.Default.People)
    object Orders : Screen("orders", "Orders", Icons.Default.ShoppingCart)
    object AI : Screen("ai", "AI Chat", Icons.Default.SmartToy)

}

val bottomTabs = listOf(Screen.Home, Screen.Users, Screen.Orders, Screen.AI)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    /* Dynamic monitor the current route stack status, used to highlight the bottom buttons */
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomTabs.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    // Core: for complex business navigation, avoid returning to the start destination
                                    // 1. pop up to the start destination of the graph to avoid building up a large stack of destinations, avoid the back button clicking multiple times
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        // save state (such as scroll position and input text)
                                        saveState = true
                                    }
                                    // 2. avoid multiple copies of the same destination when reselecting the same item(duplicate create multiple instances of the same destination)
                                    launchSingleTop = true
                                    // 3. restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Orders.route) { OrdersScreen(navController) }
            composable(Screen.Users.route) { UsersScreen(navController) }
            composable(Screen.AI.route) { ChatScreen(navController) }

            // Handling future complexities: You can directly expand second- and third-level subpages here
            // for example： from chat page to chat detail page
            composable("chat_detail/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                //ChatDetailScreen(userId = userId, navController = navController)
            }

            // for example： from order list page to order detail page
            composable("order_detail/{orderId}") { backStackEntry ->
                val orderId = backStackEntry.arguments?.getString("orderId")
                //OrderDetailScreen(orderId = orderId, navController = navController)
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Users.route) { UsersScreen(navController) }
        composable(Screen.Orders.route) { OrdersScreen(navController) }
        composable(Screen.AI.route) { ChatScreen(navController) }
    }
}