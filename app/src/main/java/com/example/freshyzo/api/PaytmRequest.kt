package com.example.freshyzo.api

data class PaytmRequest(
    val MID: String,
    val ORDER_ID: String,
    val CUST_ID: String,
    val TXN_AMOUNT: String,
    val CHANNEL_ID: String,
    val WEBSITE: String,
    val CALLBACK_URL: String,
    val INDUSTRY_TYPE_ID: String
)
