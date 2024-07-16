package com.example.freshyzo.model
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PaytmApiService {
    @Headers("Content-Type: application/json")
    @POST("admin/paytm_appbase/transaction_request.php")
    fun initiateTransaction(@Body params: PaytmRequest): Call<PaytmResponse>

    @POST("admin/paytm_appbase/transaction_status.php")
    fun checkTransactionStatus(@Body params: Map<String, String>): Call<TransactionStatusResponse>

}
