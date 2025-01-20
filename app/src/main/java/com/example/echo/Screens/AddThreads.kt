package com.example.echo.Screens

import AddThreadViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.echo.Navigation.Routes
import com.example.echo.R
import com.example.echo.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale


@Composable
fun AddThreads(navController: NavController) {
    var thread by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val threadViewModel: AddThreadViewModel = viewModel()
    val isPosted by threadViewModel.isPosted.observeAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isPosted) {
        if (isPosted == true) {
            thread = ""
            imageUri = null
            Toast.makeText(context, "Thread posted", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.Home.routes) {
                popUpTo(Routes.AddThreads.routes) { inclusive = true }
            }
        }
    }

    // Permission handlers
    val permissionRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) launcher.launch("image/*")
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF000000)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(Routes.Home.routes) {
                            popUpTo(Routes.AddThreads.routes) { inclusive = true }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "New Echo",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                TextButton(
                    onClick = {
                        if (thread.isNotEmpty()) {
                            if (imageUri == null) {
                                threadViewModel.saveThreadData(
                                    thread,
                                    "",
                                    FirebaseAuth.getInstance().currentUser!!.uid
                                )
                            } else {
                                threadViewModel.saveImageToCloudinary(
                                    thread,
                                    FirebaseAuth.getInstance().currentUser!!.uid,
                                    imageUri!!
                                )
                            }
                        }
                    },
                    enabled = thread.isNotEmpty()
                ) {
                    Text(
                        text = "Post",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (thread.isNotEmpty()) Color(0xFF0D99FF) else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            // Content Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Profile Picture Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = SharedPref.getImage(context),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(Color.Gray)
                    )
                }

                // Thread Content Column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = SharedPref.getUserName(context),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    BasicTextField(
                        value = thread,
                        onValueChange = { thread = it },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .focusRequester(focusRequester),
                        decorationBox = { innerTextField ->
                            Box {
                                if (thread.isEmpty()) {
                                    Text(
                                        text = "Start an echo...",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = Color.Gray
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    if (imageUri != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(200.dp)
                            ) {
                                Box {
                                    AsyncImage(
                                        model = imageUri,
                                        contentDescription = "Attached image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    IconButton(
                                        onClick = { imageUri = null },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove image",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Section
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Anyone can reply",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        )
                    )

                    IconButton(
                        onClick = {
                            val isGranted = ContextCompat.checkSelfPermission(
                                context,
                                permissionRequest
                            ) == PackageManager.PERMISSION_GRANTED

                            if (isGranted) {
                                launcher.launch("image/*")
                            } else {
                                permissionLauncher.launch(permissionRequest)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Attach media",
                            tint = Color(0xFF0D99FF)
                        )
                    }
                }
            }
            }
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }