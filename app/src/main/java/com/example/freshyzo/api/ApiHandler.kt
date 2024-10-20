package com.example.freshyzo.api

import com.example.freshyzo.model.BannerResponse
import com.example.freshyzo.model.CategoryResponse
import com.example.freshyzo.model.ColonyListResponse
import com.example.freshyzo.model.ColonyResponse
import com.example.freshyzo.model.CustomerRequest
import com.example.freshyzo.model.CustomerResponse
import com.example.freshyzo.model.LoginRequest
import com.example.freshyzo.model.LoginResponse
import com.example.freshyzo.model.OneTimePasswordRequest
import com.example.freshyzo.model.OrderItem
import com.example.freshyzo.model.OrderResponse
import com.example.freshyzo.model.ProductRequest
import com.example.freshyzo.model.ProductResponse
import com.example.freshyzo.model.ProfileUpdateRequest
import com.example.freshyzo.model.RechargeHistoryResponse
import com.example.freshyzo.model.SignUpRequest
import com.example.freshyzo.model.SignUpResponse
import com.example.freshyzo.model.WalletResponse
import com.example.freshyzo.network.RetrofitClient
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.emptyList as emptyList1

class ApiHandler {

    /** Callback interfaces to handle the results of the API operation **/

    interface ColonyListCallback {
        fun onSuccess(colonies: List<ColonyResponse>)
        fun onFailure(errorMessage: String)
    }

    interface ProductListCallback{
        fun onSuccess(products: List<ProductResponse>)
        fun onFailure(errorMessage: String)
    }

    interface BannerListCallback {
        fun onSuccess(images: List<String>)
        fun onFailure(errorMessage: String)
    }

    interface CategoriesListCallback{
        fun onSuccess(category: List<CategoryResponse>)
        fun onFailure(errorMessage: String)
    }

    interface CustomerListCallback {
        fun onSuccess(customerList: List<CustomerResponse>)
        fun onFailure(message: String)
    }

    interface RechargeListCallback {
        fun onSuccess(rechargeList: List<RechargeHistoryResponse>)
        fun onFailure(message: String)
    }

    /** Request OTP **/
    fun requestOTP(phoneNumber: String, method: String, callback: Callback<ResponseBody>) {
        val request = OneTimePasswordRequest(mobile_no = phoneNumber, method = method)
        val apiService = RetrofitClient.apiService.requestOTP(request)
        apiService.enqueue(callback)
    }

