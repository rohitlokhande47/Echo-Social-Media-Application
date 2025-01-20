package com.example.echo.Navigation

sealed class Routes(
    val routes: String,
) {
    object Home : Routes("Home")

    object Splash : Routes("Splash")

    object AddThreads : Routes("AddThreads")

    object Profile : Routes("Profile")

    object Search : Routes("Search")

    object Notification : Routes("Notification")

    object BottomNav : Routes("BottomNav")

    object Register : Routes("Register")

    object login : Routes("login")

  object otherUser: Routes("otherUsers/{data}")


}
