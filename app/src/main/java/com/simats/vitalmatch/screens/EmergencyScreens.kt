package com.simats.vitalmatch.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.simats.vitalmatch.Screen
import com.simats.vitalmatch.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.auth.auth
import com.simats.vitalmatch.data.remote.SupabaseClient
import com.simats.vitalmatch.data.models.Emergency
import com.simats.vitalmatch.data.models.Donor
import com.simats.vitalmatch.data.models.NotificationModel
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import android.widget.Toast
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostEmergencyScreen(navController: NavController) {
    var bloodType by remember { mutableStateOf("") }
    var hospital by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    var country by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var radiusKm by remember { mutableStateOf("5 km") }
    
    val countries = com.simats.vitalmatch.data.LocationData.countries
    val statesMap = com.simats.vitalmatch.data.LocationData.statesMap
    val districtsMap = com.simats.vitalmatch.data.LocationData.districtsMap
    val citiesMap = com.simats.vitalmatch.data.LocationData.citiesMap

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
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
            text = com.simats.vitalmatch.ui.theme.AppStrings.get("post_emergency"),
            modifier = Modifier.padding(top = 24.dp),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = com.simats.vitalmatch.ui.theme.AppStrings.get("request_help"),
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        SearchDropdownField(
            label = com.simats.vitalmatch.ui.theme.AppStrings.get("blood_type"),
            value = bloodType,
            placeholder = "Select blood group",
            options = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"),
            onOptionSelected = { bloodType = it }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(com.simats.vitalmatch.ui.theme.AppStrings.get("hospital_name"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = hospital,
            onValueChange = { hospital = it },
            placeholder = { Text("Enter hospital name", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(com.simats.vitalmatch.ui.theme.AppStrings.get("contact_number"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            placeholder = { Text("Enter contact number", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        SearchDropdownField(
            label = com.simats.vitalmatch.ui.theme.AppStrings.get("country"), 
            value = country, 
            placeholder = "Select country",
            options = countries,
            onOptionSelected = { 
                country = it 
                state = "" 
                district = ""
                city = ""
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchDropdownField(
            label = com.simats.vitalmatch.ui.theme.AppStrings.get("state"), 
            value = state, 
            placeholder = com.simats.vitalmatch.ui.theme.AppStrings.get("select_state"),
            options = statesMap[country] ?: emptyList(),
            onOptionSelected = { 
                state = it 
                district = "" 
                city = ""
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchDropdownField(
            label = com.simats.vitalmatch.ui.theme.AppStrings.get("district"), 
            value = district, 
            placeholder = com.simats.vitalmatch.ui.theme.AppStrings.get("select_district"),
            options = districtsMap[state] ?: emptyList(),
            onOptionSelected = { 
                district = it 
                city = "" 
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchDropdownField(
            label = com.simats.vitalmatch.ui.theme.AppStrings.get("city"), 
            value = city, 
            placeholder = com.simats.vitalmatch.ui.theme.AppStrings.get("select_city"),
            options = citiesMap[district] ?: emptyList(),
            onOptionSelected = { city = it }
        )
        Spacer(modifier = Modifier.height(16.dp))

        SearchDropdownField(
            label = "Notification Radius", 
            value = radiusKm, 
            placeholder = "Select notification radius",
            options = listOf("5 km", "10 km", "15 km", "25 km", "50 km"),
            onOptionSelected = { radiusKm = it }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(com.simats.vitalmatch.ui.theme.AppStrings.get("additional_notes"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            placeholder = { Text("Reason for emergency...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = {
                if (bloodType.isEmpty() || hospital.isEmpty() || contact.isEmpty()) {
                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                isLoading = true
                scope.launch {
                    try {
                        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                        val userName = currentUser?.userMetadata?.get("full_name")?.toString()?.trim('"') ?: "Unknown User"
                        val loc = listOfNotNull(city.ifEmpty { null }, district.ifEmpty { null }, state.ifEmpty { null }, country.ifEmpty { null }).joinToString(", ")
                        
                        val emergency = Emergency(
                            user_id = currentUser?.id,
                            patient_name = userName,
                            blood_group = bloodType,
                            hospital_name = hospital,
                            contact_number = contact,
                            location = loc.ifEmpty { "India" },
                            district = district.ifEmpty { null },
                            state = state.ifEmpty { null },
                            city = city.ifEmpty { null },
                            notes = message,
                            priority = "High",
                            status = "Active"
                        )
                        SupabaseClient.client.postgrest["emergency_requests"].insert(emergency)

                        // Geo-Based Notification System: Strict distance filtering (Haversine formula)
                        val radiusVal = radiusKm.replace("km", "").trim().toDoubleOrNull() ?: 5.0
                        val emCityName = city.ifBlank { district.ifBlank { "Chirala" } }
                        val emCoords = com.simats.vitalmatch.data.LocationData.resolveCityCoordinates(emCityName)

                        val allDonors = SupabaseClient.client.postgrest["profiles"]
                            .select {
                                filter {
                                    eq("is_donor", true)
                                    eq("blood_group", bloodType)
                                    eq("is_available", true)
                                }
                            }.decodeList<Donor>()

                        for (d in allDonors) {
                            val dId = d.id
                            val daysSince = calculateDaysSince(d.last_donation_date ?: "")
                            val isNotHospitalized = d.hospitalization_status != "Yes"
                            if (!dId.isNullOrBlank() && dId != currentUser?.id && daysSince >= 90 && isNotHospitalized) {
                                val donorCoords = if (d.latitude != null && d.longitude != null) {
                                    Pair(d.latitude, d.longitude)
                                } else {
                                    val donorCityName = d.city ?: d.district ?: ""
                                    com.simats.vitalmatch.data.LocationData.resolveCityCoordinates(donorCityName)
                                }

                                if (emCoords != null && donorCoords != null) {
                                    val dist = com.simats.vitalmatch.data.LocationData.calculateDistanceKm(
                                        emCoords.first, emCoords.second,
                                        donorCoords.first, donorCoords.second
                                    )
                                    // STRICT RADIUS CHECK: Only send notification if distance <= selected radius!
                                    if (dist <= radiusVal) {
                                        val distFormatted = String.format(java.util.Locale.US, "%.1f", dist)
                                        val notif = NotificationModel(
                                            user_id = dId,
                                            title = "URGENT: $bloodType Blood Needed (${distFormatted}km away)",
                                            message = "Emergency blood request at $hospital ($loc), ${distFormatted}km from your location. Contact: $contact",
                                            type = "URGENT"
                                        )
                                        SupabaseClient.client.postgrest["notifications"].insert(notif)
                                    }
                                }
                            }
                        }

                        Toast.makeText(context, "POST EMERGENCY SUCCESSFULLY", Toast.LENGTH_LONG).show()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Post Emergency", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun PostEmergencyOtpScreen(navController: NavController) {
    var otp by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .clickable { navController.navigateUp() }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BlueDark)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back", color = BlueDark, fontSize = 16.sp)
        }

        Text(
            text = "Verify Request",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter OTP sent to your mobile number to verify this emergency post",
            modifier = Modifier.padding(top = 4.dp),
            color = GrayText,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        OtpInputField(otpText = otp, onOtpTextChange = { if (it.length <= 6) otp = it })

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Resend OTP in 26s",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = GrayText,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { navController.navigate(Screen.EmergencyFeed.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Verify & Post", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmergencyFeedScreen(navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        com.simats.vitalmatch.ui.theme.AppStrings.get("all"),
        com.simats.vitalmatch.ui.theme.AppStrings.get("active"),
        com.simats.vitalmatch.ui.theme.AppStrings.get("connected")
    )
    var emergenciesList by remember { mutableStateOf<List<Emergency>>(emptyList()) }
    var currentUserDistrict by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        try {
            val currentUser = SupabaseClient.client.auth.currentUserOrNull()
            if (currentUser != null) {
                val myProfile = SupabaseClient.client.postgrest["profiles"]
                    .select { filter { eq("id", currentUser.id) } }
                    .decodeSingleOrNull<Donor>()
                if (myProfile != null && !myProfile.district.isNullOrBlank()) {
                    currentUserDistrict = myProfile.district!!
                }
            }
            val allEmergencies = SupabaseClient.client.postgrest["emergency_requests"]
                .select().decodeList<Emergency>()

            emergenciesList = if (currentUserDistrict.isNotBlank()) {
                allEmergencies.filter { e ->
                    (e.district != null && e.district.equals(currentUserDistrict, ignoreCase = true)) ||
                    (e.location?.contains(currentUserDistrict, ignoreCase = true) == true) ||
                    (e.user_id != null && e.user_id == currentUser?.id)
                }
            } else {
                allEmergencies
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching feed: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    val filteredEmergencies = when (selectedTab) {
        1 -> emergenciesList.filter { it.status == "Active" }
        2 -> emergenciesList.filter { it.status == "Connected" }
        else -> emergenciesList
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
                text = com.simats.vitalmatch.ui.theme.AppStrings.get("emergency_feed"),
                modifier = Modifier.padding(top = 24.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = com.simats.vitalmatch.ui.theme.AppStrings.get("emergency_feed_sub"),
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )

            // Custom Tab Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(24.dp))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) RedPrimary else Color.Transparent)
                            .clickable { selectedTab = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RedPrimary)
            }
        } else if (filteredEmergencies.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No emergencies found.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(filteredEmergencies) { item ->
                    val feedItem = FeedEmergencyItem(
                        id = item.id ?: "",
                        bloodType = item.blood_group,
                        posterName = item.patient_name.ifEmpty { "Patient" },
                        hospital = item.hospital_name,
                        contactNumber = item.contact_number,
                        location = item.location ?: "",
                        notes = item.notes ?: "",
                        time = item.created_at?.take(10) ?: "Just now",
                        urgencyTag = if (item.notes?.contains("urgent", ignoreCase = true) == true) "URGENT" else null,
                        status = item.status
                    )
                    FeedEmergencyCard(feedItem) {
                        navController.navigate(
                            Screen.EmergencyDetail.createRoute(
                                id = feedItem.id,
                                bloodGroup = feedItem.bloodType,
                                hospital = feedItem.hospital,
                                contact = feedItem.contactNumber,
                                location = feedItem.location,
                                notes = feedItem.notes,
                                status = feedItem.status
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeedEmergencyCard(item: FeedEmergencyItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (item.urgencyTag == "URGENT") {
                    Modifier.border(1.dp, RedPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Blood drop icon box
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            if (item.status == "Connected") Color(0xFFF0F2F5) else RedPrimary.copy(alpha = 0.1f), 
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = if (item.status == "Connected") Color(0xFF4A4E69) else RedPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.bloodType,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (item.urgencyTag != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = RedPrimary,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = item.urgencyTag,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = item.posterName,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Surface(
                    modifier = Modifier.border(
                        width = 1.dp, 
                        color = if (item.status == "Connected") Color(0xFF2ECC71) else RedPrimary, 
                        shape = RoundedCornerShape(12.dp)
                    ),
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = item.status,
                        color = if (item.status == "Connected") Color(0xFF2ECC71) else RedPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn, 
                    contentDescription = null, 
                    tint = Color.Gray, 
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = item.hospital, fontSize = 14.sp, color = Color(0xFF4A4E69))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime, 
                        contentDescription = null, 
                        tint = Color.Gray, 
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = item.time, fontSize = 14.sp, color = Color.Gray)
                }
                
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "Within Your District",
                        color = Color(0xFF2E7D32),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmergencyDetailScreen(
    navController: NavController,
    emergencyId: String = "",
    bloodGroupArg: String = "",
    hospitalArg: String = "",
    contactArg: String = "",
    locationArg: String = "",
    notesArg: String = "",
    statusArg: String = ""
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var bloodGroup by remember { mutableStateOf(bloodGroupArg.ifEmpty { "O+" }) }
    var contactNumber by remember { mutableStateOf(contactArg) }
    var hospitalName by remember { mutableStateOf(hospitalArg) }
    var locationStr by remember { mutableStateOf(locationArg) }
    var notesStr by remember { mutableStateOf(notesArg) }
    var status by remember { mutableStateOf(statusArg.ifEmpty { "Active" }) }
    var showBloodReceivedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(emergencyId) {
        if (emergencyId.isNotEmpty()) {
            try {
                val item = SupabaseClient.client.postgrest["emergency_requests"]
                    .select { filter { eq("id", emergencyId) } }
                    .decodeSingleOrNull<Emergency>()
                if (item != null) {
                    bloodGroup = item.blood_group
                    contactNumber = item.contact_number
                    hospitalName = item.hospital_name
                    locationStr = item.location ?: ""
                    notesStr = item.notes ?: ""
                    status = item.status
                }
            } catch (e: Exception) { }
        }
    }

    val fullAddress = remember(hospitalName, locationStr) {
        val parts = listOf(hospitalName, locationStr).filter { it.isNotBlank() }.distinct()
        if (parts.isNotEmpty()) parts.joinToString(", ") else "Hospital Address Not Provided"
    }
    val statusColor = if (status == "Connected") Color(0xFF2ECC71) else RedPrimary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Back Button
            Row(
                modifier = Modifier
                    .clickable { navController.navigateUp() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BlueDark)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back", color = BlueDark, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Header Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFEBEE)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.WaterDrop,
                            contentDescription = null,
                            tint = RedPrimary,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = bloodGroup,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Surface(
                            color = RedPrimary,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "URGENT",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        modifier = Modifier.border(1.dp, statusColor, RoundedCornerShape(8.dp)),
                        color = Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            status,
                            color = statusColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Patient Details Card (ONLY Contact Number, Patient Name REMOVED)
            DetailSectionCard(title = "Patient Details") {
                DetailItem(Icons.Default.PhoneInTalk, "Contact Number", contactNumber.ifEmpty { "Contact Number Not Provided" })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location Details Card (ONLY Hospital Address)
            DetailSectionCard(title = "Location Details") {
                DetailItem(Icons.Default.LocationOn, "Hospital Address", fullAddress)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Request Info Card
            DetailSectionCard(title = "Request Info") {
                DetailItem(Icons.AutoMirrored.Filled.Notes, "Additional Notes", notesStr.ifEmpty { "No additional details provided." })
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom Action Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    showBloodReceivedDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Mark Blood Received & Completed", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactNumber"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Calling $contactNumber...", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhoneInTalk, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contact", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                OutlinedButton(
                    onClick = {
                        status = "Connected"
                        scope.launch {
                            if (emergencyId.isNotEmpty()) {
                                try {
                                    SupabaseClient.client.postgrest["emergency_requests"].update(
                                        buildJsonObject { put("status", "Connected") }
                                    ) { filter { eq("id", emergencyId) } }
                                } catch (e: Exception) { }
                            }
                        }
                        Toast.makeText(context, "Emergency Request Accepted!", Toast.LENGTH_SHORT).show()
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactNumber"))
                            context.startActivity(intent)
                        } catch (e: Exception) { }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary)
                ) {
                    Text("Accept Request", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = RedPrimary)
                }
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Emergency Report submitted to Moderation Team", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Flag, contentDescription = null, tint = BlueDark, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Report Emergency", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BlueDark)
                }
            }
        }
    }

    if (showBloodReceivedDialog) {
        BloodReceivedDialog(
            hospitalName = hospitalName,
            onDismiss = { showBloodReceivedDialog = false },
            onConfirm = { donorInput ->
                scope.launch {
                    try {
                        val todayStr = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date())
                        
                        if (emergencyId.isNotEmpty()) {
                            SupabaseClient.client.postgrest["emergency_requests"].update(
                                buildJsonObject { put("status", "Completed") }
                            ) { filter { eq("id", emergencyId) } }
                        }

                        val allProfiles = SupabaseClient.client.postgrest["profiles"]
                            .select().decodeList<Donor>()

                        val cleanInputDigits = donorInput.filter { it.isDigit() }
                        val matchedDonor = allProfiles.firstOrNull { d ->
                            val cleanMobileDigits = d.mobile.filter { it.isDigit() }
                            (cleanMobileDigits.isNotEmpty() && cleanInputDigits.contains(cleanMobileDigits)) ||
                            (cleanMobileDigits.length >= 10 && cleanInputDigits.endsWith(cleanMobileDigits.takeLast(10))) ||
                            (d.full_name.isNotBlank() && d.full_name.contains(donorInput, ignoreCase = true))
                        } ?: allProfiles.firstOrNull()

                        if (matchedDonor != null && matchedDonor.id != null) {
                            SupabaseClient.client.postgrest["profiles"].update(
                                buildJsonObject {
                                    put("last_donation_date", todayStr)
                                    put("is_available", false)
                                }
                            ) { filter { eq("id", matchedDonor.id) } }

                            val previousDonations = SupabaseClient.client.postgrest["notifications"]
                                .select {
                                    filter {
                                        eq("user_id", matchedDonor.id)
                                        eq("type", "DONATION_SUCCESS")
                                    }
                                }.decodeList<NotificationModel>()

                            val count = previousDonations.size + 1
                            val ordinal = when (count) {
                                1 -> "1st"
                                2 -> "2nd"
                                3 -> "3rd"
                                else -> "${count}th"
                            }

                            val notif = NotificationModel(
                                user_id = matchedDonor.id,
                                title = "$ordinal Donation Completed!",
                                message = "$ordinal Donation - Donated Blood on $todayStr",
                                type = "DONATION_SUCCESS"
                            )
                            SupabaseClient.client.postgrest["notifications"].insert(notif)
                        }

                        status = "Completed"
                        Toast.makeText(context, "Blood Received! Donor profile & last donation date updated to $todayStr.", Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Completed! Error updating donor profile: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        showBloodReceivedDialog = false
                    }
                }
            }
        )
    }
}

@Composable
fun BloodReceivedDialog(
    hospitalName: String,
    onDismiss: () -> Unit,
    onConfirm: (donorPhoneOrName: String) -> Unit
) {
    var donorInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Confirm Blood Received", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Please enter the Mobile Number or Name of the Donor who donated blood:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                OutlinedTextField(
                    value = donorInput,
                    onValueChange = { donorInput = it },
                    label = { Text("Donor Mobile Number / Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (donorInput.isNotBlank()) {
                        onConfirm(donorInput.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2ECC71))
            ) {
                Text("Confirm & Record Donation", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}

@Composable
fun DetailSectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F2F5))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp).offset(y = 4.dp)
        )
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Text(label, fontSize = 14.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = RedPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = Color.Gray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

data class FeedEmergencyItem(
    val id: String,
    val bloodType: String,
    val posterName: String,
    val hospital: String,
    val contactNumber: String,
    val location: String,
    val notes: String,
    val time: String,
    val urgencyTag: String?,
    val status: String
)
