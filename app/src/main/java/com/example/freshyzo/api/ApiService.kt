package com.example.freshyzo.api

import com.example.freshyzo.model.BannerResponse
import com.example.freshyzo.model.CategoryResponse
import com.example.freshyzo.model.ColonyListResponse
import com.example.freshyzo.model.CustomerRequest
import com.example.freshyzo.model.CustomerResponse
import com.example.freshyzo.model.LoginRequest
import com.example.freshyzo.model.LoginResponse
import com.example.freshyzo.model.OneTimePasswordRequest
import com.example.freshyzo.model.OrderResponse
import com.example.freshyzo.model.ProductRequest
import com.example.freshyzo.model.ProductResponse
import com.example.freshyzo.model.ProfileUpdateRequest
import com.example.freshyzo.model.RechargeHistoryResponse
import com.example.freshyzo.model.SignUpRequest
import com.example.freshyzo.model.SignUpResponse
import com.example.freshyzo.model.WalletResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type': 'application/x-www-form-urlencoded")

    /** OTP Generation **/
    @POST("admin/Customer_App_Api/send_otp")
    fun requestOTP(@Body params: OneTimePasswordRequest): Call<ResponseBody>

    /** Customer Verification **/
    @POST("admin/Customer_App_Api/auth")
    fun verifyLogin(@Body params: LoginRequest): Call<LoginResponse>

    /** User Registration **/
    @POST("admin/Customer_App_Api/signup")
    fun registerUser(@Body params: SignUpRequest): Call<SignUpResponse>

    /** Wallet Balance **/
    @POST("admin/Customer_App_Api/get_balance")
    fun getWalletBalance(@Body params: CustomerRequest): Call<List<WalletResponse>>

    /** Product List **/
    @POST("admin/Customer_App_Api/fetch_product")
    fun getProducts(@Body params: ProductRequest): Call<List<ProductResponse>>

    /** Categories List **/
    @GET("admin/Customer_App_Api/product_category_list")
    fun getCategories(): Call<List<CategoryResponse>>

    /** Colony List **/
    @GET("admin/Customer_App_Api/get_all_colony")
    fun getColonies(): Call<ColonyListResponse>

    /** Carousel Banner **/
    @GET("admin/Customer_App_Api/banner_list")
    fun getBanners(): Call<List<BannerResponse>>

    /** Customer Details **/
    @POST("admin/Customer_App_Api/profile_data") // Replace with the actual endpoint
    fun getCustomerDetails(@Body params: CustomerRequest): Call<List<CustomerResponse>>

    /** Update Customer Details **/
    @POST("admin/Customer_App_Api/update_profile")
    fun updateCustomerProfile(@Body params: ProfileUpdateRequest): Call<ResponseBody>

    /** Fetch Recharge History **/
    @POST("admin/Customer_App_Api/fetch_recharges")
    fun getRechargeHistory(@Body params: CustomerRequest): Call<List<RechargeHistoryResponse>>

    /** Fetch Order History **/
    @POST("admin/Customer_App_Api/fetch_orders")
    fun fetchOrders(@Body params: CustomerRequest): Call<List<OrderResponse>>

}


