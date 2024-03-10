package com.example.freshyzo.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R

class RecyclerAdapter(var context: Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var dataList = emptyList<DataModel>()

    internal fun setDataList(dataList: List<DataModel>) {
        this.dataList = dataList
    }
    // Provide a direct reference to each of the views with data items

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var title: TextView
        var quantity: TextView
        var price: TextView

        init {
            image = itemView.findViewById(R.id.image)
            title = itemView.findViewById(R.id.title)
            quantity = itemView.findViewById(R.id.quantity_text)
            price = itemView.findViewById(R.id.price_text)
        }

    }

        // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recylcer_row, parent, false)
        return ViewHolder(view)
    }

        // Involves populating data into the item through holder
     override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        // Get the data model based on position
        var data = dataList[position]

        // Set item views based on your views and data model
        holder.title.text = data.title
        holder.quantity.text = data.quantity
        holder.price.text = data.price

        holder.image.setImageResource(data.image)
     }
     //  total count of items in the list
    override fun getItemCount() = dataList.size
}