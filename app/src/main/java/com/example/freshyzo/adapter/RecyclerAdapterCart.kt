package com.example.freshyzo.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R
import com.example.freshyzo.model.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RecyclerAdapterCart : RecyclerView.Adapter<RecyclerAdapterCart.ViewHolder>() {

    private var dataListCart: MutableList<Cart> =  mutableListOf()
    var onItemClicked : ((Cart) -> Unit)? = null

    internal fun setDataList(dataList: MutableList<Cart>) {
        this.dataListCart = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.orderProductName)
        var itemDetail: TextView = itemView.findViewById(R.id.orderItemDetails)
        var itemPrice: TextView = itemView.findViewById(R.id.orderItemPrice)
        var itemSize: TextView = itemView.findViewById(R.id.orderItemSize)
        var itemQty: TextView = itemView.findViewById(R.id.orderItemQty)
        var itemImage: ImageView = itemView.findViewById(R.id.orderImage)
        var deleteCartItem: ImageView = itemView.findViewById(R.id.deleteCartItem)
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

        holder.deleteCartItem.setOnClickListener {
            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes") { _, _ -> deleteItem(position) }
                .setNegativeButton("No", null)
                .show()
        }
    }



    private fun deleteItem(position: Int) {
        dataListCart.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, dataListCart.size)
    }

    fun deleteItemFromDatabase(item: String) {
        CoroutineScope(Dispatchers.IO).launch {
            //myDatabase.myDao().delete(item)
        }
    }

    //  total count of items in the list
    override fun getItemCount() = dataListCart.size



}