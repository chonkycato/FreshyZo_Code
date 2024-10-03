package com.example.freshyzo.model

import android.os.Parcel
import android.os.Parcelable

class Cart(var title: String,
           var itemDetail: String,
           var itemPrice: String,
           var itemSize: String,
           var itemQty: String,
           var image: Int)  : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(itemDetail)
        parcel.writeString(itemPrice)
        parcel.writeString(itemSize)
        parcel.writeString(itemQty)
        parcel.writeInt(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }
    }
}