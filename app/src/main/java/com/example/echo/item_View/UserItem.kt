package com.example.echo.item_View

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.echo.Navigation.Routes
import com.example.echo.models.UserModel

import androidx.compose.ui.Alignment

import androidx.compose.ui.text.font.FontWeight


@Composable
fun UserItem(
    users: UserModel,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val routes = Routes.otherUser.routes.replace("{data}", users.userId)
                navController.navigate(routes)
                Log.d("user.userId", routes)
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Image
        AsyncImage(
            model = users.imageUrl,
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
        )

        // User Info
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)
        ) {
            // Username
            Text(
                text = users.userName,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Display Name
            Text(
                text = users.name,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            )
        }

        // Optional: Add followers count or verification badge
        // You can add more Threads-specific UI elements here
    }
}