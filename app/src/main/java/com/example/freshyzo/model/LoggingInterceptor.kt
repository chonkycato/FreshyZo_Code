package com.example.freshyzo.model

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        // Log the raw response body
        val responseBody = response.body?.string()
        Log.d("RAW_RESPONSE", responseBody ?: "No response body")

        // Return a new response with the logged body
        return response.newBuilder()
            .body((responseBody ?: "").toResponseBody(response.body?.contentType()))
            .build()
    }
}

