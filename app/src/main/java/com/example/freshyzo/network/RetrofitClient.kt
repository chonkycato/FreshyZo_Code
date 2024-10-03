package com.example.freshyzo.network
import com.example.freshyzo.api.ApiService
import com.example.freshyzo.api.PaytmApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://freshyzo.com/"

/**    Pass gson inside addConverterFactory(GsonConverterFactory.create()) if lenient json is needed **/
//    val gson = GsonBuilder()
//        .setLenient()
//        .create()

    val client = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val paytmApiService: PaytmApiService by lazy {
        instance.create(PaytmApiService::class.java)
    }

    val apiService: ApiService by lazy{
        instance.create(ApiService::class.java)
    }
}
