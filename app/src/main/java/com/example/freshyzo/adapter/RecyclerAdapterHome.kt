package com.example.freshyzo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R
import com.example.freshyzo.model.DataModelProduct

class RecyclerAdapterHome(var context: Context) : RecyclerView.Adapter<RecyclerAdapterHome.ViewHolder>() {

    private var dataList: List<DataModelProduct> = emptyList<DataModelProduct>()
    private var filteredDataList: List<DataModelProduct> = emptyList<DataModelProduct>()
    var onItemClick : ((DataModelProduct) -> Unit)? = null

    internal fun setDataList(dataList: List<DataModelProduct>) {
        this.dataList = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var title: TextView
        var description: TextView
        var size: TextView
        var price: TextView
        val subscribeButton: Button

        init {
            itemImage = itemView.findViewById(R.id.productItemIv)
            description = itemView.findViewById(R.id.productDescTv)
            title = itemView.findViewById(R.id.productTitleTv)
            size = itemView.findViewById(R.id.size_text)
            price = itemView.findViewById(R.id.price_text)
            subscribeButton = itemView.findViewById(R.id.subscribe_btn)
        }
    }

    /* Usually involves inflating a layout from XML and returning the holder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recylcer_row_products, parent, false)
        return ViewHolder(view)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the data model based on position
        var item = dataList[position]

        // Set item views based on your views and data model
        holder.title.text = item.title
        holder.description.text = item.description
        holder.size.text = item.size
        holder.price.text = item.price
        holder.itemImage.setImageResource(item.image)

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(item)
        }

        holder.subscribeButton.setOnClickListener {
            onItemClick?.invoke(item)
        }

    }
    //  total count of items in the list
    override fun getItemCount() = dataList.size


}