package com.example.freshyzo.model

import android.os.Parcel
import android.os.Parcelable


class DataModelActiveSub (
    var prodImg: Int, var productTitle: String, var productPrice: String,
    var startDate: String, var endDate: String, var subStatus: String,
//    var deliveryLocation: String
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
//        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(prodImg)
        parcel.writeString(productTitle)
        parcel.writeString(productPrice)
        parcel.writeString(startDate)
        parcel.writeString(endDate)
        parcel.writeString(subStatus)
//        parcel.writeString(deliveryLocation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModelActiveSub> {
        override fun createFromParcel(parcel: Parcel): DataModelActiveSub {
            return DataModelActiveSub(parcel)
        }

        override fun newArray(size: Int): Array<DataModelActiveSub?> {
            return arrayOfNulls(size)
        }
    }
}