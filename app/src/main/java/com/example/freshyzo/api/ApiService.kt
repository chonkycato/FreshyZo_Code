package com.example.freshyzo.api

import com.example.freshyzo.model.BannerResponse
import com.example.freshyzo.model.ColonyListResponse
import com.example.freshyzo.model.LoginRequest
import com.example.freshyzo.model.LoginResponse
import com.example.freshyzo.model.OneTimePasswordRequest
import com.example.freshyzo.model.SignUpRequest
import com.example.freshyzo.model.SignUpResponse
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

    /** Colony List **/
    @GET("admin/Customer_App_Api/get_all_colony")
    fun getColonies(): Call<ColonyListResponse>

    /** Carousel Banner **/
    @GET("admin/Customer_App_Api/banner_list")
    fun getBanners(): Call<BannerResponse>
}

//@POST("admin/paytm_appbase/transaction_request.php")
//fun initiateTransaction(@Body params: PaytmRequest): Call<PaytmResponse>
//
//@POST("admin/paytm_appbase/transaction_status.php")
//fun checkTransactionStatus(@Body params: Map<String, String>): Call<TransactionStatusResponse>
//

/** OTP Generation
//https://freshyzo.com/admin/Customer_App_Api/send_otp
//{
//    "mobile_no": "7047112327"
//    "method": "signup" //login for login
//} **/

/** Login Authentication
//for otp authentication-post method
//https://freshyzo.com/admin/Customer_App_Api/auth
//{
//    "mobile_no": "7047112327",
//    "otp":"237552"
//
//}
//server response-{"customer_id":"745","customer_role":"customers"} **/

/** for user registration
//method: "post",
// url: https://freshyzo.com/admin/Customer_App_Api/signup
//payload-first_name, last_name, mobile_no, colony, otp
//response-
//"Invalid or expired OTP."
//'failed'
//{
//    'ragistration_id' => $ragistration_id,
//    'customer_role' => 'ragister_customer',
//}; **/

/** Area api

//url: https://freshyzo.com/admin/Customer_App_Api/get_all_colony
//data: {
//    "colony": [
//        {
//            "colony_id": "2",
//            "colony_name": "Adarsh nagar"
//        }, ...
//      ]
//  } **/

/** Carousel banner API*
api link- https://freshyzo.com/admin/Customer_App_Api/banner_list
response - {"banner_list":
                [
                    {
                        "offer_banner_id":"1",
                        "img_name":"214071727963239318d32f4.jpeg"
                    }
                ]
            }
https://freshyzo.com/admin/uploads/offer_banner_image/214071727963239318d32f4.jpeg **/