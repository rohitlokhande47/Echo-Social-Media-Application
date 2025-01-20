package com.example.echo.Screens

import HomeViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.echo.item_View.ThreadItem
import com.example.echo.utils.ThreadsIcon
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider

import androidx.compose.ui.unit.dp

import androidx.compose.material3.LinearProgressIndicator

@Composable
fun Home(navController: NavController) {
    val homeViewModel: HomeViewModel = viewModel()
    val threadAndUsers by homeViewModel.threadsandUsers.observeAsState(null)
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff19191a))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Loading indicator
            if (threadAndUsers == null) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = Color.White,
                    trackColor = Color(0xFF353535)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Logo item
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ThreadsIcon(
                            modifier = Modifier.size(48.dp),
                            color = Color.White
                        )
                    }
                }

                // Thread items
                items(threadAndUsers ?: emptyList()) { pairs ->
                    ThreadItem(
                        thread = pairs.first,
                        users = pairs.second,
                        navController,
                        FirebaseAuth.getInstance().currentUser!!.uid,
                    )
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}