package com.simats.vitalmatch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.auth.auth
import com.simats.vitalmatch.data.remote.SupabaseClient
import com.simats.vitalmatch.data.models.Emergency
import com.simats.vitalmatch.data.models.BloodRequest
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import kotlinx.coroutines.launch
import com.simats.vitalmatch.Screen
import com.simats.vitalmatch.ui.theme.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import java.util.Locale

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Chatbot.route) },
                containerColor = RedPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.SmartToy, contentDescription = "NLP Chatbot")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            HomeHeader(navController)
            
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = (-40).dp)
            ) {
                ActionCards(navController)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = com.simats.vitalmatch.ui.theme.AppStrings.get("live_emergencies"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = com.simats.vitalmatch.ui.theme.AppStrings.get("view_all"),
                        fontSize = 14.sp,
                        color = RedPrimary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { navController.navigate(Screen.EmergencyFeed.route) }
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                EmergencyList(navController)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HomeHeader(navController: NavController) {
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("Fetching location...") }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchLocation(context) { locationText = it }
        } else {
            locationText = "Location Denied"
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fetchLocation(context) { locationText = it }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = RedPrimary,
                shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
            )
            .padding(top = 48.dp, bottom = 80.dp)
            .padding(horizontal = 24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Current Location",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = locationText,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { navController.navigate(Screen.Chatbot.route) }) {
                        Icon(
                            Icons.Default.SmartToy,
                            contentDescription = "AI Chatbot Assistant",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Box {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp).clickable { navController.navigate(Screen.Notifications.route) }
                        )
                        Surface(
                            modifier = Modifier
                                .size(8.dp)
                                .align(Alignment.TopEnd),
                            shape = CircleShape,
                            color = Color(0xFF2ECC71),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary)
                        ) {}
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp).clickable { navController.navigate(Screen.Settings.route) }
                    )
                }
            }
            
            var userName by remember { mutableStateOf("User") }
            LaunchedEffect(Unit) {
                try {
                    val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                    if (currentUser != null) {
                        val prof = SupabaseClient.client.postgrest["profiles"]
                            .select { filter { eq("id", currentUser.id) } }
                            .decodeSingleOrNull<com.simats.vitalmatch.data.models.Donor>()
                        if (prof != null && prof.full_name.isNotBlank()) {
                            userName = prof.full_name
                        } else {
                            SupabaseClient.client.auth.signOut()
                            Toast.makeText(context, "Account cleared from database. Please register.", Toast.LENGTH_LONG).show()
                            navController.navigate(Screen.SignIn.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                } catch (e: Exception) {
                    userName = "User"
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Welcome, $userName!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Help save lives in your community",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun ActionCards(navController: NavController) {
    val cardBg = MaterialTheme.colorScheme.surface
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ActionCard(
            modifier = Modifier.weight(1f),
            title = com.simats.vitalmatch.ui.theme.AppStrings.get("search_donors"),
            icon = Icons.Default.Search,
            containerColor = cardBg,
            contentColor = RedPrimary,
            onClick = { navController.navigate(Screen.Search.route) }
        )
        ActionCard(
            modifier = Modifier.weight(1f),
            title = com.simats.vitalmatch.ui.theme.AppStrings.get("post_emergency"),
            icon = Icons.Default.ErrorOutline,
            containerColor = cardBg,
            contentColor = RedPrimary,
            onClick = { navController.navigate(Screen.PostEmergency.route) }
        )
        ActionCard(
            modifier = Modifier.weight(1f),
            title = com.simats.vitalmatch.ui.theme.AppStrings.get("become_donor"),
            icon = Icons.Default.PersonAdd,
            containerColor = Color(0xFF2ECC71),
            contentColor = Color.White,
            onClick = { navController.navigate(Screen.DonorRegistration.route) }
        )
    }
}

@Composable
fun ActionCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = if (containerColor == Color.White) RedPrimary.copy(alpha = 0.05f) else Color.White.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                color = if (containerColor == Color.White) Color(0xFF1A1C1E) else Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun EmergencyList(navController: NavController) {
    var emergenciesList by remember { mutableStateOf<List<Emergency>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val currentUser = SupabaseClient.client.auth.currentUserOrNull()
            var userDistrict = ""
            if (currentUser != null) {
                val myProfile = SupabaseClient.client.postgrest["profiles"]
                    .select { filter { eq("id", currentUser.id) } }
                    .decodeSingleOrNull<com.simats.vitalmatch.data.models.Donor>()
                if (myProfile != null && !myProfile.district.isNullOrBlank()) {
                    userDistrict = myProfile.district!!
                }
            }

            val allActive = SupabaseClient.client.postgrest["emergency_requests"]
                .select().decodeList<Emergency>()
                .filter { it.status == "Active" }

            emergenciesList = if (userDistrict.isNotBlank()) {
                val districtMatches = allActive.filter { e ->
                    (e.location?.contains(userDistrict, ignoreCase = true) == true) ||
                    (e.user_id != null && e.user_id == currentUser?.id)
                }
                if (districtMatches.isNotEmpty()) districtMatches else allActive
            } else {
                allActive
            }.take(10)
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = RedPrimary)
        }
    } else if (emergenciesList.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
            Text("No active emergencies at the moment", color = GrayText)
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            emergenciesList.forEach { item ->
                val emergencyItem = EmergencyItem(
                    id = item.id ?: "",
                    bloodType = item.blood_group,
                    hospital = item.hospital_name,
                    contactNumber = item.contact_number,
                    location = item.location ?: "",
                    notes = item.notes ?: "",
                    time = item.created_at?.take(10) ?: "Just now",
                    tag = if (item.notes?.contains("urgent", ignoreCase = true) == true) "URGENT" else null,
                    status = item.status
                )
                EmergencyCard(emergencyItem) {
                    navController.navigate(
                        Screen.EmergencyDetail.createRoute(
                            id = emergencyItem.id,
                            bloodGroup = emergencyItem.bloodType,
                            hospital = emergencyItem.hospital,
                            contact = emergencyItem.contactNumber,
                            location = emergencyItem.location,
                            notes = emergencyItem.notes,
                            status = emergencyItem.status
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun EmergencyCard(item: EmergencyItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, RedPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = RedPrimary.copy(alpha = 0.1f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = RedPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.bloodType,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (item.tag != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = RedPrimary,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = item.tag,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        modifier = Modifier.border(1.dp, RedPrimary, RoundedCornerShape(6.dp)),
                        color = Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.status,
                            color = RedPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn, 
                    contentDescription = null, 
                    tint = MaterialTheme.colorScheme.onSurfaceVariant, 
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.hospital, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.onSurfaceVariant, 
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = item.time, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Within Your District",
                        color = Color(0xFF2E7D32),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

data class EmergencyItem(
    val id: String, 
    val bloodType: String, 
    val hospital: String,
    val contactNumber: String,
    val location: String,
    val notes: String,
    val time: String,
    val tag: String?,
    val status: String
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.WaterDrop),
        BottomNavItem("Emergencies", Screen.EmergencyFeed.route, Icons.Default.ErrorOutline),
        BottomNavItem("Requests", Screen.MyRequests.route, Icons.AutoMirrored.Filled.List),
        BottomNavItem("Dashboard", Screen.DonorDashboard.route, Icons.Default.PersonAdd)
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface, tonalElevation = 8.dp) {
        items.forEach { item ->
            val labelText = when(item.label) {
                "Home" -> com.simats.vitalmatch.ui.theme.AppStrings.get("home")
                "Emergencies" -> com.simats.vitalmatch.ui.theme.AppStrings.get("emergencies")
                "Requests" -> com.simats.vitalmatch.ui.theme.AppStrings.get("my_requests")
                "Dashboard" -> com.simats.vitalmatch.ui.theme.AppStrings.get("profile")
                else -> item.label
            }
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = labelText) },
                label = { Text(labelText) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = RedPrimary,
                    selectedTextColor = RedPrimary,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = RedPrimary.copy(alpha = 0.1f)
                )
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRequestsScreen(navController: NavController) {
    var requestsList by remember { mutableStateOf<List<BloodRequest>>(emptyList()) }
    var selectedFilter by remember { mutableStateOf("all") }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val currentUser = SupabaseClient.client.auth.currentUserOrNull()
            if (currentUser != null) {
                requestsList = SupabaseClient.client.postgrest["blood_requests"]
                    .select {
                        filter {
                            eq("donor_user_id", currentUser.id)
                        }
                    }.decodeList<BloodRequest>()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    val filteredList = when (selectedFilter) {
        "Accepted" -> requestsList.filter { it.status == "Accepted" }
        "Completed" -> requestsList.filter { it.status == "Completed" }
        else -> requestsList
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
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
                    text = com.simats.vitalmatch.ui.theme.AppStrings.get("my_requests"),
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage received blood requests and donations",
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )

                // 3 Tabs requested by user
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(
                        selected = selectedFilter == "all",
                        onClick = { selectedFilter = "all" },
                        label = { Text("All Requests", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = selectedFilter == "Accepted",
                        onClick = { selectedFilter = "Accepted" },
                        label = { Text("Accepted Requests", fontSize = 11.sp) }
                    )
                    FilterChip(
                        selected = selectedFilter == "Completed",
                        onClick = { selectedFilter = "Completed" },
                        label = { Text("Completed Donations", fontSize = 11.sp) }
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = RedPrimary)
                }
            } else if (filteredList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No requests found in this tab.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(filteredList) { request ->
                        RequestCard(
                            request = request,
                            onAccept = {
                                scope.launch {
                                    try {
                                        SupabaseClient.client.postgrest["blood_requests"]
                                            .update({ set("status", "Accepted") }) {
                                                filter { eq("id", request.id ?: "") }
                                            }
                                        Toast.makeText(context, "Request Accepted!", Toast.LENGTH_SHORT).show()
                                        requestsList = requestsList.map { if (it.id == request.id) it.copy(status = "Accepted") else it }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onDecline = {
                                scope.launch {
                                    try {
                                        SupabaseClient.client.postgrest["blood_requests"]
                                            .update({ set("status", "Declined") }) {
                                                filter { eq("id", request.id ?: "") }
                                            }
                                        Toast.makeText(context, "Request Declined.", Toast.LENGTH_SHORT).show()
                                        requestsList = requestsList.map { if (it.id == request.id) it.copy(status = "Declined") else it }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            onCompleteDonation = {
                                scope.launch {
                                    try {
                                        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                                        val todayStr = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date())
                                        
                                        SupabaseClient.client.postgrest["blood_requests"]
                                            .update({ set("status", "Completed") }) {
                                                filter { eq("id", request.id ?: "") }
                                            }

                                        if (currentUser != null) {
                                            SupabaseClient.client.postgrest["profiles"].update(
                                                kotlinx.serialization.json.buildJsonObject {
                                                    put("last_donation_date", kotlinx.serialization.json.JsonPrimitive(todayStr))
                                                    put("is_available", kotlinx.serialization.json.JsonPrimitive(false))
                                                }
                                            ) { filter { eq("id", currentUser.id) } }

                                            SupabaseClient.client.postgrest["notifications"].insert(
                                                com.simats.vitalmatch.data.models.NotificationModel(
                                                    user_id = currentUser.id,
                                                    title = "Donation Completed!",
                                                    message = "Donated blood on $todayStr. Profile updated.",
                                                    type = "DONATION_SUCCESS"
                                                )
                                            )
                                        }

                                        Toast.makeText(context, "Donation Completed! Profile Updated ($todayStr).", Toast.LENGTH_LONG).show()
                                        requestsList = requestsList.map { if (it.id == request.id) it.copy(status = "Completed") else it }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Donation Completed!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RequestCard(
    request: BloodRequest, 
    onAccept: () -> Unit, 
    onDecline: () -> Unit,
    onCompleteDonation: () -> Unit = {},
    onCancelRequest: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.requester_name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Surface(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = when (request.status) {
                            "Completed" -> Color(0xFF3498DB)
                            "Accepted" -> Color(0xFF2ECC71)
                            "Declined" -> RedPrimary
                            else -> Color(0xFFF39C12)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                    color = when (request.status) {
                        "Completed" -> Color(0xFFEBF5FB)
                        "Accepted" -> Color(0xFFE8F5E9)
                        "Declined" -> Color(0xFFFFEBEE)
                        else -> Color(0xFFFEF9E7)
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = request.status,
                        color = when (request.status) {
                            "Completed" -> Color(0xFF2980B9)
                            "Accepted" -> Color(0xFF2E7D32)
                            "Declined" -> RedPrimary
                            else -> Color(0xFFD68910)
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Call, contentDescription = null, tint = GrayText, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = request.requester_phone, fontSize = 14.sp, color = GrayText)
            }
            
            if (request.status == "Pending") {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDecline,
                        modifier = Modifier.weight(1f).height(48.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Decline", fontWeight = FontWeight.Bold)
                    }
                    
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Accept", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            } else if (request.status == "Accepted") {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onCompleteDonation,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Completed Donation & Update Profile", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onCancelRequest,
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary)
                ) {
                    Text("Cancel / Reject Request (Did Not Donate)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun fetchLocation(context: Context, onResult: (String) -> Unit) {
    try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Request a fresh, active GPS fix (not cached lastLocation which can be null)
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, 1000L
        ).setMaxUpdates(1).build()

        val locationCallback = object : com.google.android.gms.location.LocationCallback() {
            override fun onLocationResult(result: com.google.android.gms.location.LocationResult) {
                val location = result.lastLocation
                if (location != null) {
                    try {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val address = addresses[0]
                            val city = address.locality ?: address.subAdminArea ?: "Unknown City"
                            val state = address.adminArea ?: "Unknown State"
                            onResult("$city, $state")
                        } else {
                            onResult("Location unavailable")
                        }
                    } catch (e: Exception) {
                        onResult("Location unavailable")
                    }
                } else {
                    onResult("Enable GPS for live location")
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        // Try lastLocation first for instant display, then request fresh fix
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val city = address.locality ?: address.subAdminArea ?: "Unknown City"
                        val state = address.adminArea ?: "Unknown State"
                        onResult("$city, $state")
                    } else {
                        // No cached location, request fresh
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, android.os.Looper.getMainLooper())
                    }
                } catch (e: Exception) {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, android.os.Looper.getMainLooper())
                }
            } else {
                // No cached location, request active GPS fix
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, android.os.Looper.getMainLooper())
            }
        }.addOnFailureListener {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, android.os.Looper.getMainLooper())
        }
    } catch (e: Exception) {
        onResult("Location error")
    }
}

