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
import com.example.freshyzo.SubActiveFragment
import com.example.freshyzo.model.ActiveSubscription


class RecyclerAdapterSubActive(private val context: Context, listenerActiveSub: SubActiveFragment) : RecyclerView.Adapter<RecyclerAdapterSubActive.ViewHolder>() {

    private var dataListSub: List<ActiveSubscription> = emptyList<ActiveSubscription>()
    var onItemClicked : ((ActiveSubscription) -> Unit)? = null
    var onItemClickedPause : ((ActiveSubscription) -> Unit)? = null
    var onItemClickedCancel : ((ActiveSubscription) -> Unit)? = null


    internal fun setDataList(dataList: List<ActiveSubscription>) {
        this.dataListSub = dataList
    }

    // Provide a direct reference to each of the views with data items
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mProdImage: ImageView
        var mProdTitle: TextView
        var mProdPrice: TextView
        var mStartDate: TextView
        var mEndDate: TextView
        var mSubStatus: TextView
//        var mLocation: TextView
        var mPauseButton: Button
        var mCancelAButton: Button


        init {
            mProdImage = itemView.findViewById(R.id.activeSubProductIV)
            mProdTitle = itemView.findViewById(R.id.productNameASubTV)
            mProdPrice = itemView.findViewById(R.id.productPriceASubTV)
            mStartDate = itemView.findViewById(R.id.startDateSubTVASub)
            mEndDate = itemView.findViewById(R.id.endDateSubTVASub)
            mSubStatus = itemView.findViewById(R.id.subStatusSubTVASub)
//            mLocation = itemView.findViewById(R.id.deliveryLocationTVASub)
            mPauseButton = itemView.findViewById(R.id.pauseSubButton)
            mCancelAButton = itemView.findViewById(R.id.cancelButtonASub)
        }

    }

    /* Usually involves inflating a layout from XML and returning the holder */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the custom layout
        var view = LayoutInflater.from(parent.context).inflate(R.layout.custom_recycler_row_subscriptions_active, parent, false)
        return ViewHolder(view)
    }

    // Populate data into the item through holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the data model based on position
        var item = dataListSub[position]

        // Set item views based on your views and data model
        holder.mProdImage.setImageResource(item.prodImg)
        holder.mProdTitle.text = item.productTitle
        holder.mProdPrice.text = item.productPrice
        holder.mStartDate.text = item.startDate
        holder.mEndDate.text = item.endDate
        holder.mSubStatus.text = item.subStatus
//        holder.mLocation.text = item.deliveryLocation

        holder.itemView.setOnClickListener {
            onItemClicked?.invoke(item)
        }

        holder.mPauseButton.setOnClickListener{
            onItemClickedPause?.invoke(item)

        }

        holder.mCancelAButton.setOnClickListener{
            onItemClickedCancel?.invoke(item)
        }

    }

    //  total count of items in the list
    override fun getItemCount() = dataListSub.size

}


