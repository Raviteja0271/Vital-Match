package com.simats.vitalmatch.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Donor(
    val id: String? = null,
    val full_name: String = "",
    val email: String? = null,
    val mobile: String = "",
    val blood_group: String = "",
    val state: String? = null,
    val district: String? = null,
    val city: String? = null,
    val is_donor: Boolean = false,
    val is_available: Boolean = true,
    val hospitalization_status: String? = "No",
    val last_donation_date: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val fcm_token: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class Emergency(
    val id: String? = null,
    val user_id: String? = null,
    val patient_name: String = "",
    val blood_group: String = "",
    val hospital_name: String = "",
    val contact_number: String = "",
    val location: String? = null,
    val notes: String? = null,
    val priority: String? = "High",
    val status: String = "Active",
    val created_at: String? = null,
    val district: String? = null,
    val state: String? = null,
    val city: String? = null
)

@Serializable
data class BloodRequest(
    val id: String? = null,
    val donor_user_id: String = "",
    val requester_name: String = "",
    val requester_phone: String = "",
    val status: String = "Pending",
    val created_at: String? = null,
    val donor_id: String? = null
)

@Serializable
data class NotificationModel(
    val id: String? = null,
    val user_id: String? = null,
    val title: String = "",
    val message: String = "",
    val time: String? = null,
    val type: String? = "info",
    val is_read: Boolean = false,
    val created_at: String? = null
)
