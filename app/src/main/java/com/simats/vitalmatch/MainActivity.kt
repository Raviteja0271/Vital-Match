package com.simats.vitalmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simats.vitalmatch.screens.*
import com.simats.vitalmatch.ui.theme.VITALMATCHTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("vitalmatch_settings", MODE_PRIVATE)
        com.simats.vitalmatch.ui.theme.ThemeState.themeMode.value = prefs.getString("theme", "Light") ?: "Light"
        com.simats.vitalmatch.ui.theme.ThemeState.languageMode.value = prefs.getString("language", "English") ?: "English"

        requestNotificationPermission()
        enableEdgeToEdge()
        setContent {
            VITALMATCHTheme {
                VitalMatchApp()
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }
}

@Composable
fun VitalMatchApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.AuthSelection.route) { AuthSelectionScreen(navController) }
        composable(Screen.SignUp.route) { SignUpScreen(navController) }
        composable(Screen.OtpVerification.route) { backStackEntry ->

            val mobileNumber =
                backStackEntry.arguments?.getString("mobileNumber") ?: ""

            OtpVerificationScreen(
                navController,
                mobileNumber
            )
        }
        composable(Screen.EmailOtpVerification.route) { EmailOtpVerificationScreen(navController) }
        composable(Screen.SignUpSuccess.route) { SignUpSuccessScreen(navController) }
        composable(Screen.SignIn.route) { SignInScreen(navController) }
        composable(Screen.SignInSuccess.route) { SignInSuccessScreen(navController) }
        composable(Screen.ForgotPasswordMobile.route) { ForgotPasswordMobileScreen(navController) }
        composable(Screen.ForgotPasswordOtp.route) { backStackEntry ->
            val mobileNumber =
                backStackEntry.arguments?.getString("mobileNumber") ?: ""

            ForgotPasswordOtpScreen(
                navController,
                mobileNumber
            )
        }

        composable(Screen.ResetPassword.route) { backStackEntry ->
            val mobileNumber =
                backStackEntry.arguments?.getString("mobileNumber") ?: ""

            ResetPasswordScreen(
                navController,
                mobileNumber
            )
        }
        composable(Screen.ResetPasswordSuccess.route) { ResetPasswordSuccessScreen(navController) }
        composable(Screen.LocationPermission.route) { LocationPermissionScreen(navController) }
        composable(Screen.NotificationPermission.route) { NotificationPermissionScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Search.route) { SearchScreen(navController) }
        composable(Screen.SearchResults.route) { backStackEntry -> 
            val bloodGroup = backStackEntry.arguments?.getString("bloodGroup") ?: "O+"
            val country = backStackEntry.arguments?.getString("country") ?: ""
            val state = backStackEntry.arguments?.getString("state") ?: ""
            val district = backStackEntry.arguments?.getString("district") ?: ""
            val city = backStackEntry.arguments?.getString("city") ?: ""
            SearchResultsScreen(navController, bloodGroup, country, state, district, city) 
        }
        composable(Screen.PostEmergency.route) { PostEmergencyScreen(navController) }
        composable(Screen.PostEmergencyOtp.route) { PostEmergencyOtpScreen(navController) }
        composable(Screen.EmergencyFeed.route) { EmergencyFeedScreen(navController) }
        composable(
            route = Screen.EmergencyDetail.route,
            arguments = listOf(
                navArgument("id") { defaultValue = "" },
                navArgument("bloodGroup") { defaultValue = "" },
                navArgument("hospital") { defaultValue = "" },
                navArgument("contact") { defaultValue = "" },
                navArgument("location") { defaultValue = "" },
                navArgument("notes") { defaultValue = "" },
                navArgument("status") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            val bloodGroup = backStackEntry.arguments?.getString("bloodGroup") ?: "O+"
            val hospital = backStackEntry.arguments?.getString("hospital") ?: ""
            val contact = backStackEntry.arguments?.getString("contact") ?: ""
            val location = backStackEntry.arguments?.getString("location") ?: ""
            val notes = backStackEntry.arguments?.getString("notes") ?: ""
            val status = backStackEntry.arguments?.getString("status") ?: "Active"

            EmergencyDetailScreen(
                navController = navController,
                emergencyId = id,
                bloodGroupArg = bloodGroup,
                hospitalArg = hospital,
                contactArg = contact,
                locationArg = location,
                notesArg = notes,
                statusArg = status
            )
        }
        composable(Screen.DonorRegistration.route) { DonorRegistrationScreen(navController) }
        composable(Screen.DonorDashboard.route) { DonorDashboardScreen(navController) }
        composable(Screen.Notifications.route) { NotificationsScreen(navController) }
        composable(Screen.MyRequests.route) { MyRequestsScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(Screen.Chatbot.route) { com.simats.vitalmatch.screens.ChatbotScreen(navController) }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object AuthSelection : Screen("auth_selection")
    object SignUp : Screen("sign_up")
    object OtpVerification : Screen("otp_verification/{mobileNumber}") {

        fun createRoute(mobileNumber: String): String {
            return "otp_verification/$mobileNumber"
        }
    }
    object EmailOtpVerification : Screen("email_otp_verification")
    object SignUpSuccess : Screen("sign_up_success")
    object SignIn : Screen("sign_in")
    object SignInSuccess : Screen("sign_in_success")
    object ForgotPasswordMobile : Screen("forgot_password_mobile")
    object ForgotPasswordOtp : Screen("forgot_password_otp/{mobileNumber}") {
        fun createRoute(mobileNumber: String): String {
            return "forgot_password_otp/$mobileNumber"
        }
    }
    object ResetPassword : Screen("reset_password/{mobileNumber}") {
        fun createRoute(mobileNumber: String): String {
            return "reset_password/$mobileNumber"
        }
    }
    object ResetPasswordSuccess : Screen("reset_password_success")
    object LocationPermission : Screen("location_permission")
    object NotificationPermission : Screen("notification_permission")
    object Home : Screen("home")
    object Search : Screen("search")
    object SearchResults : Screen("search_results/{bloodGroup}?country={country}&state={state}&district={district}&city={city}") {
        fun createRoute(bloodGroup: String, country: String, state: String, district: String, city: String): String {
            return "search_results/${android.net.Uri.encode(bloodGroup)}?country=${android.net.Uri.encode(country)}&state=${android.net.Uri.encode(state)}&district=${android.net.Uri.encode(district)}&city=${android.net.Uri.encode(city)}"
        }
    }
    object PostEmergency : Screen("post_emergency")
    object PostEmergencyOtp : Screen("post_emergency_otp")
    object EmergencyFeed : Screen("emergency_feed")
    object EmergencyDetail : Screen("emergency_detail?id={id}&bloodGroup={bloodGroup}&hospital={hospital}&contact={contact}&location={location}&notes={notes}&status={status}") {
        fun createRoute(
            id: String = "",
            bloodGroup: String = "",
            hospital: String = "",
            contact: String = "",
            location: String = "",
            notes: String = "",
            status: String = ""
        ): String {
            return "emergency_detail?id=${android.net.Uri.encode(id)}&bloodGroup=${android.net.Uri.encode(bloodGroup)}&hospital=${android.net.Uri.encode(hospital)}&contact=${android.net.Uri.encode(contact)}&location=${android.net.Uri.encode(location)}&notes=${android.net.Uri.encode(notes)}&status=${android.net.Uri.encode(status)}"
        }
    }
    object DonorRegistration : Screen("donor_registration")
    object DonorDashboard : Screen("donor_dashboard")
    object Notifications : Screen("notifications")
    object MyRequests : Screen("my_requests")
    object Settings : Screen("settings")
    object Chatbot : Screen("chatbot")
}
