package com.example.echo.Screens

import AuthViewModel
import UserViewModel
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.echo.Navigation.Routes
import com.example.echo.item_View.ThreadItem
import com.example.echo.models.UserModel
import com.example.echo.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val context = LocalContext.current

    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val threads by userViewModel.threads.observeAsState(null)

    val users =
        UserModel(
            name = SharedPref.getName(context),
            userName = SharedPref.getUserName(context),
            imageUrl = SharedPref.getImage(context),
        )

    if (currentUserId != "") {
        userViewModel.getFollwers(currentUserId)
        userViewModel.getFollowing(currentUserId)
    }

    if (firebaseUser != null) {
        userViewModel.fetchThreads(firebaseUser!!.uid)
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.login.routes) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF000000),
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
        ) {
            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                ) {
                    // Top Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Private account",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = SharedPref.getUserName(context),
                                style =
                                    MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                    ),
                            )
                        }

                        IconButton(onClick = { /* Add menu functionality */ }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                            )
                        }
                    }

                    // Profile Section
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = SharedPref.getName(context),
                                style =
                                    MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                    ),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = SharedPref.getBio(context),
                                style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray,
                                    ),
                            )
                        }

                        AsyncImage(
                            model = SharedPref.getImage(context),
                            contentDescription = "Profile picture",
                            modifier =
                                Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${followerList?.size ?: 0}",
                                style =
                                    MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                    ),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "followers",
                                style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray,
                                    ),
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${followingList?.size ?: 0}",
                                style =
                                    MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                    ),
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "following",
                                style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Gray,
                                    ),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(
                            onClick = { /* Edit profile */ },
                            modifier = Modifier.weight(1f),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White,
                                ),
                            border = BorderStroke(1.dp, Color.Gray),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text("Edit profile")
                        }

                        Button(
                            onClick = { authViewModel.Logout() },
                            modifier = Modifier.weight(1f),
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = Color.White,
                                ),
                            border = BorderStroke(1.dp, Color.Gray),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text("Sign out")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tabs
                    TabRow(
                        selectedTabIndex = 0,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[0]),
                                height = 1.dp,
                                color = Color.White,
                            )
                        },
                    ) {
                        Tab(
                            selected = true,
                            onClick = { /* Handle tab click */ },
                            text = { Text("Threads") },
                        )
                        Tab(
                            selected = false,
                            onClick = { /* Handle tab click */ },
                            text = { Text("Replies") },
                        )
                    }
                }
            }

            // Threads List
            items(threads ?: emptyList()) { pair ->
                ThreadItem(
                    thread = pair,
                    users = users,
                    navController = navController,
                    userId = SharedPref.getUserName(context),
                )
            }
        }
    }
}
