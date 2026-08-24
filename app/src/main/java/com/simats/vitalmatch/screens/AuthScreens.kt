package com.simats.vitalmatch.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.simats.vitalmatch.R
import com.simats.vitalmatch.Screen
import com.simats.vitalmatch.data.remote.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import com.simats.vitalmatch.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate(Screen.AuthSelection.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "VitalMatch Logo",
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "Connecting Lives Through Blood",
            modifier = Modifier.padding(top = 8.dp),
            color = GrayText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(48.dp))
        LinearProgressIndicator(
            modifier = Modifier.width(100.dp),
            color = RedPrimary,
            trackColor = RedPrimary.copy(alpha = 0.2f)
        )
    }
}

@Composable
fun AuthSelectionScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "VitalMatch Logo",
            modifier = Modifier.size(120.dp)
        )
        Text(
            text = "Welcome to VitalMatch",
            modifier = Modifier.padding(top = 32.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Connecting Lives Through Blood",
            modifier = Modifier.padding(top = 8.dp),
            color = GrayText,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = { navController.navigate(Screen.SignIn.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Sign In", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = { navController.navigate(Screen.SignUp.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, RedPrimary)
        ) {
            Text("Sign Up", fontSize = 18.sp, color = RedPrimary)
        }
    }
}

@Composable
fun SignUpScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isEmailOtp by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BlueDark)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back", color = BlueDark, fontSize = 16.sp)
        }

        Text(
            text = "Create Account",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Sign up to get started",
            modifier = Modifier.padding(top = 4.dp),
            color = GrayText,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text("Full Name", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = { Text("Enter your full name", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Email Address", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email address", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Mobile Number", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { mobileNumber = it },
            placeholder = { Text("Enter your mobile number", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Set Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("......", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = null, tint = GrayText)
                }
            },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text("Verify via:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = !isEmailOtp, onClick = { isEmailOtp = false })
            Text("Mobile", color = BlueDark)
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = isEmailOtp, onClick = { isEmailOtp = true })
            Text("Email", color = BlueDark)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                val currentEmail = email
                val currentPassword = password
                scope.launch {
                    try {
                        isLoading = true
                        val currentEmail = email
                        val currentPassword = password
                        SupabaseClient.client.auth.signUpWith(Email) {
                            this.email = currentEmail
                            this.password = currentPassword
                            data = buildJsonObject {
                                put("full_name", fullName)
                                put("mobile_number", mobileNumber)
                            }
                        }
                        isLoading = false
                        Toast.makeText(context, "Registration Successful! Please sign in.", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.SignIn.route)
                    } catch (e: Exception) {
                        isLoading = false
                        Toast.makeText(context, e.message ?: "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Send OTP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Already have an account? Sign In",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .clickable { navController.navigate(Screen.SignIn.route) },
            textAlign = TextAlign.Center,
            color = RedPrimary,
            fontSize = 16.sp
        )
    }
}

@Composable
fun OtpInputField(
    otpText: String,
    onOtpTextChange: (String) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        BasicTextField(
            value = otpText,
            onValueChange = onOtpTextChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.matchParentSize().alpha(0f)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(6) { index ->
                val char = when {
                    index < otpText.length -> otpText[index].toString()
                    else -> ""
                }
                Box(
                    modifier = Modifier
                        .size(width = 50.dp, height = 60.dp)
                        .background(Color(0xFFF0F2F5), RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = char,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueDark
                    )
                }
            }
        }
    }
}

@Composable
fun OtpVerificationScreen(
    navController: NavController,
    mobileNumber: String
){
    var otp by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

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
            text = "Verify Mobile",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter OTP sent to your mobile number",
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
            onClick = {
                // Verification logic would go here
                Toast.makeText(context, "Verifying...", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.SignUpSuccess.route)
            },
            enabled = otp.length == 6 && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF34C759),          // Normal green
                disabledContainerColor = Color(0xFFA5D6A7),  // Light green
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Verify",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmailOtpVerificationScreen(navController: NavController) {
    var otp by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

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
            text = "Verify Email",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter OTP sent to your email address",
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
            onClick = {
                if (otp.length < 6) {
                    Toast.makeText(context, "Enter 6-digit OTP", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                navController.navigate(Screen.SignUpSuccess.route)
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA5D6A7)),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Verify", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SuccessScreenContent(navController: NavController, title: String, message: String, targetRoute: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = GreenSuccess,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = GreenSuccess)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, textAlign = TextAlign.Center, color = GrayText)
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = { navController.navigate(targetRoute) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GreenSuccess),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun SignUpSuccessScreen(navController: NavController) {
    SuccessScreenContent(
        navController = navController,
        title = "Account Created!",
        message = "Your account has been successfully created. Please sign in to continue.",
        targetRoute = Screen.SignIn.route
    )
}

@Composable
fun SignInScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BlueDark)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back", color = BlueDark, fontSize = 16.sp)
        }

        Text(
            text = "Sign In",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Welcome back to VitalMatch",
            modifier = Modifier.padding(top = 4.dp),
            color = GrayText,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text("Email", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Enter your password", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = null, tint = GrayText)
                }
            },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Forgot Password?",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screen.ForgotPasswordMobile.route) },
            textAlign = TextAlign.End,
            color = RedPrimary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, "Please enter credentials", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val currentEmail = email
                val currentPassword = password
                isLoading = true
                scope.launch {
                    try {
                        SupabaseClient.client.auth.signInWith(Email) {
                            this.email = currentEmail
                            this.password = currentPassword
                        }
                        
                        val user = SupabaseClient.client.auth.currentUserOrNull()
                        val fullNameFromResponse = user?.userMetadata?.get("full_name")?.toString()?.replace("\"", "") ?: "User"

                        val sharedPreferences =
                            context.getSharedPreferences(
                                "vitalmatch",
                                Context.MODE_PRIVATE
                            )

                        sharedPreferences.edit()
                            .putString("user_name", fullNameFromResponse)
                            .putString("email", currentEmail)
                            .apply()

                        Toast.makeText(
                            context,
                            "Login Successful",
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.navigate(
                            Screen.SignInSuccess.route
                        )
                    } catch (e: Exception) {
                        val rawMsg = e.message ?: ""
                        val userMsg = when {
                            rawMsg.contains("Invalid login credentials", ignoreCase = true) -> "Invalid Email or Password. Please check your details."
                            rawMsg.contains("Email not confirmed", ignoreCase = true) -> "Email not confirmed. Please check your inbox."
                            rawMsg.contains("User not found", ignoreCase = true) -> "Account not found. Please Sign Up first."
                            rawMsg.contains("Unable to resolve host", ignoreCase = true) || rawMsg.contains("ConnectException", ignoreCase = true) -> "Network error. Please check internet connection."
                            else -> rawMsg.ifBlank { "Sign in failed. Please check your credentials." }
                        }
                        Toast.makeText(context, userMsg, Toast.LENGTH_LONG).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            else Text("Sign In", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Don't have an account? Sign Up",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screen.SignUp.route) },
            textAlign = TextAlign.Center,
            color = RedPrimary,
            fontSize = 16.sp
        )
    }
}


@Composable
fun ForgotPasswordMobileScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

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
            text = "Forgot Password",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Enter your email to reset password",
            modifier = Modifier.padding(top = 4.dp),
            color = GrayText,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text("Email", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter your email", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = {
                if (email.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                isLoading = true
                val currentEmail = email
                scope.launch {
                    try {
                        SupabaseClient.client.auth.resetPasswordForEmail(currentEmail)
                        
                        Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = RedPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Send OTP",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ForgotPasswordOtpScreen(
    navController: NavController,
    mobileNumber: String
) {
    var otp by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

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
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = BlueDark
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back", color = BlueDark, fontSize = 16.sp)
        }

        Text(
            text = "Verify OTP",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Enter OTP sent to your mobile number",
            modifier = Modifier.padding(top = 4.dp),
            color = GrayText,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OtpInputField(
            otpText = otp,
            onOtpTextChange = {
                if (it.length <= 6) otp = it
            }
        )

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
            onClick = {
                if (isLoading) return@Button
                isLoading = true
                scope.launch {
                    navController.navigate(
                        Screen.ResetPassword.createRoute(mobileNumber)
                    )
                    isLoading = false
                }
            },
            enabled = otp.length == 6 && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF34C759),
                disabledContainerColor = Color(0xFFA5D6A7),
                contentColor = Color.White,
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = "Verify",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ResetPasswordScreen(
    navController: NavController,
    mobileNumber: String
){
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BlueDark)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back", color = BlueDark, fontSize = 16.sp)
        }

        Text(
            text = "Reset Password",
            modifier = Modifier.padding(top = 24.dp),
            color = BlueDark,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Create a new password",
            modifier = Modifier.padding(top = 4.dp),
            color = GrayText,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text("Set Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Enter new password", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = null, tint = GrayText)
                }
            },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text("Retype Password", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueDark)
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("Confirm new password", color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(image, contentDescription = null, tint = GrayText)
                }
            },
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(color = BlueDark, fontSize = 16.sp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF9FAFB),
                focusedContainerColor = Color(0xFFF9FAFB),
                unfocusedBorderColor = Color(0xFFE5E7EB),
                focusedTextColor = BlueDark,
                unfocusedTextColor = BlueDark
            )
        )

        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = {
                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Please enter both passwords",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (password != confirmPassword) {
                    Toast.makeText(
                        context,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                isLoading = true

                scope.launch {
                    try {
                        // Success navigation
                        navController.navigate(Screen.ResetPasswordSuccess.route)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = RedPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text(
                    "Save Password",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ResetPasswordSuccessScreen(navController: NavController) {
    SuccessScreenContent(
        navController = navController,
        title = "Password Created Successfully!",
        message = "Your password has been successfully reset. Please sign in with your new password.",
        targetRoute = Screen.SignIn.route
    )
}

@Composable
fun SignInSuccessScreen(navController: NavController) {
    SuccessScreenContent(
        navController = navController,
        title = "Login Successful!",
        message = "Welcome back to VitalMatch.",
        targetRoute = Screen.LocationPermission.route
    )
}
