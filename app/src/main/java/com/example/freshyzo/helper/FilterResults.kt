package com.example.freshyzo.helper

import android.widget.Filter
import com.example.freshyzo.adapter.RecyclerAdapterHome
import com.example.freshyzo.model.DataModelProduct
import java.util.Locale

class FilterResults(
    private val adapter: RecyclerAdapterHome,
    private val productList: ArrayList<DataModelProduct>
) : Filter() {
    override fun performFiltering(searchQuery: CharSequence?): FilterResults {
        val filteredResults = FilterResults()

        if (!searchQuery.isNullOrEmpty()){
            val query = searchQuery.toString().trim().uppercase(Locale.getDefault()).split(" ")
            val filteredProductList: ArrayList<DataModelProduct>
//            for (product in productList){
//                if (query.any{search->
//
//                    })
//            }
        }
        else{
            filteredResults.apply {
                count = productList.size
                values = filteredResults
            }
        }

        return filteredResults
    }

    override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
        TODO("Not yet implemented")
    }
}