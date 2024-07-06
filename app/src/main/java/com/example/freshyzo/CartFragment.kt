package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.model.BottomNavVisibilityListener
import com.example.freshyzo.model.DataModelCart
import com.example.freshyzo.model.RecyclerAdapterCart

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterCart
    private var dataList = mutableListOf<DataModelCart>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(false)

        val cartProd = (activity as MainActivity).displayCartItems()

        
        //add data
        dataList.add(DataModelCart(resources.getString(R.string.freshyzo_ghee_butter), resources.getString(R.string._100_pure_desi_ghee), resources.getString(R.string.itemPrice), resources.getString(R.string._1_kg), resources.getString(R.string.qty_1), R.drawable.img_ghee))
        dataList.add(DataModelCart(resources.getString(R.string.freshyzo_ghee_butter), resources.getString(R.string._100_pure_desi_ghee), resources.getString(R.string.itemPrice), resources.getString(R.string._1_kg), resources.getString(R.string.qty_1), R.drawable.img_ghee))
        dataList.add(DataModelCart(resources.getString(R.string.freshyzo_ghee_butter), resources.getString(R.string._100_pure_desi_ghee), resources.getString(R.string.itemPrice), resources.getString(R.string._1_kg), resources.getString(R.string.qty_1), R.drawable.img_ghee))

        recyclerView = view.findViewById(R.id.recyclerViewCart)
        recyclerAdapter = RecyclerAdapterCart()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = recyclerAdapter
        recyclerAdapter.setDataList(dataList)

        recyclerAdapter.onItemClicked = {
            val intentCart = Intent(activity, ProductActivity::class.java)
            intentCart.putExtra("cart", it)
            Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).startActivity(intentCart)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
    }

    override fun onPause() {
        super.onPause()
    }
}