package com.example.freshyzo.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.freshyzo.R

// ViewModel class to hold and manage UI-related data
class OrdersViewModel : ViewModel() {

    // This list holds the data for orders, it acts as a Model
    var dataList = mutableListOf<DataModelOrders>()

    // Method to populate the initial data list
    fun populateInitialDataList() {
        Log.d("OrdersViewModel", "Data List Initialised")

        if (dataList.isEmpty()) {
            dataList.add(DataModelOrders("Malai Dahi", "500g", "2", R.drawable.img_dahi, "Delivery by Today 5 PM"))
            dataList.add(DataModelOrders("Ghee Butter", "1 Kg", "1", R.drawable.img_ghee, "Delivered, 23rd April"))
            dataList.add(DataModelOrders("Cow Milk", "500ml", "2", R.drawable.img_milk, "Delivered, 22nd April"))
        }

        Log.d("OrdersViewModel", "Data List Size: ${dataList.size}")
    }
}
