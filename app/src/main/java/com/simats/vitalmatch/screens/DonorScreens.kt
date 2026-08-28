package com.simats.vitalmatch.screens

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.simats.vitalmatch.Screen
import com.simats.vitalmatch.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.simats.vitalmatch.data.remote.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorRegistrationScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var lastDonationDate by remember { mutableStateOf("") }
    var hospitalizationStatus by remember { mutableStateOf("No") }
    
    var country by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    val isFormValid =
        fullName.isNotBlank() &&
                bloodGroup.isNotBlank() &&
                phoneNumber.isNotBlank() &&
                country.isNotBlank() &&
                state.isNotBlank() &&
                district.isNotBlank() &&
                city.isNotBlank()

    var isAvailable by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    
    val countries = com.simats.vitalmatch.data.LocationData.countries
    val statesMap = com.simats.vitalmatch.data.LocationData.statesMap
    val districtsMap = com.simats.vitalmatch.data.LocationData.districtsMap
    val citiesMap = com.simats.vitalmatch.data.LocationData.citiesMap

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            lastDonationDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis

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

        Text(com.simats.vitalmatch.ui.theme.AppStrings.get("become_donor"), modifier = Modifier.padding(top = 24.dp), color = MaterialTheme.colorScheme.onSurface, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Register as a blood donor and save lives", modifier = Modifier.padding(top = 4.dp), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Text(com.simats.vitalmatch.ui.theme.AppStrings.get("full_name"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = { Text("Enter your full name", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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

        Text(AppStrings.get("blood_type"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        Box(modifier = Modifier.padding(top = 8.dp)) {
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = bloodGroup,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text(AppStrings.get("select_blood_group"), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
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
                ExposedDropdownMenu(
                    expanded = expanded, 
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    bloodGroups.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption, color = MaterialTheme.colorScheme.onSurface) },
                            onClick = { bloodGroup = selectionOption; expanded = false },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(AppStrings.get("mobile_number"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = { Text("Enter your phone number", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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
        
        // Location Fields
        SearchDropdownField(
            label = AppStrings.get("country"), 
            value = country, 
            placeholder = "Select country",
            options = countries,
            onOptionSelected = { 
                country = it 
                state = "" // Reset dependents
                district = ""
                city = ""
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchDropdownField(
            label = AppStrings.get("state"), 
            value = state, 
            placeholder = AppStrings.get("select_state"),
            options = statesMap[country] ?: emptyList(),
            onOptionSelected = { 
                state = it 
                district = "" // Reset dependents
                city = ""
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchDropdownField(
            label = AppStrings.get("district"), 
            value = district, 
            placeholder = AppStrings.get("select_district"),
            options = districtsMap[state] ?: emptyList(),
            onOptionSelected = { 
                district = it 
                city = "" // Reset dependents
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        SearchDropdownField(
            label = AppStrings.get("city"), 
            value = city, 
            placeholder = AppStrings.get("select_city"),
            options = citiesMap[district] ?: emptyList(),
            onOptionSelected = { city = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(AppStrings.get("last_donation_date"), fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = lastDonationDate,
            onValueChange = { },
            readOnly = true,
            placeholder = { Text("dd-mm-yyyy", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp).clickable { datePickerDialog.show() },
            enabled = false, // Disable to handle click on box
            trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledBorderColor = Color(0xFFE5E7EB),
                disabledTextColor = MaterialTheme.colorScheme.onSurface
            )
        )
        Text("Leave empty if you haven't donated before", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))

        Spacer(modifier = Modifier.height(32.dp))

        Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(AppStrings.get("available"), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    Text("You can change this anytime", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(checked = isAvailable, onCheckedChange = { isAvailable = it })
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    try {
                        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                        val formattedDate = if (lastDonationDate.isNotBlank()) {
                            val parts = lastDonationDate.split("-")
                            if (parts.size == 3) "${parts[2]}-${parts[1]}-${parts[0]}" else lastDonationDate
                        } else null

                        try {
                            SupabaseClient.client.postgrest["profiles"]
                                .update(
                                    buildJsonObject {
                                        put("full_name", fullName)
                                        put("blood_group", bloodGroup)
                                        put("mobile", phoneNumber)
                                        if (formattedDate != null) {
                                            put("last_donation_date", formattedDate)
                                        }
                                        put("hospitalization_status", hospitalizationStatus)
                                        put("is_available", isAvailable)
                                        put("is_donor", true)
                                        put("state", state)
                                        put("district", district)
                                        put("city", city)
                                    }
                                ) {
                                    filter {
                                        eq("id", currentUser?.id ?: "")
                                    }
                                }
                        } catch (primaryErr: Exception) {
                            SupabaseClient.client.postgrest["profiles"]
                                .update(
                                    buildJsonObject {
                                        put("full_name", fullName)
                                        put("blood_group", bloodGroup)
                                        put("mobile", phoneNumber)
                                        if (formattedDate != null) {
                                            put("last_donation_date", formattedDate)
                                        }
                                        put("is_available", isAvailable)
                                        put("is_donor", true)
                                        put("state", state)
                                        put("district", district)
                                        put("city", city)
                                    }
                                ) {
                                    filter {
                                        eq("id", currentUser?.id ?: "")
                                    }
                                }
                        }

                        val sharedPreferences =
                            context.getSharedPreferences("vitalmatch", Context.MODE_PRIVATE)

                        sharedPreferences.edit()
                            .putString("last_donation_date", lastDonationDate)
                            .apply()

                        Toast.makeText(context, "DONAR REGISTERED SUCCESSFULLY", Toast.LENGTH_LONG).show()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Registration Failed: ${e.message}", Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            enabled = isFormValid && !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = RedPrimary,                    // Dark red when enabled
                disabledContainerColor = Color(0xFFF59B99),     // Light red when disabled
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Register as Donor", fontSize = 18.sp, color = White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DonorDashboardScreen(navController: NavController) {
    var isAvailableInternal by remember { mutableStateOf(true) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val sharedPreferences =
        context.getSharedPreferences("vitalmatch", Context.MODE_PRIVATE)

    val userName =
        sharedPreferences.getString("user_name", "User") ?: "User"
    var lastDonationDate by remember {
        mutableStateOf(
            sharedPreferences.getString(
                "last_donation_date",
                ""
            ) ?: ""
        )
    }
    var pastDonations by remember { mutableStateOf<List<com.simats.vitalmatch.data.models.NotificationModel>>(emptyList()) }
    var userBloodGroup by remember { mutableStateOf("O+") }

    LaunchedEffect(Unit) {
        try {
            val currentUser = com.simats.vitalmatch.data.remote.SupabaseClient.client.auth.currentUserOrNull()
            if (currentUser != null) {
                val profile = com.simats.vitalmatch.data.remote.SupabaseClient.client.postgrest["profiles"]
                    .select { filter { eq("id", currentUser.id) } }
                    .decodeSingleOrNull<com.simats.vitalmatch.data.models.Donor>()
                if (profile != null) {
                    if (!profile.last_donation_date.isNullOrBlank()) {
                        lastDonationDate = profile.last_donation_date!!
                        sharedPreferences.edit().putString("last_donation_date", profile.last_donation_date).apply()
                    }
                    if (profile.blood_group.isNotBlank()) {
                        userBloodGroup = profile.blood_group
                    }
                }
                // Automatically turn ON availability button when days >= 90 and not hospitalized
                val currentDays = calculateDaysSince(lastDonationDate)
                val isHospitalized = profile?.hospitalization_status == "Yes"
                if (currentDays >= 90 && !isHospitalized) {
                    isAvailableInternal = true
                } else {
                    isAvailableInternal = false
                }
                pastDonations = com.simats.vitalmatch.data.remote.SupabaseClient.client.postgrest["notifications"]
                    .select {
                        filter {
                            eq("user_id", currentUser.id)
                            eq("type", "DONATION_SUCCESS")
                        }
                    }.decodeList<com.simats.vitalmatch.data.models.NotificationModel>()
            }
        } catch (e: Exception) { }
    }
    
    val daysSinceLastDonation = remember(lastDonationDate) { calculateDaysSince(lastDonationDate) }
    val isEligible = daysSinceLastDonation >= 90
    val statusColor = if (isEligible) GreenSuccess else RedPrimary

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(modifier = Modifier.fillMaxWidth().background(color = RedPrimary, shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)).padding(bottom = 24.dp)) {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(modifier = Modifier.clickable { navController.navigateUp() }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.get("back"), color = White, fontSize = 16.sp)
                        }
                        IconButton(onClick = { showLogoutDialog = true }) {
                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout", tint = White)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(64.dp), shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.2f)) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.WaterDrop, contentDescription = null, tint = White, modifier = Modifier.size(32.dp)) }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = userName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                            Text("Blood Donor", fontSize = 16.sp, color = White.copy(alpha = 0.9f))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), color = Color.White.copy(alpha = 0.15f)) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(AppStrings.get("available"), fontWeight = FontWeight.Bold, fontSize = 16.sp, color = White)
                                Text(
                                    if (isEligible)
                                        "You will receive emergency alerts"
                                    else
                                        "Available after completing 90 days",
                                    fontSize = 12.sp,
                                    color = White.copy(alpha = 0.9f)
                                )
                            }
                            Switch(
                                checked = if (isEligible) isAvailableInternal else false,
                                onCheckedChange = { newValue ->
                                    if (isEligible) {
                                        isAvailableInternal = newValue
                                    } else {
                                        isAvailableInternal = false
                                    }
                                },
                                enabled = isEligible,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = RedPrimary,
                                    checkedTrackColor = White
                                )
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Blood Group Card
                DashboardCard {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(AppStrings.get("blood_type"), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(userBloodGroup, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Surface(modifier = Modifier.size(56.dp), shape = RoundedCornerShape(12.dp), color = RedPrimary.copy(alpha = 0.15f)) {
                            Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.WaterDrop, contentDescription = null, tint = RedPrimary, modifier = Modifier.size(28.dp)) }
                        }
                    }
                }

                // Days Since Last Donation Card
                val daysRemaining = if (lastDonationDate.isBlank()) 0L else maxOf(0L, 90L - daysSinceLastDonation)
                val daysTextDisplay = if (lastDonationDate.isBlank()) "Eligible" else if (isEligible) "${daysSinceLastDonation}d" else "${daysRemaining}d"

                DashboardCard(borderColor = statusColor.copy(alpha = 0.5f)) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(AppStrings.get("last_donation_date"), fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = daysTextDisplay, fontSize = 48.sp, fontWeight = FontWeight.ExtraBold, color = statusColor)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(if (isEligible) Icons.Default.Check else Icons.Default.Close, contentDescription = null, tint = statusColor, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isEligible) "Eligible to donate" else "Eligible in $daysRemaining day(s)", color = statusColor, fontWeight = FontWeight.Medium)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (lastDonationDate.isBlank()) "You can donate blood today and save lives!" 
                                   else if (isEligible) "You have completed the required 90 days" 
                                   else "You need to wait $daysRemaining more days before donating", 
                            fontSize = 12.sp, 
                            color = MaterialTheme.colorScheme.onSurfaceVariant, 
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Eligibility Status Card
                DashboardCard(borderColor = statusColor.copy(alpha = 0.5f)) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Eligibility Status", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Surface(color = statusColor.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp)) {
                                Text(if (isEligible) "Eligible" else "Not Eligible", color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Last donation: ${if (lastDonationDate.isBlank()) "Never donated" else lastDonationDate}", color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedButton(
                            onClick = { showUpdateDialog = true }, 
                            modifier = Modifier.fillMaxWidth().height(56.dp), 
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary)
                        ) {
                            Text("Update Last Donation Date", color = RedPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Donation History Card
                DashboardCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, contentDescription = null, tint = RedPrimary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Donation History", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        if (pastDonations.isEmpty()) {
                            Text("No blood donations recorded yet.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        } else {
                            pastDonations.forEach { donation ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    color = RedPrimary.copy(alpha = 0.08f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = donation.message,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Verified & Profile Updated",
                                                fontSize = 12.sp,
                                                color = Color(0xFF2ECC71)
                                            )
                                        }
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(20.dp))
                                    }
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showUpdateDialog) {
        UpdateDonationDateDialog(
            currentDate = lastDonationDate,
            onDismiss = { showUpdateDialog = false },
            onUpdate = { newDate ->
                lastDonationDate = newDate

                sharedPreferences.edit()
                    .putString("last_donation_date", newDate)
                    .apply()

                showUpdateDialog = false
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            title = { Text(AppStrings.get("logout"), color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("Are you sure you want to logout?", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = { TextButton(onClick = { navController.navigate(Screen.AuthSelection.route) { popUpTo(0) } }) { Text(AppStrings.get("logout"), color = RedPrimary) } },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
        )
    }
}

@Composable
fun UpdateDonationDateDialog(currentDate: String, onDismiss: () -> Unit, onUpdate: (String) -> Unit) {
    var dateText by remember { mutableStateOf(currentDate) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateText = String.format(java.util.Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text("Update Last Donation Date", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.align(Alignment.Center))
                    IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.TopEnd).size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Text("Please enter the date of your last blood donation", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
                Spacer(modifier = Modifier.height(32.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Donation Date", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = dateText,
                        onValueChange = { },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth().clickable { datePickerDialog.show() },
                        enabled = false,
                        shape = RoundedCornerShape(16.dp),
                        trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledBorderColor = Color(0xFFE5E7EB),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { onUpdate(dateText) }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = RedPrimary), shape = RoundedCornerShape(28.dp)) {
                    Text("Update", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = White)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(28.dp)) {
                    Text("Cancel", fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun calculateDaysSince(dateString: String): Long {
    if (dateString.isBlank()) return 999L
    return try {
        val sdfIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val sdfIndian = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val donationDate = try { sdfIso.parse(dateString) } catch (e: Exception) { sdfIndian.parse(dateString) }
        val today = Calendar.getInstance().time
        if (donationDate != null) {
            val diff = today.time - donationDate.time
            TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        } else 999L
    } catch (_: Exception) { 999L }
}

@Composable
fun DashboardCard(borderColor: Color = Color.Transparent, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().border(width = if (borderColor != Color.Transparent) 1.dp else 0.dp, color = borderColor, shape = RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 2.dp
    ) { Box(modifier = Modifier.padding(24.dp)) { content() } }
}
