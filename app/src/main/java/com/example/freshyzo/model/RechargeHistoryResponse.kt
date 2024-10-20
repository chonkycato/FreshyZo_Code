package com.example.freshyzo.model

import com.google.gson.annotations.SerializedName

data class RechargeHistoryResponse(
    @SerializedName("recharge_id") val rechargeID: String,
    @SerializedName("recharge_amount") val rechargeAmount: String,
    @SerializedName("recharge_date") val rechargeDate: String
)