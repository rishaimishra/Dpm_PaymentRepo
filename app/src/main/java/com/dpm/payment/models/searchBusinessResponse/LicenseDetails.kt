package com.dpm.payment.models.searchBusinessResponse


import com.google.gson.annotations.SerializedName

data class LicenseDetails(
    @SerializedName("BusinessCategoryID")
    val businessCategoryID: Int?,
    @SerializedName("BusinessCategoryeName")
    val businessCategoryeName: String?,
    @SerializedName("BusinessType")
    val businessType: String?,
    @SerializedName("LicenseFee")
    val licenseFee: Int?
)