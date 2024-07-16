package com.example.freshyzo.model

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class PaytmResponse(
    @SerializedName("head") val head: Head,
    @SerializedName("body") val body: Body,
    @SerializedName("signature") val CHECKSUMHASH: String?,
    @SerializedName("txnToken") val TXNTOKEN: String?,
    @SerializedName("authenticated") val AUTH: Boolean?,
    @SerializedName("resultInfo") val RESULT: Objects?
)

data class Head(
    @SerializedName("responseTimeStamp") val responseTimestamp: String,
    @SerializedName("version") val version: String,
    @SerializedName("signature") val signature: String
)

data class Body(
    @SerializedName("resultInfo") val resultInfo: ResultInfo,
    @SerializedName("txnToken") val txnToken: String,
    @SerializedName("isPromoCodeValid") val isPromoCodeValid: Boolean,
    @SerializedName("authenticated") val authenticated: Boolean
)

data class ResultInfo(
    @SerializedName("resultStatus") val resultStatus: String,
    @SerializedName("resultCode") val resultCode: String,
    @SerializedName("resultMsg") val resultMsg: String
)


