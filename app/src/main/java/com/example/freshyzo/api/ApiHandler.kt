package com.example.freshyzo.api

import com.example.freshyzo.model.BannerResponse
import com.example.freshyzo.model.ColonyListResponse
import com.example.freshyzo.model.ColonyResponse
import com.example.freshyzo.model.LoginRequest
import com.example.freshyzo.model.LoginResponse
import com.example.freshyzo.model.OneTimePasswordRequest
import com.example.freshyzo.model.SignUpRequest
import com.example.freshyzo.model.SignUpResponse
import com.example.freshyzo.network.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiHandler {

    /** Callback interface to handle the results of the colony fetch operation **/
    interface ColonyListCallback {
        fun onSuccess(colonies: List<ColonyResponse>)
        fun onFailure(errorMessage: String)
    }

    interface BannerListCallback {
        fun onSuccess(images: List<String>)
        fun onFailure(errorMessage: String)
    }

    /** Request OTP **/
    fun requestOTP(phoneNumber: String, method: String, callback: Callback<ResponseBody>) {
        val request = OneTimePasswordRequest(mobile_no = phoneNumber, method = method)
        val apiService = RetrofitClient.apiService.requestOTP(request)
        apiService.enqueue(callback)
    }

    /** Verify Login **/
    fun verifyLogin(phoneNumber: String, otp: String, callback: Callback<LoginResponse>) {
        val request = LoginRequest(mobile_no = phoneNumber, otp = otp)
        val apiService = RetrofitClient.apiService.verifyLogin(request)
        apiService.enqueue(callback)
    }

    /** Register User **/
    fun registerUser(firstName: String, lastName: String, phoneNumber: String, colony: String, otp:String, callback: Callback<SignUpResponse>){
        val request = SignUpRequest(first_name = firstName, last_name = lastName,
            mobile_no = phoneNumber, colony = colony, otp = otp)
        val apiService = RetrofitClient.apiService.registerUser(request)
        apiService.enqueue(callback)
    }

    /** Fetch Colonies **/
    fun fetchColonies(callback: ColonyListCallback) {
        val apiService = RetrofitClient.apiService.getColonies()  // Assuming a Retrofit instance is set up
        apiService.enqueue(object : Callback<ColonyListResponse> {
            override fun onResponse(call: Call<ColonyListResponse>, response: Response<ColonyListResponse>) {
                if (response.isSuccessful) {
                    val colonies = response.body()?.colony ?: emptyList()  // Access the 'colony' list
                    callback.onSuccess(colonies)
                } else {
                    callback.onFailure("Failed to load areas: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ColonyListResponse>, t: Throwable) {
                callback.onFailure("Error: ${t.message}")
            }
        })
    }

    /** Fetch Carousel Images **/
    fun fetchBannerImages(callback: BannerListCallback) {

        val apiService = RetrofitClient.apiService.getBanners()
        apiService.enqueue(object : Callback<BannerResponse> {
            override fun onResponse(call: Call<BannerResponse>, response: Response<BannerResponse>) {
                if (response.isSuccessful) {
                    val bannerList = response.body()?.banner_list
                    val imageUrls = bannerList?.map { "https://freshyzo.com/admin/uploads/offer_banner_image/${it.img_name}" }
                    if (imageUrls != null) {
                        callback.onSuccess(imageUrls)
                    } else {
                        callback.onFailure("No images found")
                    }
                } else {
                    callback.onFailure("Failed to load banner images")
                }
            }

            override fun onFailure(call: Call<BannerResponse>, t: Throwable) {
                callback.onFailure(t.message ?: "An error occurred")
            }
        })
    }
}