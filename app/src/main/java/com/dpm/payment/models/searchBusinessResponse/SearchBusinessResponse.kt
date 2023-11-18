package com.dpm.payment.models.searchBusinessResponse


import com.google.gson.annotations.SerializedName

data class SearchBusinessResponse(
    @SerializedName("business_id")
    val businessId: Int?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("license_details")
    val licenseDetails: LicenseDetails?,
    @SerializedName("registration_details")
    val registrationDetails: RegistrationDetails?,
    @SerializedName("success")
    val success: Boolean?
)