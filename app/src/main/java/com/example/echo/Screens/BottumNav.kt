package com.example.echo.Screens

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.echo.Navigation.Routes
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.background

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNav(navController: NavHostController) {
    val navController1 = rememberNavController()

    Scaffold(
        bottomBar = { MyBottomBar(navController1) },
        containerColor = Color.Black
    ) { innerPadding ->
        NavHost(
            navController = navController1,
            startDestination = Routes.Home.routes,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Home.routes) {
                Home(navController)
            }
            composable(Routes.Search.routes) {
                Search(navController)
            }
            composable(Routes.Notification.routes) {
                Notification(navController)
            }
            composable(Routes.AddThreads.routes) {
                AddThreads(navController1)
            }
            composable(Routes.Profile.routes) {
                Profile(navController)
            }
        }
    }
}

@Composable
fun MyBottomBar(navController1: NavHostController) {
    val backstackEntry by navController1.currentBackStackEntryAsState()
    val currentRoute = backstackEntry?.destination?.route

    val navigationItems = listOf(
        BottomNavItem(
            "Home",
            Routes.Home.routes,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            "Search",
            Routes.Search.routes,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search
        ),
        BottomNavItem(
            "Write",
            Routes.AddThreads.routes,
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit
        ),
        BottomNavItem(
            "Activity",
            Routes.Notification.routes,
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.FavoriteBorder
        ),
        BottomNavItem(
            "Profile",
            Routes.Profile.routes,
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    Surface(
        color = Color.Black,
        tonalElevation = 0.dp,
        modifier = Modifier.background(Color.Black)
    ) {
        NavigationBar(
            containerColor = Color.Black,
            contentColor = Color.White,
            tonalElevation = 0.dp,
            modifier = Modifier
                .height(100.dp)  // Increased height
                .fillMaxWidth()
                .background(Color.Black)


        ) {
            navigationItems.forEach { item ->
                val selected = item.route == currentRoute

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController1.navigate(item.route) {
                            popUpTo(navController1.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.title,
                            modifier = Modifier.size(32.dp),  // Increased icon size
                            tint = if (selected) Color.White else Color.White.copy(alpha = 0.5f)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        indicatorColor = Color.Black
                    )
                )
            }
        }
    }
}