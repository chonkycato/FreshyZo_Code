package com.example.freshyzo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("product_id") val productID: String,
    @SerializedName("product_name") val productName: String,
    @SerializedName("dairy_product_image") val productImage: String,
    @SerializedName("unit") val productUnit: String,
    @SerializedName("product_price") val productPrice: String,
    @SerializedName("dairy_mrp") val dairyMRP: String,
    @SerializedName("product_type") val productType: String,
    @SerializedName("required_milk") val requiredMilk: String,
    @SerializedName("product_status") val productStatus: String,
    @SerializedName("dairy_product_id") val dairyProductID: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(productID)
        parcel.writeString(productName)
        parcel.writeString(productImage)
        parcel.writeString(productUnit)
        parcel.writeString(productPrice)
        parcel.writeString(dairyMRP)
        parcel.writeString(productType)
        parcel.writeString(requiredMilk)
        parcel.writeString(productStatus)
        parcel.writeString(dairyProductID)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ProductResponse> {
        override fun createFromParcel(parcel: Parcel): ProductResponse {
            return ProductResponse(parcel)
        }

        override fun newArray(size: Int): Array<ProductResponse?> {
            return arrayOfNulls(size)
        }
    }
}
