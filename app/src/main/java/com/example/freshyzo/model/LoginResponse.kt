package com.example.freshyzo.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName ("customer_id") val customerID: String,
    @SerializedName ("customer_role") val customerRole: String
)
