package com.example.freshyzo.model

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.freshyzo.MainActivity
import org.json.JSONArray
import org.json.JSONObject


class ApiCall {

    val url = "https://freshyzo.com/admin/Agent_App_Api/"

    interface LoginCallback {
        fun processFinish(result: Boolean, id: String)
    }
    interface ProfileDataCallback {
        fun processFinish(result: JSONObject)
    }
    interface DeliveryListCallback {
        fun processFinish(result: JSONObject)
    }
    interface SubmitDeliveryCallback {
        fun processFinish(result: JSONObject)
    }

    fun auth(email: String, password: String, context: MainActivity, callback: LoginCallback){
//        println(email)
//        println(password)
        val queue = Volley.newRequestQueue(context)

        // making a string request to update our data and
        // passing method as POST. to update our data.
        val request: StringRequest =
            object : StringRequest(
                Method.POST, url+"team_login",
                Response.Listener { response ->
                    // on below line we are displaying a toast message as data updated.
                    println("api login response")
                    //println(response)
                    if(response!="" && response!="failed"){
                        println("not null and login success")
                        callback.processFinish(true, response.toString())
                    }else{
                        println("login failed")
                        callback.processFinish(false, response.toString())
                    }
                }, Response.ErrorListener { error ->
                    // displaying toast message on response failure.
                    println("api login error")
                    println(error!!.message)
                    callback.processFinish(false, "Unable to connect, Check your internet connection ")
                }) {
                override fun getParams(): Map<String, String> {

                    // below line we are creating a map for storing
                    // our values in key and value pair.
                    val params: MutableMap<String, String> = HashMap()

                    // on below line we are passing our key
                    // and value pair to our parameters.
                    params["email"] = email
                    params["password"] = password
                    params["login"] = "login"

                    // at last we are
                    // returning our params.
                    return params
                }
            }
        // below line is to make
        // a json object request.
        queue.add(request)
    }

    fun getProfileData(userId: String, context: MainActivity ,callback: ProfileDataCallback){
//        println(userId)
        val jsonObject = JSONObject()
        jsonObject.put("login_email", userId)

        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(
            Request.Method.POST, url+"user_profile_data", jsonObject,
            { response ->
                // Handle the response
                println("response profile $response")
//                println(response::class.simpleName)
                callback.processFinish(response)
            },
            { error ->
                // Handle the error
                println("error profile $error")
            }
        )
        queue.add(request)

    }
    fun getDeliveryList(userId: String, context: Context, callback: DeliveryListCallback){
        println(userId)
        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(Request.Method.GET, url+"delivery_details/"+userId+"/all/all/1", null,
            { response ->
                // Handle the JSON response here
                println("getDeliverylist $response")
                callback.processFinish(response)


            },
            { error ->
                // Handle the error here
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            })

        // Add the request to the RequestQueue
        queue.add(request)

    }

    fun submitDelivery(customerId: String, agentId: String,arr: JSONArray, extraOrder: Boolean, context: Context, callback: SubmitDeliveryCallback){
        val jsonObject = JSONObject()
        jsonObject.put("linked_id", customerId)
        jsonObject.put("agent_id", agentId)
        jsonObject.put("arr", arr)
        println(jsonObject)
        var urladd: String
        if(extraOrder) urladd = "submit_extra_order" else urladd = "submit_today_order"
        val queue = Volley.newRequestQueue(context)
        val request = JsonObjectRequest(
            Request.Method.POST, url+ urladd, jsonObject,
            { response ->
                // Handle the response
//                println("response submitdelivery $response")
//                println(response::class.simpleName)
                callback.processFinish(response)

            },
            { error ->
                // Handle the error
                println("error submitdelivery $error")
            }
        )
        queue.add(request)
    }

}
