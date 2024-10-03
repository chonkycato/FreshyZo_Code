package com.example.freshyzo.model

import android.os.Parcel
import android.os.Parcelable


class DataModelPausedSub (
    var prodImg: Int, var productTitle: String, var productPrice: String,
    var pauseStartDate: String, var pauseEndDate: String, var subStatus: String,
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
        parcel.writeString(pauseStartDate)
        parcel.writeString(pauseEndDate)
        parcel.writeString(subStatus)
//        parcel.writeString(deliveryLocation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataModelPausedSub> {
        override fun createFromParcel(parcel: Parcel): DataModelPausedSub {
            return DataModelPausedSub(parcel)
        }

        override fun newArray(size: Int): Array<DataModelPausedSub?> {
            return arrayOfNulls(size)
        }
    }
}