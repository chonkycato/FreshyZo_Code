package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.model.DataModelOrders
import com.example.freshyzo.model.RecyclerAdapterOrders

class OrdersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterOrders
    private var dataList = mutableListOf<DataModelOrders>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        (activity as MainActivity).setBottomNavVisibility(false)

        val mBackBtn = view.findViewById<Button>(R.id.back_icon_orders)

        dataList.add(DataModelOrders(resources.getString(R.string.freshyzo_malai_dahi), resources.getString(R.string.size_500_gm), "2", R.drawable.img_dahi, resources.getString(R.string.delivery_by_today_5_pm)))
        dataList.add(DataModelOrders(resources.getString(R.string.freshyzo_ghee_butter), resources.getString(R.string._1_kg), "1", R.drawable.img_ghee, "Delivered on 23rd April"))
        dataList.add(DataModelOrders(resources.getString(R.string.milk), resources.getString(R.string.size_500_gm), "2", R.drawable.img_milk, "Delivered on 22nd April"))

        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        recyclerAdapter = RecyclerAdapterOrders()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.setDataList(dataList)
        recyclerAdapter.onItemClicked = {
            val intentOrders = Intent(activity, ProductActivity::class.java)
            intentOrders.putExtra("product", it)
            Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).startActivity(intentOrders)
        }

        mBackBtn.setOnClickListener {
            try {
//                findNavController().navigate(R.id.action_productFragment_to_homeFragment)
                (activity as MainActivity).loadFragment(HomeFragment(), false)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }


        return view
    }
}

