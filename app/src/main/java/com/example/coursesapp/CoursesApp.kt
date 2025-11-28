package com.example.coursesapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.common.Constants.ROUTE_ACCOUNT
import com.example.core.common.Constants.ROUTE_FAVORITES
import com.example.core.common.Constants.ROUTE_HOME
import com.example.core.common.Constants.ROUTE_LOGIN
import com.example.features.account.AccountScreen
import com.example.features.auth.LoginScreen
import com.example.features.favorites.FavoritesScreen
import com.example.features.home.HomeScreen

@Composable
fun CoursesApp() {
    val navController = rememberNavController()

    // Стартовый экран - вход
    NavHost(
        navController = navController,
        startDestination = ROUTE_LOGIN,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(ROUTE_LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(ROUTE_HOME) {
            // Главный экран с Bottom Navigation
            MainScreen(navController = navController)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: androidx.navigation.NavController) {
    val navControllerInternal = rememberNavController()
    val currentBackStack by navControllerInternal.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    NavigationItem(ROUTE_HOME, "Главная", Icons.Default.Home),
                    NavigationItem(ROUTE_FAVORITES, "Избранное", Icons.Default.Favorite),
                    NavigationItem(ROUTE_ACCOUNT, "Аккаунт", Icons.Default.AccountCircle)
                )

                items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = { Text(item.title) },
                        selected = currentDestination?.route == item.route,
                        onClick = {
                            navControllerInternal.navigate(item.route) {
                                popUpTo(navControllerInternal.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navControllerInternal,
            startDestination = ROUTE_HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ROUTE_HOME) {
                HomeScreen()
            }
            composable(ROUTE_FAVORITES) {
                FavoritesScreen()
            }
            composable(ROUTE_ACCOUNT) {
                AccountScreen()
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)