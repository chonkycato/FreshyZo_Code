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
    var onItemClicked : ((DataModelCart) -> Unit)? = null

    internal fun setDataList(dataList: List<DataModelCart>) {
        this.dataListCart = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var itemDetail: TextView
        var itemPrice: TextView
        var itemSize: TextView
        var itemQty: TextView
        var itemImage: ImageView


        init {
            title = itemView.findViewById(R.id.orderItemName)
            itemDetail = itemView.findViewById(R.id.orderItemDetails)
            itemPrice = itemView.findViewById(R.id.orderItemPrice)
            itemSize = itemView.findViewById(R.id.orderItemSize)
            itemQty = itemView.findViewById(R.id.orderItemQty)
            itemImage = itemView.findViewById(R.id.orderItemImage)
        }

    }

    /* Usually involves inflating a layout from XML and returning the holder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row_cart, parent, false)
        return ViewHolder(view)
    }

    // Populate data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the data model based on position
        var item = dataListCart[position]

        // Set item views based on your views and data model
        holder.title.text = item.title
        holder.itemDetail.text = item.itemDetail
        holder.itemSize.text = item.itemSize
        holder.itemQty.text = item.itemQty.toString()
        holder.itemPrice.text = item.itemPrice
        holder.itemImage.setImageResource(item.image)

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }
    }

    //  total count of items in the list
    override fun getItemCount() = dataListCart.size

}