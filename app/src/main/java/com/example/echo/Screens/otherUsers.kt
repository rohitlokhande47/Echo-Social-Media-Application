package com.example.echo.Screens

import AuthViewModel
import UserViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.echo.components.FullScreenImageViewer
import com.example.echo.item_View.ThreadItem
import com.example.echo.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun otherUsers(
    navController: NavController,
    userId: String,
) {
    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current
    val user by userViewModel.users.observeAsState(null)
    val thread by userViewModel.threads.observeAsState(null)
    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)
    var showFullScreenImage by remember { mutableStateOf(false) }

    userViewModel.fetchUser(userId)
    userViewModel.fetchThreads(userId)
    userViewModel.getFollwers(userId)
    userViewModel.getFollowing(userId)

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF000000))
        ) {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            user?.let { currentUser ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = currentUser.name ?: "",
                                        style = TextStyle(
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                    Text(
                                        text = currentUser.userName,
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            color = Color.Gray
                                        )
                                    )
                                }

                                AsyncImage(
                                    model = currentUser.imageUrl,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .clickable { showFullScreenImage = true },
                                    contentScale = ContentScale.Crop
                                )
                            }

                            if (!currentUser.bio.isNullOrEmpty()) {
                                Text(
                                    text = currentUser.bio,
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${followerList?.size ?: 0} followers",
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(24.dp))
                                Text(
                                    text = "${followingList?.size ?: 0} following",
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                )
                            }

                            Button(
                                onClick = {
                                    if (currentUserId.isNotEmpty()) {
                                        if (followerList?.contains(currentUserId) == true) {
                                            userViewModel.unfollowUsers(userId, currentUserId)
                                        } else {
                                            userViewModel.followUsers(userId, currentUserId)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (followerList?.contains(currentUserId) == true)
                                        Color.DarkGray else Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (followerList?.contains(currentUserId) == true)
                                        "Following" else "Follow",
                                    color = if (followerList?.contains(currentUserId) == true)
                                        Color.White else Color.Black
                                )
                            }

                            Divider(
                                color = Color(0xFF1C1C1C),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }

                    if (thread != null) {
                        items(thread ?: emptyList()) { pair ->
                            ThreadItem(
                                thread = pair,
                                users = currentUser,
                                navController = navController,
                                userId = SharedPref.getUserName(context)
                            )
                        }
                    }
                }
            }
        }

        // Full screen image viewer
        if (showFullScreenImage) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(2f)
                    .background(Color.Black)
            ) {
                FullScreenImageViewer(
                    imageUrl = user?.imageUrl ?: "",
                    onDismiss = { showFullScreenImage = false }
                )
            }
        }
    }
}