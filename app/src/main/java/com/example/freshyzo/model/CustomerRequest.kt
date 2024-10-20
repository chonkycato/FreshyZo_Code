package com.example.freshyzo.model

import com.google.gson.annotations.SerializedName

data class CustomerRequest(
    @SerializedName("customer_id") val customerID: String,
    @SerializedName("customer_role") val customerRole: String
)
