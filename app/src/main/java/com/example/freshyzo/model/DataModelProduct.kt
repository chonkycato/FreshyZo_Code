package com.example.freshyzo.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class DataModelProduct(var category: String, var title: String, var description: String, var quantity: String, var size: String, var price: String, var image: Int) : Parcelable, Serializable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(category)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(quantity)
        parcel.writeString(size)
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