package com.example.freshyzo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R
import com.example.freshyzo.helper.StringManipulation
import com.example.freshyzo.model.OrderResponse

class OrderAdapter(private val orderList: List<OrderResponse>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderImage: ImageView = itemView.findViewById(R.id.orderImage)
        val orderProductName: TextView = itemView.findViewById(R.id.orderProductName)
        val orderTotalPrice: TextView = itemView.findViewById(R.id.orderTotalPrice)
        val orderProductSize: TextView = itemView.findViewById(R.id.orderProductSize)
        val orderProductQuantity: TextView = itemView.findViewById(R.id.orderProductQuantity)
        val orderDate: TextView = itemView.findViewById(R.id.orderDate)
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val orderItemDeliveryDate: TextView = itemView.findViewById(R.id.orderItemDeliveryDate)
        val markUndelivered: TextView = itemView.findViewById(R.id.mark_undelivered)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_recycler_row_orders, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]

        val productName = holder.itemView.context.getString(R.string.price_string, order.)
        // Bind order information to the layout views
        holder.orderProductName.text = "Order #" + order.onlineOrderDetails // You can parse this further
        holder.orderTotalPrice.text = "Total: ₹" + order.totalOrderPrice
        holder.orderDate.text = "Order Date: " + order.orderDate
        holder.orderStatus.text = order.orderStatus
        holder.orderItemDeliveryDate.text = "Delivery Date: " + order.deliveryDate

        val string = StringManipulation().capitalizeWords("string")
        Log.d("Capital", string)

        // You can set the product image using Glide or another image loading library
        // Assuming each order has one or more items, just load the first item's image
        // Glide.with(holder.itemView.context).load(order.items[0].itemImage).into(holder.orderImage)

        // Parse order items (if applicable)
        // For simplicity, assume the first item represents the order (you can modify this)
        val firstItem = order.onlineOrderDetails // You can parse this JSON to get actual items

        // Assuming you have parsed items from the order:
        // holder.orderProductSize.text = "Size: " + firstItem.itemSize
        // holder.orderProductQuantity.text = "Quantity: " + firstItem.itemQuantity

        // Set undelivered/mark undelivered functionality
        holder.markUndelivered.setOnClickListener {
            // Handle undelivered marking logic here
            Toast.makeText(holder.itemView.context, "Marking order as undelivered", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}
