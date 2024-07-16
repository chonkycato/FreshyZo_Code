package com.example.freshyzo

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

        (activity as MainActivity).hideBottomNav()

        /** Handle back navigation **/
        val backNavIcon = view.findViewById<Button>(R.id.back_icon_orders)
        backNavIcon.setOnClickListener { (activity as MainActivity).backNavigation() }

        /** Set up and populate recycler view **/

        dataList.add(DataModelOrders(resources.getString(R.string.freshyzo_malai_dahi), resources.getString(R.string.sample_item_size), "2", R.drawable.img_dahi, resources.getString(R.string.delivery_by_today_5_pm)))
        dataList.add(DataModelOrders(resources.getString(R.string.freshyzo_ghee_butter), resources.getString(R.string._1_kg), "1", R.drawable.img_ghee, "Delivered, 23rd April"))
        dataList.add(DataModelOrders(resources.getString(R.string.cow_milk), resources.getString(R.string.sample_item_size), "2", R.drawable.img_milk, "Delivered, 22nd April"))

        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        recyclerAdapter = RecyclerAdapterOrders()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.setDataList(dataList)
        recyclerAdapter.onItemClicked = {
            Toast.makeText(requireContext(), "ITEM TAPPED", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putString("orderID","ORDER12345")
            }
            OrderDetailsFragment().arguments = bundle
            (activity as MainActivity).loadFragment(OrderDetailsFragment(), false, null)
        }

        return view
    }

//    private fun openOrderDetails(order: DataModelOrders) {
//        val bundle = Bundle().apply {
//            putString("itemTitle", order.itemTitle)
//            putString("itemDelivery", order.itemDelivery)
//            putInt("itemImage", order.itemImage)
//            putString("itemSize", order.itemSize)
//            putString("itemQty", order.itemQty)
//        }
//        OrderDetailsFragment().arguments = bundle
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainerView, OrderDetailsFragment())
//            .addToBackStack(null)
//            .commit()
//    }
}

