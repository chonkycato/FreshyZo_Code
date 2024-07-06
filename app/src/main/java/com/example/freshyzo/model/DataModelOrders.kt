package com.example.freshyzo.model

import android.os.Parcel
import android.os.Parcelable

class DataModelOrders (var itemTitle: String, var itemSize: String, var itemQty: String,
                       var itemImage: Int, var itemDelivery: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemTitle)
        parcel.writeString(itemSize)
        parcel.writeString(itemQty)
        parcel.writeInt(itemImage)
        parcel.writeString(itemDelivery)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModelOrders> {
        override fun createFromParcel(parcel: Parcel): DataModelOrders {
            return DataModelOrders(parcel)
        }

        override fun newArray(size: Int): Array<DataModelOrders?> {
            return arrayOfNulls(size)
        }
    }
}