    /** Fetch Wallet Balance**/
    fun fetchWalletBalance(customerID: String, customerRole: String, callback: Callback<List<WalletResponse>>){
        val request = CustomerRequest(customerID = customerID, customerRole = customerRole)
        val apiService = RetrofitClient.apiService.getWalletBalance(request)
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
                    val colonies = response.body()?.colony ?: emptyList1()  // Access the 'colony' list
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

    /** Fetch Product List **/
    fun fetchProductList(productID: String, customerID: String, categoryID: String, callback: ProductListCallback) {
        val productRequest = ProductRequest(product_id = productID, customer_id = customerID, category_id = categoryID)
        val apiService = RetrofitClient.apiService.getProducts(productRequest)

        apiService.enqueue(object : Callback<List<ProductResponse>> {
            override fun onResponse(call: Call<List<ProductResponse>>, response: Response<List<ProductResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Pass the product list directly to the callback's onSuccess method
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onFailure("Error fetching products: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ProductResponse>>, t: Throwable) {
                callback.onFailure("API request failed: ${t.message}")
            }
        })
    }

    /** Fetch Customer Details **/
    fun fetchCustomerDetails(customerId: String, customerRole: String, callback: CustomerListCallback) {
        val customerRequest = CustomerRequest(customerID = customerId, customerRole = customerRole)
        val apiService = RetrofitClient.apiService.getCustomerDetails(customerRequest)
        apiService.enqueue(object : Callback<List<CustomerResponse>> {
            override fun onResponse(call: Call<List<CustomerResponse>>, response: Response<List<CustomerResponse>>) {
                if (response.isSuccessful) {
                    val customers = response.body() ?: emptyList1()
                    callback.onSuccess(customers)
                } else {
                    callback.onFailure("Failed to load customer data: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<CustomerResponse>>, t: Throwable) {
                callback.onFailure("Error occurred: ${t.message}")
            }
        })
    }

    /** Update Customer Details**/
    fun updateCustomerDetails(
        customerID: String, customerRole: String, firstName: String, lastName: String,
        number: String, address: String, colonyID: String, callback: Callback<ResponseBody>
    ) {
        val request = ProfileUpdateRequest(
            customerID = customerID, customerRole = customerRole, firstName = firstName,
            lastName = lastName, phoneNumber = number, address = address, colonyID = colonyID
        )

        val apiService = RetrofitClient.apiService.updateCustomerProfile(request)
        apiService.enqueue(callback)
    }

    /** Fetch Carousel Images **/
    fun fetchBannerImages(callback: BannerListCallback) {
        val apiService = RetrofitClient.apiService.getBanners()
        apiService.enqueue(object : Callback<List<BannerResponse>> {  // Expecting a List of Banner objects
            override fun onResponse(call: Call<List<BannerResponse>>, response: Response<List<BannerResponse>>) {
                if (response.isSuccessful) {
                    val bannerList = response.body()
                    val imageUrls = bannerList?.map {
                        "https://freshyzo.com/admin/uploads/offer_banner_image/214071727963239318d32f4.jpeg"
//                        "https://freshyzo.com/admin/uploads/offer_banner_image/${it.img_name}"
                    }
                    if (imageUrls != null) {
                        callback.onSuccess(imageUrls)
                    } else {
                        callback.onFailure("No images found")
                    }
                } else {
                    callback.onFailure("Failed to load banner images")
                }
            }

            override fun onFailure(call: Call<List<BannerResponse>>, t: Throwable) {
                callback.onFailure(t.message ?: "An error occurred")
            }
        })
    }

    /** Fetch Categories Label and Images **/
    fun fetchCategories(callback: CategoriesListCallback) {
        val apiService = RetrofitClient.apiService.getCategories()
        apiService.enqueue(object : Callback<List<CategoryResponse>> {
            override fun onResponse(
                call: Call<List<CategoryResponse>>,
                response: Response<List<CategoryResponse>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { categories ->
                        callback.onSuccess(categories) // Pass the list of categories
                    } ?: callback.onFailure("Empty response")
                } else {
                    callback.onFailure("Failed to fetch categories")
                }
            }

            override fun onFailure(call: Call<List<CategoryResponse>>, t: Throwable) {
                callback.onFailure(t.message ?: "Unknown error")
            }
        })
    }

    /** Fetch Recharge History **/
    fun fetchRechargeHistory(customerID: String, customerRole: String, callback: RechargeListCallback) {
        val request = CustomerRequest(customerID = customerID, customerRole = customerRole)
        val apiService = RetrofitClient.apiService.getRechargeHistory(request)

        apiService.enqueue(object : Callback<List<RechargeHistoryResponse>> {
            override fun onResponse(call: Call<List<RechargeHistoryResponse>>, response: Response<List<RechargeHistoryResponse>>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onFailure("Error fetching recharge history: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<RechargeHistoryResponse>>, t: Throwable) {
                callback.onFailure("API request failed: ${t.message}")
            }
        })
    }

    /** Fetch Order Details **/
    fun fetchOrders(customerId: String, customerRole: String, callback: (List<OrderResponse>?) -> Unit) {
        val request = CustomerRequest(customerID = customerId, customerRole = customerRole)
        val apiService = RetrofitClient.apiService.fetchOrders(request)
        apiService.enqueue(object : Callback<List<OrderResponse>> {
            override fun onResponse(call: Call<List<OrderResponse>>, response: Response<List<OrderResponse>>) {
                if (response.isSuccessful) {
                    val orderList = response.body() ?: emptyList1()

                    for (order in orderList) {
                        println("Total Order Price: ${order.totalOrderPrice}")
                        println("Order Status: ${order.orderStatus}")

                        // Parsing the online_order_details which is a JSON encoded string
                        val orderItems: Array<OrderItem> = Gson().fromJson(order.onlineOrderDetails, Array<OrderItem>::class.java)

                        for (item in orderItems) {
                            println("Item Name: ${item.itemName}")
                            println("Item Price: ${item.itemPrice}")
                        }
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<OrderResponse>>, t: Throwable) {
                println("Failed to fetch orders: ${t.message}")
            }
        })
    }
}