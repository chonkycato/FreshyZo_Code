package com.example.freshyzo.model

import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @SerializedName("customer_image") val customerImage: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("contact_1") val contact: String,
    @SerializedName("address") val address: String,
    @SerializedName("colony_id") val colonyId: String,
    @SerializedName("colony_name") val colonyName: String
)
