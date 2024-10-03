package com.example.freshyzo.model

data class OneTimePasswordRequest(
    val mobile_no: String,
    val method: String
)
