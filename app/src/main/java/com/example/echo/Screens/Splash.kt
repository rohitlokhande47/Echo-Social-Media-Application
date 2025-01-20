package com.example.echo.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.example.echo.Navigation.Routes
import com.example.echo.R
import com.example.echo.utils.ThreadsIcon
import com.example.echo.utils.ThreadsIconSplash
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun Splash(navController: NavHostController) {
    ConstraintLayout(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        val (image) = createRefs()

        ThreadsIconSplash(
            modifier = Modifier.size(200.dp).constrainAs(image) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            color = Color.White
        )
    }

    LaunchedEffect(true) {
        delay(3000)

        if (FirebaseAuth.getInstance().currentUser != null) {
            navController.navigate(Routes.BottomNav.routes) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            navController.navigate(Routes.login.routes) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
