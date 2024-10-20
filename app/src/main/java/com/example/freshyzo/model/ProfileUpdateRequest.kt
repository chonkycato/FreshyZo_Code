package com.example.freshyzo.model

import com.google.gson.annotations.SerializedName

data class ProfileUpdateRequest(
    @SerializedName("customer_id") val customerID: String,
    @SerializedName("customer_role") val customerRole: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("number") val phoneNumber: String,
    @SerializedName("address") val address: String,
    @SerializedName("colony_id") val colonyID: String
)
