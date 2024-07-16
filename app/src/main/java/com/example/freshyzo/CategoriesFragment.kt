package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.model.DataModelProduct
import com.example.freshyzo.model.RecyclerAdapterHome

class CategoriesFragment() : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerAdapter: RecyclerAdapterHome
    private var allProdList = mutableListOf<DataModelProduct>()
    private var filteredList = mutableListOf<DataModelProduct>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val mCategoryLabel = view.findViewById<TextView>(R.id.categoryLabel)
        val mCategoryItems = view.findViewById<TextView>(R.id.categoryItems)
        (activity as MainActivity).hideBottomNav()

        /** Handle back navigation **/
        val backNavIcon = view.findViewById<Button>(R.id.back_icon_prod_categories)
        backNavIcon.setOnClickListener { (activity as MainActivity).backNavigation() }


        /** Category search **/
        var bundleMsg = ""
        val bundleMessage = arguments?.getString("Search Query")
        if(bundleMessage!=null){
            bundleMsg = bundleMessage
            mCategoryLabel.text = bundleMessage
        }


        // TODO: (sort and display by product category)
        allProdList.add(DataModelProduct("Milk Products","FreshyZo Ghee Butter", "100% Pure desi ghee","250 gms","₹350",R.drawable.img_ghee))
        allProdList.add(DataModelProduct("Milk","FreshyZo Cow Milk", "100% pure desi cow milk","200 ml","₹25",R.drawable.img_milk))
        allProdList.add(DataModelProduct("Milk","FreshyZo Buffalo Milk", "100% pure buffalo milk","200 ml","₹25",R.drawable.img_milk_buff))
        allProdList.add(DataModelProduct("Milk Products","FreshyZo Malai Dahi", "Fresh and creamy malai dahi","250 ml","₹35",R.drawable.img_dahi))
        allProdList.add(DataModelProduct("Milk Products","FreshyZo Paneer", "Fresh and soft paneer","250 gms","₹250",R.drawable.img_paneer))

        mRecyclerView = view.findViewById(R.id.recylerViewCategories)
        mRecyclerView.layoutManager = GridLayoutManager(context, 2)
        mRecyclerAdapter = RecyclerAdapterHome(requireContext())
        mRecyclerView.adapter = mRecyclerAdapter

        mRecyclerAdapter.onItemClick = {
            val intentHome = Intent(activity, ProductActivity::class.java)
            intentHome.putExtra("product", it)
            Toast.makeText(context, bundleMessage.toString(), Toast.LENGTH_SHORT).show()
            (activity as MainActivity).startActivity(intentHome)
        }

        filterProductList(bundleMsg, allProdList)
        mRecyclerAdapter.setDataList(filteredList)
        val text = filteredList.size.toString() + " products found."
        mCategoryItems.text = text

        return view
    }


    fun filterProductList(query: String, list: MutableList<DataModelProduct>) {
        val filterList = if (query.isEmpty()) {
            list
        } else {
            list.filter {
                 it.category.contains(query, ignoreCase = true)
            }
        }
        filteredList.clear()
        filteredList.addAll(filterList)
    }



}