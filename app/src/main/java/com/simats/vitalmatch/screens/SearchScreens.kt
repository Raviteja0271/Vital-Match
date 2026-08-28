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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.OutlinedFlag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import com.simats.vitalmatch.Screen
import com.simats.vitalmatch.ui.theme.*
import io.github.jan.supabase.postgrest.postgrest
import com.simats.vitalmatch.data.remote.SupabaseClient
import com.simats.vitalmatch.data.models.Donor
import com.simats.vitalmatch.data.LocationData
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import com.simats.vitalmatch.data.models.BloodRequest
import com.simats.vitalmatch.data.models.NotificationModel
import io.github.jan.supabase.auth.auth
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val context = LocalContext.current
    var bloodGroup by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // Use Real Data for Hierarchy
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
    val countries = LocationData.countries
    val statesMap = LocationData.statesMap
    val districtsMap = LocationData.districtsMap
    val citiesMap = LocationData.citiesMap

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Back Button
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
            text = com.simats.vitalmatch.ui.theme.AppStrings.get("search_donors"),
            modifier = Modifier.padding(top = 24.dp),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Find blood donors near you",
            modifier = Modifier.padding(top = 4.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        SearchDropdownField(
            label = "Blood Group", 
            value = bloodGroup, 
            placeholder = "Select blood group",
            options = bloodGroups,
            onOptionSelected = { bloodGroup = it }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        SearchDropdownField(
            label = "Country", 
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
        Spacer(modifier = Modifier.height(24.dp))
        
        SearchDropdownField(
            label = "State", 
            value = state, 
            placeholder = "Select state",
            options = statesMap[country] ?: emptyList(),
            onOptionSelected = { 
                state = it 
                district = "" // Reset dependents
                city = ""
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // 4. District
        SearchDropdownField(
            label = "District", 
            value = district, 
            placeholder = "Select district",
            options = districtsMap[state] ?: emptyList(),
            onOptionSelected = { 
                district = it 
                city = "" // Reset dependents
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // 5. Town/City
        SearchDropdownField(
            label = "Town/City", 
            value = city, 
            placeholder = "Select town/city",
            options = citiesMap[district] ?: emptyList(),
            onOptionSelected = { city = it }
        )

        Spacer(modifier = Modifier.height(48.dp))
        
        val isSearchEnabled = bloodGroup.isNotBlank() && country.isNotBlank() && state.isNotBlank() && district.isNotBlank() && city.isNotBlank()

        Button(
            onClick = { 
                navController.navigate(Screen.SearchResults.createRoute(bloodGroup, country, state, district, city)) 
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            enabled = isSearchEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = RedPrimary,
                disabledContainerColor = Color(0xFFF59B99),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Search Donors", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDropdownField(
    label: String, 
    value: String, 
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                shape = RoundedCornerShape(12.dp),
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedBorderColor = Color(0xFFE5E7EB),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            if (options.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { 
                                Text(
                                    text = selectionOption,
                                    color = BlueDark,
                                    fontSize = 16.sp
                                ) 
                            },
                            onClick = {
                                onOptionSelected(selectionOption)
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultsScreen(navController: NavController, bloodGroup: String, country: String, state: String, district: String, city: String) {
    var donorsList by remember { mutableStateOf<List<Donor>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(bloodGroup, country, state, district, city) {
        try {
            val fetchedDonors = SupabaseClient.client.postgrest["profiles"]
                .select {
                    filter {
                        eq("is_donor", true)
                        eq("blood_group", bloodGroup)
                        if (state.isNotEmpty()) eq("state", state)
                        if (district.isNotEmpty()) eq("district", district)
                        if (city.isNotEmpty()) eq("city", city)
                    }
                }.decodeList<Donor>()
            
            val searchCityCoords = LocationData.resolveCityCoordinates(city.ifEmpty { district })

            donorsList = fetchedDonors
                .filter { it.hospitalization_status != "Yes" }
                .sortedWith(Comparator { d1, d2 ->
                    val c1 = LocationData.resolveCityCoordinates(d1.city ?: d1.district ?: "")
                    val c2 = LocationData.resolveCityCoordinates(d2.city ?: d2.district ?: "")
                    val dist1 = if (searchCityCoords != null && c1 != null) LocationData.calculateDistanceKm(searchCityCoords.first, searchCityCoords.second, c1.first, c1.second) else 999.0
                    val dist2 = if (searchCityCoords != null && c2 != null) LocationData.calculateDistanceKm(searchCityCoords.first, searchCityCoords.second, c2.first, c2.second) else 999.0
                    dist1.compareTo(dist2)
                })
        } catch (e: Exception) {
            Toast.makeText(context, "Error fetching donors: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                text = "Search Results",
                modifier = Modifier.padding(top = 24.dp),
                color = BlueDark,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Found ${donorsList.size} available donors for $bloodGroup",
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp),
                color = GrayText,
                fontSize = 16.sp
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = RedPrimary)
            }
        } else if (donorsList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No available donors found for $bloodGroup", color = GrayText)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(donorsList) { donor ->
                    DonorCard(donor = donor, onRequestClick = {
                        scope.launch {
                            try {
                                val currentUser = SupabaseClient.client.auth.currentUserOrNull()
                                val donorId = donor.id
                                if (currentUser != null && !donorId.isNullOrBlank()) {
                                    var name = currentUser.userMetadata?.get("full_name")?.jsonPrimitive?.content ?: ""
                                    var phone = currentUser.userMetadata?.get("mobile")?.jsonPrimitive?.content ?: currentUser.userMetadata?.get("mobile_number")?.jsonPrimitive?.content ?: ""

                                    if (name.isBlank() || phone.isBlank()) {
                                        val p = SupabaseClient.client.postgrest["profiles"]
                                            .select { filter { eq("id", currentUser.id) } }
                                            .decodeSingleOrNull<Donor>()
                                        if (p != null) {
                                            if (name.isBlank()) name = p.full_name
                                            if (phone.isBlank()) phone = p.mobile
                                        }
                                    }
                                    if (name.isBlank()) name = "Patient/Requester"
                                    if (phone.isBlank()) phone = "N/A"

                                    val newRequest = BloodRequest(
                                        donor_user_id = donorId,
                                        requester_name = name,
                                        requester_phone = phone,
                                        status = "Pending"
                                    )
                                    SupabaseClient.client.postgrest["blood_requests"].insert(newRequest)

                                    val newNotif = NotificationModel(
                                        user_id = donorId,
                                        title = "New Blood Request Received",
                                        message = "Urgent request from $name. Contact: $phone",
                                        type = "URGENT"
                                    )
                                    SupabaseClient.client.postgrest["notifications"].insert(newNotif)

                                    Toast.makeText(context, "Request sent to ${donor.full_name} successfully!", Toast.LENGTH_LONG).show()
                                    navController.navigateUp()
                                } else {
                                     Toast.makeText(context, "Cannot send request at this time.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error sending request: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }
        }
    }
}

@Composable
fun DonorCard(donor: Donor, onRequestClick: () -> Unit = {}) {
    val context = LocalContext.current
    val statusColor = if (donor.is_available) Color(0xFF2ECC71) else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Blood drop icon box
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(RedPrimary.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = null,
                        tint = RedPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = donor.full_name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val donorCoords = com.simats.vitalmatch.data.LocationData.resolveCityCoordinates(donor.city ?: donor.district)
                        if (donorCoords != null) {
                            val userCoords = com.simats.vitalmatch.data.LocationData.resolveCityCoordinates("Ongole")
                            if (userCoords != null) {
                                val km = com.simats.vitalmatch.data.LocationData.calculateDistanceKm(
                                    userCoords.first, userCoords.second,
                                    donorCoords.first, donorCoords.second
                                )
                                val formattedKm = String.format(java.util.Locale.US, "%.1f", km)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${formattedKm}km",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = donor.blood_group,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = RedPrimary
                    )
                }

                Surface(
                    modifier = Modifier.border(
                        width = 1.dp,
                        color = if (donor.is_available) Color(0xFF2ECC71) else RedPrimary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                    color = if (donor.is_available) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (donor.is_available) com.simats.vitalmatch.ui.theme.AppStrings.get("available") else com.simats.vitalmatch.ui.theme.AppStrings.get("unavailable"),
                        color = if (donor.is_available) Color(0xFF2E7D32) else RedPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Last donation: ${donor.last_donation_date ?: "N/A"}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { 
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${donor.mobile}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Call", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                OutlinedButton(
                    onClick = { 
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("smsto:${donor.mobile}")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Icon(Icons.Default.Email, contentDescription = "Message", tint = Color(0xFF0D1B2A))
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                OutlinedButton(
                    onClick = { /* Flag Action */ },
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Icon(Icons.Outlined.Flag, contentDescription = "Report", tint = Color(0xFF0D1B2A))
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onRequestClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D1B2A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Request Donor", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class Donor(
    val id: String, 
    val name: String, 
    val bloodType: String, 
    val lastDonation: String,
    val isEligible: Boolean
)
