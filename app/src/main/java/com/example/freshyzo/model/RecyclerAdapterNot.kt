package com.example.freshyzo.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R

class RecyclerAdapterNot : RecyclerView.Adapter<RecyclerAdapterNot.ViewHolder>() {

    private var dataListNot: List<DataModelNotification> = emptyList<DataModelNotification>()

    internal fun setDataList(dataList: List<DataModelNotification>) {
        this.dataListNot = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView
        var title: TextView
        var body: TextView

        init {
            date = itemView.findViewById(R.id.update_date)
            title = itemView.findViewById(R.id.notification_title)
            body = itemView.findViewById(R.id.notification_body)
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
        var item = dataListNot[position]

        // Set item views based on your views and data model
        holder.date.text = item.date
        holder.title.text = item.title_not
        holder.body.text = item.body_not
    }

    //  total count of items in the list
    override fun getItemCount() = dataListNot.size

}