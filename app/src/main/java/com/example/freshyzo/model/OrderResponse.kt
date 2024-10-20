package com.example.freshyzo.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderItem(
    @SerializedName ("item_id") val itemID: String,
    @SerializedName ("item_name") val itemName: String,
    @SerializedName ("item_qty") val itemQuantity: String,
    @SerializedName ("item_price") val itemPrice: String,
    @SerializedName ("item_img") val itemImage: String,
    @SerializedName ("item_gst") val itemGst: Int
) : Parcelable

@Parcelize
data class OrderResponse(
    @SerializedName("online_order_details") val onlineOrderDetails: String, // Will parse this later
    @SerializedName ("total_order_price") val totalOrderPrice: String,
    @SerializedName ("delivery_charge") val deliveryCharge: String,
    @SerializedName ("point_discount") val pointDiscount: String,
    @SerializedName ("coupan_discount") val couponDiscount: String,
    @SerializedName ("online_order_date") val orderDate: String,
    @SerializedName ("delivery_date") val deliveryDate: String,
    @SerializedName ("order_status") val orderStatus: String
) : Parcelable

