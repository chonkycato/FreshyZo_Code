package com.example.freshyzo.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R

class RecyclerAdapterCart : RecyclerView.Adapter<RecyclerAdapterCart.ViewHolder>() {

        private var dataListCart: List<DataModelCart> = emptyList<DataModelCart>()

    internal fun setDataList(dataList: List<DataModelCart>) {
        this.dataListCart = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTitle: TextView
        var itemDetail: TextView
        var itemPrice: TextView
        var itemSize: TextView
        var itemQty: TextView
        var itemImage: ImageView


        init {
            itemTitle = itemView.findViewById(R.id.cartItemName)
            itemDetail = itemView.findViewById(R.id.cartItemDetails)
            itemPrice = itemView.findViewById(R.id.cartItemPrice)
            itemSize = itemView.findViewById(R.id.itemSizeSpinner)
            itemQty = itemView.findViewById(R.id.cartItemQty)
            itemImage = itemView.findViewById(R.id.cartItemImage)
        }

    }

    /* Usually involves inflating a layout from XML and returning the holder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row_notifications, parent, false)
        return ViewHolder(view)
    }

    // Populate data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the data model based on position
        var item = dataListCart[position]

        // Set item views based on your views and data model
        holder.itemTitle.text = item.itemTitle
        holder.itemDetail.text = item.itemDetail
        holder.itemSize.text = item.itemSize
        holder.itemQty.text = item.itemQty.toString()
        holder.itemPrice.text = item.itemPrice
        holder.itemImage.setImageResource(item.itemImage)
    }

    //  total count of items in the list
    override fun getItemCount() = dataListCart.size

}