package com.simats.vitalmatch_web.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    @SerialName("id") val id: String,
    @SerialName("full_name") val fullName: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("mobile") val mobile: String = "",
    @SerialName("blood_group") val bloodGroup: String = "",
    @SerialName("state") val state: String = "",
    @SerialName("district") val district: String = "",
    @SerialName("city") val city: String = "",
    @SerialName("is_donor") val isDonor: Boolean = false,
    @SerialName("is_available") val isAvailable: Boolean = true,
    @SerialName("last_donation_date") val lastDonationDate: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)
