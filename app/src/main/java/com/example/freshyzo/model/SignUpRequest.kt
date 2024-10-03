package com.example.freshyzo.model

data class SignUpRequest(
    val first_name: String,
    val last_name: String,
    val mobile_no: String,
    val colony: String,
    val otp: String
)
