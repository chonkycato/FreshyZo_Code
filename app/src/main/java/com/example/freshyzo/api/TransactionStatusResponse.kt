package com.example.freshyzo.api

data class TransactionStatusResponse(
    val ORDERID: String,
    val STATUS: String,
    val TXNAMOUNT: String,
    // Add other fields as per the API response
)

