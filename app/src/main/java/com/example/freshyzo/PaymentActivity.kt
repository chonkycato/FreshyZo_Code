package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freshyzo.model.PaytmRequest
import com.example.freshyzo.model.PaytmResponse
import com.example.freshyzo.model.RetrofitClient
import com.example.freshyzo.model.TransactionStatusResponse
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import com.paytm.pgsdk.TransactionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class PaymentActivity : AppCompatActivity() {

    /** Staging credentials
     * SWKBaq01791611191993
     * mIJpLPNTd7UyI0wL
    **/

    private val ActivityRequestCode = 2  // Same request code used in onActivityResult 1001 earlier
    private lateinit var orderId: String
    private lateinit var amount: String
    private lateinit var callbackURL: String
    private lateinit var mID: String
    private lateinit var host : String
    private var randomNumber : Int = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var statusTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        /** Initialize progress bar **/
        progressBar = findViewById(R.id.progressBar)
        progressBar.isIndeterminate

//        /** Initialise status TV **/
        statusTV = findViewById(R.id.processingTV)
        statusTV.text = getString(R.string.processing)

        mID = "qzWFll53703123606941"
//        mID = "SWKBaq01791611191993"
        amount = "1.00"
        randomNumber = Random.nextInt(111111, 999999)
        orderId = "ORDER$randomNumber"
        Log.e("ORDER ID:", "$orderId")
        host = "https://securegw.paytm.in/"
//        host = "https://securegw-stage.paytm.in/"
        callbackURL = host + "theia/paytmCallback?ORDER_ID=$orderId"

        /** Handle Button Click **/
//        paymentButton = findViewById(R.id.payment_button)
//        paymentButton.setOnClickListener{
            prepareParams(orderId, amount, callbackURL)
//        }

    }

    private fun prepareParams(orderId: String, amount: String, callbackURL: String) {
        val params = HashMap<String, String>()
        params["MID"] = "qzWFll53703123606941"
//        params["MID"] = "SWKBaq01791611191993"
        params["ORDER_ID"] = orderId
        params["CUST_ID"] = "CUSTOMER_ID"
        params["TXN_AMOUNT"] = amount
        params["CHANNEL_ID"] = "WAP"
        params["CALLBACK_URL"] = callbackURL
        params["WEBSITE"] = "DEFAULT"
        params["INDUSTRY_TYPE_ID"] = "Retail"
        initiatePaytmCall(params)
    }

    private fun initiatePaytmCall(params: Map<String, String>) {
        val request = PaytmRequest(
            MID = params["MID"]!!,
            ORDER_ID = params["ORDER_ID"]!!,
            CUST_ID = params["CUST_ID"]!!,
            TXN_AMOUNT = params["TXN_AMOUNT"]!!,
            CHANNEL_ID = params["CHANNEL_ID"]!!,
            WEBSITE = params["WEBSITE"]!!,
            CALLBACK_URL = params["CALLBACK_URL"]!!,
            INDUSTRY_TYPE_ID = params["INDUSTRY_TYPE_ID"]!!
        )

        val call = RetrofitClient.apiService.initiateTransaction(request)
        call.enqueue(object : Callback<PaytmResponse> {
            override fun onResponse(
                call: Call<PaytmResponse>,
                response: Response<PaytmResponse>
            ) {
                if (response.isSuccessful) {
                    val paytmResponse = response.body()
                    val checksum = paytmResponse?.head?.signature
                    val txnToken = paytmResponse?.body?.txnToken

                    Log.e("Paytm: onResponse response", "responsebody: $paytmResponse")
                    Log.e("Paytm: onResponse CHECKSUMHASH", "CHECKSUMHASH: $checksum")
                    Log.e("Paytm: onResponse TXNTOKEN", "TXNTOKEN: $txnToken")

                    if (txnToken != null) {
                        if (checksum != null) {
                            startPaytmTransaction(
                                params["ORDER_ID"]!!,
                                params["TXN_AMOUNT"]!!,
                                txnToken
                            )

                        }
                        Log.e(
                            "Paytm: onResponse success",
                            "responsebody: ${response.body()?.toString()}"
                        )
                    }
                } else {
                    // Handle the error
                    Log.e("Paytm: onResponse Error", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PaytmResponse>, t: Throwable) {
                // Handle failure
                Log.e("Paytm: onFailure", "Failure ehh: ${t.message}")
                Log.e("Paytm Call:", call.toString())
            }
        })
    }

    private fun startPaytmTransaction(orderId: String, amount: String, txnToken: String) {

        if (isOrderAlreadyPaid(orderId)){
            Log.e("Order already paid!", "End of transaction")
        }

        val paytmOrder = PaytmOrder(orderId, mID, txnToken, amount, callbackURL)
        val transactionManager = TransactionManager(paytmOrder, object : PaytmPaymentTransactionCallback {

                override fun onTransactionResponse(bundle: Bundle?) {
                    Log.e("Paytm: onTransactionResponse", "Response: $bundle")
                }

                override fun networkNotAvailable() {
                    // Handle network not available
                    Log.e("Paytm: Transaction", "Network Not Available")
                }

                override fun onErrorProceed(s: String) {
                    // Handle error
                    Log.e("Paytm: onErrorProceed", "Message: $s")
                }

                override fun clientAuthenticationFailed(s: String) {
                    // Handle authentication failed
                    Log.e("Paytm: clientAuthenticationFailed", "Message: $s")
                }

                override fun someUIErrorOccurred(s: String) {
                    // Handle UI error
                    Log.e("Paytm: someUIErrorOccurred", "Message: $s")
                }

                override fun onErrorLoadingWebPage(i: Int, s: String, s1: String) {
                    // Handle webpage load error
                    Log.e("Paytm: onErrorLoadingWebPage", "Message: $s")
                }

                override fun onBackPressedCancelTransaction() {
                    // Handle transaction cancel
                    Log.e("Paytm: onBackPressedCancelTransaction", "cancelled")
                }

                override fun onTransactionCancel(s: String, bundle: Bundle) {
                    // Handle transaction cancel
                    Log.e(
                        "Paytm: onTransactionCancel",
                        "Transaction Cancelled: $s, Bundle: $bundle"
                    )
                }
            })
        transactionManager.setAppInvokeEnabled(true)
        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage")
        transactionManager.startTransaction(this, ActivityRequestCode)
        Log.e("PaytmTransaction", "Transaction started")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ActivityRequestCode && data != null) {
            Toast.makeText(this, data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"), Toast.LENGTH_SHORT).show()
            checkTransactionStatus(orderId)
            val paymentStatus = data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response")
            statusTV.text = getString(R.string.payment_successful)
            // Launch a coroutine on the Main (UI) thread
            CoroutineScope(Dispatchers.Main).launch {
                // Perform a delay of 2000 milliseconds (2 seconds)
                statusTV.text = getString(R.string.redirecting)
                delay(2000)
            }
            // Step 4: Return the payment status to the WalletFragment
            val resultIntent = Intent()
            resultIntent.putExtra("paymentStatus", paymentStatus)
            setResult(RESULT_OK, resultIntent)
            finish()  // Close the PaymentActivity
        }
//        paymentButton.visibility = View.GONE
//        val intent = Intent(this, PaymentStatus::class.java)
//        startActivity(intent)
    }

    private fun updateOrderStatus(orderId: String){
        Log.e("Order Status", "Updated")
    }

    private fun isOrderAlreadyPaid(orderId: String) : Boolean {
        return true
    }


    // Function to check transaction status
    private fun checkTransactionStatus(orderId: String) {
        val params = HashMap<String, String>()
        params["ORDERID"] = orderId
        // Add other required parameters

        val call = RetrofitClient.apiService.checkTransactionStatus(params)
        call.enqueue(object : Callback<TransactionStatusResponse> {
            override fun onResponse(
                call: Call<TransactionStatusResponse>,
                response: Response<TransactionStatusResponse>
            ) {
                if (response.isSuccessful) {
                    val transactionStatus = response.body()
                    if (transactionStatus != null) {
                        // Handle transaction status
                        if (transactionStatus.STATUS == "TXN_SUCCESS") {
                            // Update order status on server
                            updateOrderStatus(orderId)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<TransactionStatusResponse>, t: Throwable) {
                // Handle failure
                Log.e("onFailure: TransactionStatusResponse", t.toString())
            }
        })
    }

}