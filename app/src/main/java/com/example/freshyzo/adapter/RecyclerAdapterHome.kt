package com.example.freshyzo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freshyzo.R
import com.example.freshyzo.model.ProductResponse

class RecyclerAdapterHome(var context: Context) : RecyclerView.Adapter<RecyclerAdapterHome.ViewHolder>() {
    private var dataList: List<ProductResponse> = emptyList<ProductResponse>()
    var onItemClick: ((ProductResponse) -> Unit)? = null

    internal fun setDataList(dataList: List<ProductResponse>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.productItemIv)
        var title: TextView = itemView.findViewById(R.id.productTitleTv)
        var size: TextView = itemView.findViewById(R.id.size_text)
        var price: TextView = itemView.findViewById(R.id.price_text)
        var productStatus: TextView = itemView.findViewById(R.id.productSubscriptionStatus)
        val subscribeButton: Button = itemView.findViewById(R.id.subscribe_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_recylcer_row_products, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]

        // Separate product name and quantity
        val (productName, productQuantity) = separateProductNameAndQuantity(item.productName)

        // Bind the product name (without quantity) and quantity to the views
        holder.title.text = capitalizeWords(productName)
        holder.size.text = productQuantity ?: item.productUnit // Fallback to productUnit if no quantity found
        // Set the price

        val priceString = holder.itemView.context.getString(R.string.price_string, item.dairyMRP)
        holder.price.text = priceString

        // Use Glide to load product images dynamically
        val productImageURL = "https://freshyzo.com/admin/uploads/product_image/${item.productImage}"
        Glide.with(context)
            .load(productImageURL)
            .into(holder.itemImage)

        // Set product status visibility
        holder.productStatus.text = if (item.productStatus == "Active") "Active" else "Inactive"
        holder.productStatus.visibility = if (item.productStatus == "Active") View.VISIBLE else View.INVISIBLE

        // Handle item click
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }

        // Handle subscribe button click
        holder.subscribeButton.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun getItemCount() = dataList.size

    // Function to capitalize the first letter of each word
    fun capitalizeWords(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    }

    // Function to separate product name and quantity
    fun separateProductNameAndQuantity(input: String): Pair<String, String?> {
        val quantityPattern = Regex("(\\d+\\s*(ml|kg|g|l|gm|litres|grams|lit))$", RegexOption.IGNORE_CASE)

        // Check if the input matches the quantity pattern
        val matchResult = quantityPattern.find(input)
        return if (matchResult != null) {
            // If there's a match, split the product name and quantity
            val productName = input.removeRange(matchResult.range).trim()
            val quantity = matchResult.value.trim()
            Pair(productName, quantity)
        } else {
            // If no match found, return the original input as the product name, no quantity
            Pair(input, null)
        }
    }
}
