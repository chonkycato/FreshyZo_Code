package com.example.freshyzo.model

import android.os.Parcel
import android.os.Parcelable

class DataModelProduct(var title: String, var quantity: String, var price: String, var image: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(quantity)
        parcel.writeString(price)
        parcel.writeInt(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModelProduct> {
        override fun createFromParcel(parcel: Parcel): DataModelProduct {
            return DataModelProduct(parcel)
        }

        override fun newArray(size: Int): Array<DataModelProduct?> {
            return arrayOfNulls(size)
        }
    }
}