package com.simats.vitalmatch.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.vitalmatch.Screen
import com.simats.vitalmatch.ui.theme.RedPrimary
import kotlinx.coroutines.delay
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionScreenContent(
    navController: NavController,
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String,
    targetRoute: String,
    onButtonClick: (() -> Unit)? = null
) {
    var showSplash by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = RedPrimary,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = description,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                    if (onButtonClick != null) {
                        onButtonClick()
                    } else {
                        navController.navigate(targetRoute)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedPrimary
                )
            ) {
                Text(
                    buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(onClick = { showSplash = true }) {
                Text("Maybe Later", color = Color.Gray, fontSize = 16.sp)
            }
        }

        // Top Right Splash Message
        AnimatedVisibility(
            visible = showSplash,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 24.dp)
        ) {
            Surface(
                color = RedPrimary,
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Need access to login",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            LaunchedEffect(showSplash) {
                if (showSplash) {
                    delay(3000)
                    showSplash = false
                }
            }
        }
    }
}

@Composable
fun LocationPermissionScreen(navController: NavController) {
    val context = LocalContext.current

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                navController.navigate(
                    Screen.NotificationPermission.route
                )
            } else {
                Toast.makeText(
                    context,
                    "Location access is required to continue",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    PermissionScreenContent(
        navController = navController,
        icon = Icons.Default.LocationOn,
        title = "Enable Location",
        description =
            "We need your location to find nearby blood donors and emergency requests.",
        buttonText = "Allow Location",
        targetRoute = Screen.NotificationPermission.route,
        onButtonClick = {
            val isGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                navController.navigate(
                    Screen.NotificationPermission.route
                )
            } else {
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        }
    )
}

@Composable
fun NotificationPermissionScreen(navController: NavController) {
    val context = LocalContext.current

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                navController.navigate(Screen.Home.route)
            } else {
                Toast.makeText(
                    context,
                    "Notification access is required to continue",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    PermissionScreenContent(
        navController = navController,
        icon = Icons.Default.Notifications,
        title = "Enable Notifications",
        description =
            "Get notified about urgent blood requests and updates in your area.",
        buttonText = "Allow Notifications",
        targetRoute = Screen.Home.route,
        onButtonClick = {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                navController.navigate(Screen.Home.route)
                return@PermissionScreenContent
            }

            val isGranted =
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                navController.navigate(Screen.Home.route)
            } else {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    )
}
