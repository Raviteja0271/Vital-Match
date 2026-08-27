package com.simats.vitalmatch_web.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmergencyRequest(
    @SerialName("id") val id: String? = null,
    @SerialName("user_id") val userId: String,
    @SerialName("patient_name") val patientName: String,
    @SerialName("blood_group") val bloodGroup: String,
    @SerialName("hospital_name") val hospitalName: String,
    @SerialName("contact_number") val contactNumber: String,
    @SerialName("location") val location: String,
    @SerialName("notes") val notes: String = "",
    @SerialName("priority") val priority: String = "High",
    @SerialName("status") val status: String = "Active",
    @SerialName("created_at") val createdAt: String? = null
)
