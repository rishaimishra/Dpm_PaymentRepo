package com.dpm.payment.models.searchBusinessResponse


import com.google.gson.annotations.SerializedName

data class RegistrationDetails(
    @SerializedName("BusinessCategory")
    val businessCategory: String?,
    @SerializedName("BusinessType")
    val businessType: String?,
    @SerializedName("PaymentStatus")
    val paymentStatus: String?,
    @SerializedName("RegistrationFee")
    val registrationFee: Int?
)