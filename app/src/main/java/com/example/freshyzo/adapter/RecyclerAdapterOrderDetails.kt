package com.example.freshyzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R
import com.example.freshyzo.model.DataModelOrderDetails

class RecyclerAdapterOrderDetails : RecyclerView.Adapter<RecyclerAdapterOrderDetails.ViewHolder>() {

    private var dataListOrderDetails: List<DataModelOrderDetails> = emptyList<DataModelOrderDetails>()

    internal fun setDataList(dataList: List<DataModelOrderDetails>) {
        this.dataListOrderDetails = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTitle: TextView
        var itemSize: TextView
        var itemQty: TextView
        var itemImage: ImageView

        init {
            itemTitle = itemView.findViewById(R.id.orderProductName)
            itemSize = itemView.findViewById(R.id.orderProductSize)
            itemQty = itemView.findViewById(R.id.orderProductQuantity)
            itemImage = itemView.findViewById(R.id.orderImage)
        }

    }

    /* Usually involves inflating a layout from XML and returning the holder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row_order_details, parent, false)
        return ViewHolder(view)
    }

    // Populate data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the data model based on position
        var item = dataListOrderDetails[position]

        // Set item views based on your views and data model

        holder.itemTitle.text = item.itemTitle
        holder.itemSize.text = item.itemSize
        holder.itemQty.text = item.itemQty
        holder.itemImage.setImageResource(item.itemImage)
    }

    //  total count of items in the list
    override fun getItemCount() = dataListOrderDetails.size

}