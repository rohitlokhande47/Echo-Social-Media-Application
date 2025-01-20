package com.example.echo.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.echo.Screens.AddThreads
import com.example.echo.Screens.BottomNav
import com.example.echo.Screens.Home
import com.example.echo.Screens.Notification
import com.example.echo.Screens.Profile
import com.example.echo.Screens.Register
import com.example.echo.Screens.Search
import com.example.echo.Screens.Splash
import com.example.echo.Screens.login
import com.example.echo.Screens.otherUsers

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.Splash.routes) {
        composable(Routes.Splash.routes) {
            Splash(navController)
        }
        composable(Routes.Home.routes) {
            Home(navController)
        }
        composable(Routes.AddThreads.routes) {
            AddThreads(navController)
        }
        composable(Routes.Search.routes) {
            Search(navController)
        }
        composable(Routes.Notification.routes) {
            Notification(navController)
        }
        composable(Routes.Profile.routes) {
            Profile(navController)
        }
        composable(Routes.BottomNav.routes) {
            BottomNav(navController)
        }
        composable(Routes.Register.routes) {
            Register(navController)
        }
        composable(Routes.login.routes) {
            login(navController)
        }
        composable(Routes.otherUser.routes){
            val data = it.arguments!!.getString("data")
            otherUsers(navController,data!!)
        }
        }
    }

