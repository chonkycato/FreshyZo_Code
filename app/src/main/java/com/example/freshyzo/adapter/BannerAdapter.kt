package com.example.freshyzo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.freshyzo.R

// BannerAdapter.kt
class BannerAdapter(private val imageUrls: List<String>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    inner class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.bannerImageView) // Make sure to have this ImageView in your layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_banner_layout, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val imageUrl = imageUrls[position]

        Glide.with(holder.imageView.context)
            .load(imageUrl)
            .override(Target.SIZE_ORIGINAL, 300) // Load with original size
            .into(holder.imageView) // Load image into the ImageView
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
}

