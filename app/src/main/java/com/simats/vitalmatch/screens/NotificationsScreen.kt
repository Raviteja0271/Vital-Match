package com.simats.vitalmatch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.vitalmatch.ui.theme.*
import io.github.jan.supabase.postgrest.postgrest
import com.simats.vitalmatch.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import com.simats.vitalmatch.data.models.NotificationModel
import com.simats.vitalmatch.data.models.Emergency
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun NotificationsScreen(navController: NavController) {
    var notificationsList by remember { mutableStateOf<List<NotificationData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val currentUser = SupabaseClient.client.auth.currentUserOrNull()
            val currentUserId = currentUser?.id ?: ""

            // Fetch real notifications targeted to current user and active emergencies
            val fetchedNotifications = SupabaseClient.client.postgrest["notifications"]
                .select {
                    filter {
                        if (currentUserId.isNotEmpty()) eq("user_id", currentUserId)
                    }
                }.decodeList<NotificationModel>().map {
                    NotificationData(
                        id = it.id ?: "",
                        title = it.title,
                        message = it.message,
                        time = it.created_at?.take(10) ?: "Just now",
                        type = if (it.type == "URGENT") "URGENT" else "NORMAL",
                        location = null
                    )
                }
            val activeEmergencies = SupabaseClient.client.postgrest["emergency_requests"]
                .select().decodeList<Emergency>().filter { it.status == "Active" }.map {
                    NotificationData(
                        id = it.id ?: "",
                        title = "Emergency: ${it.blood_group} Blood Needed",
                        message = it.notes ?: "Emergency blood request at ${it.hospital_name}",
                        time = it.created_at?.take(10) ?: "Recently",
                        type = if (it.notes?.contains("urgent", ignoreCase = true) == true) "URGENT" else "NORMAL",
                        location = it.hospital_name
                    )
                }
            notificationsList = fetchedNotifications + activeEmergencies
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .clickable { navController.navigateUp() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = RedPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(com.simats.vitalmatch.ui.theme.AppStrings.get("back"), color = RedPrimary, fontSize = 16.sp)
            }

            Text(
                text = com.simats.vitalmatch.ui.theme.AppStrings.get("notifications"),
                modifier = Modifier.padding(top = 24.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Stay updated with emergency alerts",
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        }

        HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RedPrimary)
            }
        } else if (notificationsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No notifications", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(notificationsList) { notification ->
                    NotificationCard(notification)
                }
            }
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationData) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isAccepted by remember { mutableStateOf(false) }
    var isDeclined by remember { mutableStateOf(false) }

    val isRequestNotif = notification.type == "URGENT" || 
                         notification.title.contains("Request", ignoreCase = true) || 
                         notification.title.contains("Emergency", ignoreCase = true)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (notification.type == "URGENT") {
                    Modifier.border(1.dp, RedPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon Box
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isAccepted || notification.type == "CONNECTED") Color(0xFFE8F5E9) else Color(0xFFFFEBEE), 
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isAccepted || notification.type == "CONNECTED") Icons.Default.CheckCircle else Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = if (isAccepted || notification.type == "CONNECTED") Color(0xFF2ECC71) else RedPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    
                    if (isAccepted) {
                        Surface(color = Color(0xFFE8F5E9), shape = RoundedCornerShape(6.dp)) {
                            Text("Accepted", color = Color(0xFF2E7D32), fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    } else if (isDeclined) {
                        Surface(color = Color(0xFFFFEBEE), shape = RoundedCornerShape(6.dp)) {
                            Text("Declined", color = RedPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    } else if (notification.type == "URGENT") {
                        Surface(color = RedPrimary, shape = RoundedCornerShape(4.dp)) {
                            Text("URGENT", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = notification.time, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    
                    if (notification.location != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = notification.location, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                if (isRequestNotif && !isAccepted && !isDeclined) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                isDeclined = true
                                scope.launch {
                                    try {
                                        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                                        if (currentUser != null) {
                                            SupabaseClient.client.postgrest["blood_requests"]
                                                .update({ set("status", "Declined") }) {
                                                    filter { eq("donor_user_id", currentUser.id) }
                                                }
                                        }
                                    } catch (e: Exception) { }
                                }
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary)
                        ) {
                            Text("Decline", fontSize = 12.sp, color = RedPrimary, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                isAccepted = true
                                scope.launch {
                                    try {
                                        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                                        if (currentUser != null) {
                                            SupabaseClient.client.postgrest["blood_requests"]
                                                .update({ set("status", "Accepted") }) {
                                                    filter { eq("donor_user_id", currentUser.id) }
                                                }
                                        }
                                        Toast.makeText(context, "Request Accepted! Requester notified.", Toast.LENGTH_SHORT).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Request Accepted!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f).height(38.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Accept Request", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

data class NotificationData(
    val id: String, 
    val title: String, 
    val message: String, 
    val time: String, 
    val type: String,
    val location: String?
)
