package com.example.echo.Screens

import SearchViewModel
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.echo.item_View.UserItem
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navController: NavController) {
    val searchViewModel: SearchViewModel = viewModel()
    val userlist by searchViewModel.usersList.observeAsState(null)
    var search by remember { mutableStateOf("") }

    // Get current user ID
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp)
    ) {
        // Search Header
        Text(
            text = "Search",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Search TextField
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            color = Color(0xFF1C1C1C)
        ) {
            TextField(
                value = search,
                onValueChange = { search = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                placeholder = {
                    Text(
                        "Search",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Results
        if(search.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (userlist != null && userlist!!.isNotEmpty()) {
                    val filterItem = userlist!!.filter { user ->
                        user.name!!.contains(search, ignoreCase = true) &&
                                user.userId != currentUserId // Filter out current user
                    }
                    items(filterItem) { user ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            color = Color(0xFF1C1C1C)
                        ) {
                            UserItem(user, navController)
                        }
                    }
                }
            }
        }
    }
}