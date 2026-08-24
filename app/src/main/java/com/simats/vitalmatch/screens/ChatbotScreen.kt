package com.simats.vitalmatch.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.vitalmatch.Screen
import com.simats.vitalmatch.data.models.Donor
import com.simats.vitalmatch.data.remote.SupabaseClient
import com.simats.vitalmatch.ui.theme.*
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import java.util.Locale

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: String, // "user" or "bot"
    val text: String,
    val donors: List<Donor>? = null,
    val actionRoute: String? = null,
    val actionLabel: String? = null,
    val timestamp: String = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault()).format(java.util.Date())
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatbotScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    sender = "bot",
                    text = "Hello! 👋 I am VitalMatch AI Assistant.\nI can help you view all blood donors, search by location/blood group, post emergency requests, or check donation eligibility.\n\nTry asking:\n• 'Show All Donors'\n• 'Find Donors in Prakasam'\n• 'Search A+ Blood Donors'\n• 'How to post emergency?'"
                )
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("VitalMatch AI Assistant", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("NLP Donor & App Assistant", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = RedPrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF8F9FA))
        ) {
            // Suggested Quick Chips (Horizontally Scrollable LazyRow)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                val suggestions = listOf(
                    "Show All Donors",
                    "Donors in Prakasam",
                    "A+ Blood Donors",
                    "Post Emergency",
                    "Eligibility Criteria"
                )
                items(suggestions) { suggestion ->
                    SuggestionChip(
                        onClick = {
                            inputText = suggestion
                        },
                        label = {
                            Text(
                                text = suggestion,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = RedPrimary.copy(alpha = 0.08f),
                            labelColor = RedPrimary
                        ),
                        border = SuggestionChipDefaults.suggestionChipBorder(
                            enabled = true,
                            borderColor = RedPrimary.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            HorizontalDivider(thickness = 1.dp, color = Color(0xFFEEEEEE))

            // Chat Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(messages) { msg ->
                    ChatMessageBubble(msg = msg, navController = navController, context = context)
                }

                if (isThinking) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = RedPrimary, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Fetching donor records...", fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // Input Bar
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Ask for donors, area, blood group...", fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RedPrimary,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isThinking) {
                                val userQuery = inputText.trim()
                                inputText = ""
                                messages = messages + ChatMessage(sender = "user", text = userQuery)
                                isThinking = true

                                scope.launch {
                                    val botResponse = processNlpQuery(userQuery)
                                    isThinking = false
                                    messages = messages + botResponse
                                }
                            }
                        },
                        containerColor = RedPrimary,
                        contentColor = Color.White,
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatMessageBubble(msg: ChatMessage, navController: NavController, context: android.content.Context) {
    val isUser = msg.sender == "user"
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) RedPrimary else Color.White,
            shadowElevation = if (isUser) 1.dp else 2.dp,
            modifier = Modifier.widthIn(max = 330.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = msg.text,
                    color = if (isUser) Color.White else Color(0xFF1E293B),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                // Action button if available
                if (msg.actionRoute != null && msg.actionLabel != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { navController.navigate(msg.actionRoute) },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isUser) Color.White else RedPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(msg.actionLabel, color = if (isUser) RedPrimary else Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Render Donor Cards
                if (!msg.donors.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    msg.donors.forEach { donor ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        donor.full_name.ifEmpty { "Blood Donor" },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                    Surface(color = RedPrimary, shape = RoundedCornerShape(4.dp)) {
                                        Text(
                                            donor.blood_group.ifEmpty { "N/A" },
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                val loc = listOfNotNull(donor.city, donor.district, donor.state)
                                    .filter { it.isNotBlank() }
                                    .joinToString(", ")
                                Text(
                                    text = if (loc.isNotBlank()) "📍 Location: $loc" else "📍 Location: Not specified",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )

                                Surface(
                                    color = if (donor.is_available) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = if (donor.is_available) "Status: Available" else "Status: Unavailable",
                                        color = if (donor.is_available) Color(0xFF2E7D32) else Color(0xFFC62828),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                if (donor.mobile.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier
                                            .clickable {
                                                try {
                                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${donor.mobile}"))
                                                    context.startActivity(intent)
                                                } catch (e: Exception) { }
                                            }
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = null, tint = RedPrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Call Phone: ${donor.mobile}", color = RedPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = msg.timestamp,
                    color = if (isUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

// Full Database Dynamic NLP Query Engine
suspend fun processNlpQuery(query: String): ChatMessage {
    val q = query.lowercase(Locale.getDefault()).trim()

    // 1. Extract Blood Group Entity
    val bloodRegex = Regex("""\b(a\+|a-|b\+|b-|o\+|o-|ab\+|ab-)\b""", RegexOption.IGNORE_CASE)
    val extractedBlood = bloodRegex.find(q)?.value?.uppercase(Locale.getDefault())

    // 2. Extract Location Entity dynamically
    val stopWords = setOf(
        "show", "find", "search", "list", "donors", "donor", "in", "for", "blood", "give",
        "all", "the", "me", "data", "please", "are", "available", "there", "any", "get",
        "database", "details", "info", "information", "registered", "need", "want", "of", "display"
    )

    var locationQuery = q
    if (extractedBlood != null) {
        locationQuery = locationQuery.replace(extractedBlood.lowercase(Locale.getDefault()), "")
    }

    val cleanLocationTokens = locationQuery.split(Regex("""\s+""")).filter { token ->
        token.isNotBlank() && !stopWords.contains(token) && token.length > 2
    }
    val extractedLocation = cleanLocationTokens.joinToString(" ").takeIf { it.isNotBlank() }

    // 3. Query Supabase Database Profiles
    try {
        val allDonors = SupabaseClient.client.postgrest["profiles"]
            .select()
            .decodeList<Donor>()
            .filter { it.is_donor }

        val isAllDataQuery = q.contains("all") || q.contains("database") || q.contains("every") ||
                (extractedBlood == null && extractedLocation == null && (q.contains("donor") || q.contains("list") || q.contains("data")))

        val filtered = if (isAllDataQuery && extractedBlood == null && extractedLocation == null) {
            allDonors
        } else {
            allDonors.filter { d ->
                val matchBlood = extractedBlood == null || d.blood_group.equals(extractedBlood, ignoreCase = true)
                val matchLoc = extractedLocation == null ||
                        (d.district != null && d.district.contains(extractedLocation, ignoreCase = true)) ||
                        (d.city != null && d.city.contains(extractedLocation, ignoreCase = true)) ||
                        (d.state != null && d.state.contains(extractedLocation, ignoreCase = true)) ||
                        (d.full_name.contains(extractedLocation, ignoreCase = true))
                matchBlood && matchLoc
            }
        }

        if (filtered.isNotEmpty()) {
            val titleText = when {
                isAllDataQuery && extractedBlood == null && extractedLocation == null ->
                    "Displaying all ${filtered.size} registered donor(s) from database:"
                extractedBlood != null && extractedLocation != null ->
                    "Found ${filtered.size} $extractedBlood donor(s) in $extractedLocation:"
                extractedBlood != null ->
                    "Found ${filtered.size} $extractedBlood donor(s) in database:"
                extractedLocation != null ->
                    "Found ${filtered.size} donor(s) in $extractedLocation:"
                else ->
                    "Found ${filtered.size} donor(s) in database:"
            }
            return ChatMessage(
                sender = "bot",
                text = titleText,
                donors = filtered
            )
        } else {
            val locStr = extractedLocation ?: "that area"
            val bloodStr = extractedBlood ?: "that group"
            return ChatMessage(
                sender = "bot",
                text = "No registered $bloodStr donors found matching '$locStr' in the database. You can post an emergency request to alert nearby donors immediately!",
                actionRoute = Screen.PostEmergency.route,
                actionLabel = "Post Emergency Request"
            )
        }
    } catch (e: Exception) {
        if (q.contains("emergency") || q.contains("urgent") || q.contains("post")) {
            return ChatMessage(
                sender = "bot",
                text = "To post an urgent blood emergency:\n1. Tap 'Post Emergency'\n2. Fill hospital details & contact\n3. Donors in your district will be notified immediately!",
                actionRoute = Screen.PostEmergency.route,
                actionLabel = "Post Emergency Now"
            )
        }

        if (q.contains("eligible") || q.contains("rules") || q.contains("criteria") || q.contains("days")) {
            return ChatMessage(
                sender = "bot",
                text = "🩸 Blood Donation Eligibility Criteria:\n• Age: 18 – 65 years\n• Weight: Minimum 45 kg\n• Frequency: Must wait 90 days between donations\n• Hemoglobin: At least 12.5 g/dL\n• Good general health condition."
            )
        }

        return ChatMessage(
            sender = "bot",
            text = "Try asking: 'Show All Donors', 'Find Donors in Prakasam', or 'How to post emergency?'"
        )
    }
}
