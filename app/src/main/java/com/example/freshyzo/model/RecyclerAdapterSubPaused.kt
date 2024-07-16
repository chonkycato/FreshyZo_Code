package com.example.freshyzo.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R


class RecyclerAdapterSubPaused : RecyclerView.Adapter<RecyclerAdapterSubPaused.ViewHolder>() {

    private var dataList: List<DataModelPausedSub> = emptyList<DataModelPausedSub>()
    var onItemClickResume : ((DataModelPausedSub) -> Unit)? = null
    var onItemClickCancel: ((DataModelPausedSub) -> Unit)? = null

    internal fun setDataList(dataList: List<DataModelPausedSub>) {
        this.dataList = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var prodImage: ImageView
        var prodTitle: TextView
        var prodPrice: TextView
        var startDate: TextView
        var pauseDate: TextView
        var subStatus: TextView
//        var location: TextView
        var mResumeButton : Button
        var mCancelPButton : Button

        init {
            prodImage = itemView.findViewById(R.id.pausedSubProductIV)
            prodTitle = itemView.findViewById(R.id.productNamePSubTV)
            prodPrice = itemView.findViewById(R.id.productPricePSubTV)
            startDate = itemView.findViewById(R.id.startDateSubTVPSub)
            pauseDate = itemView.findViewById(R.id.pauseDateSubTVPSub)
            subStatus = itemView.findViewById(R.id.subStatusSubTVPSub)
//            location = itemView.findViewById(R.id.deliveryLocationTVPSub)
            mResumeButton = itemView.findViewById(R.id.resumeSubButton)
            mCancelPButton = itemView.findViewById(R.id.cancelButtonBSub)
        }

    }

    /* Usually involves inflating a layout from XML and returning the holder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row_subscriptions_paused, parent, false)
        return ViewHolder(view)
    }

    // Populate data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the data model based on position
        var item = dataList[position]

        // Set item views based on your views and data model
        holder.prodImage.setImageResource(item.prodImg)
        holder.prodTitle.text = item.productTitle
        holder.prodPrice.text = item.productPrice
        holder.startDate.text = item.startDate
        holder.pauseDate.text = item.pauseDate
        holder.subStatus.text = item.subStatus
//        holder.location.text = item.deliveryLocation

        holder.mResumeButton.setOnClickListener{
            onItemClickResume?.invoke(item)
        }

        holder.mCancelPButton.setOnClickListener{
            onItemClickCancel?.invoke(item)
        }
    }

    //  total count of items in the list
    override fun getItemCount() = dataList.size

}