package com.example.freshyzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.R
import com.example.freshyzo.helper.StringManipulation
import com.example.freshyzo.model.RechargeHistoryResponse

class RechargeAdapter(private val rechargeList: List<RechargeHistoryResponse>) :
    RecyclerView.Adapter<RechargeAdapter.RechargeViewHolder>() {

    // ViewHolder class to hold references to the views for each item
    class RechargeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rechargeID: TextView = itemView.findViewById(R.id.tvRechargeID)
        val rechargeAmount: TextView = itemView.findViewById(R.id.tvRechargeAmount)
        val rechargeDate: TextView = itemView.findViewById(R.id.tvRechargeDate)
    }

    // Inflate the custom item layout and create a ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RechargeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_recharge_history_layout, parent, false)
        return RechargeViewHolder(view)
    }

    // Bind data from the RechargeHistoryResponse object to the views
    override fun onBindViewHolder(holder: RechargeViewHolder, position: Int) {
        val recharge = rechargeList[position]
        val rechargeID = holder.itemView.context.getString(R.string.recharge_id, recharge.rechargeID)
        val rechargeAmount = holder.itemView.context.getString(R.string.price_string, recharge.rechargeAmount)
        holder.rechargeID.text = rechargeID
        holder.rechargeAmount.text = rechargeAmount
        holder.rechargeDate.text = StringManipulation().removeStringAfterDelimiter(recharge.rechargeDate, " ")
    }

    // Return the total count of items
    override fun getItemCount(): Int {
        return rechargeList.size
    }


}
