package com.example.freshyzo.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R

class RecyclerAdapterOrders : RecyclerView.Adapter<RecyclerAdapterOrders.ViewHolder>() {

    private var dataListOrders: List<DataModelOrders> = emptyList<DataModelOrders>()
    var onItemClicked : ((DataModelOrders) -> Unit)? = null

    internal fun setDataList(dataList: List<DataModelOrders>) {
        this.dataListOrders = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var itemTitle: TextView
        var itemSize: TextView
        var itemQty: TextView
        var itemDelivery: TextView
        var itemImage: ImageView

        init {
            itemTitle = itemView.findViewById(R.id.orderItemName)
            itemSize = itemView.findViewById(R.id.orderItemSize)
            itemQty = itemView.findViewById(R.id.orderItemQty)
            itemDelivery = itemView.findViewById(R.id.orderItemDelivery)
            itemImage = itemView.findViewById(R.id.orderItemImage)
        }

    }

    /* Usually involves inflating a layout from XML and returning the holder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row_orders, parent, false)
        return ViewHolder(view)
    }

    // Populate data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the data model based on position
        var item = dataListOrders[position]

        // Set item views based on your views and data model

        holder.itemTitle.text = item.itemTitle
        holder.itemSize.text = item.itemSize
        holder.itemQty.text = item.itemQty
        holder.itemDelivery.text = item.itemDelivery
        holder.itemImage.setImageResource(item.itemImage)

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }
    }

    //  total count of items in the list
    override fun getItemCount() = dataListOrders.size

}