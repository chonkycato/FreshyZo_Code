package com.example.freshyzo

import RecyclerAdapterOrders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.model.DataModelOrders
import com.example.freshyzo.model.OrdersViewModel

class OrdersFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterOrders
    private var dataList = mutableListOf<DataModelOrders>()
    // Access the ViewModel shared across the activity (or use "by viewModels()" if it's Fragment-specific)
    private var ordersViewModel: OrdersViewModel by viewModels<>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_orders, container, false)

        (activity as MainActivity).hideBottomNav()

        // Initialize the ViewModel (using ViewModelProvider)
        ordersViewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)



        /** Handle back navigation **/
        val backNavIcon = view.findViewById<Button>(R.id.back_icon_orders)
        backNavIcon.setOnClickListener { (activity as MainActivity).backNavigation() }


        recyclerView = view.findViewById(R.id.recyclerViewOrders)
        recyclerAdapter = RecyclerAdapterOrders()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

        // Populate the dataList in ViewModel (only if not already populated)
        ordersViewModel.populateInitialDataList()

        recyclerAdapter.setDataList(dataList)
        recyclerAdapter.onItemClicked = {
            Toast.makeText(requireContext(), "ITEM TAPPED", Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply {
                putString("orderID","ORDER12345")
            }
            OrderDetailsFragment().arguments = bundle
            (activity as MainActivity).loadFragment(OrderDetailsFragment(), false, null)
        }

        // Handle "Mark Undelivered" click
        recyclerAdapter.onMarkUndeliveredClicked = { order, markUndeliveredView ->
            markOrderAsUndelivered(order, markUndeliveredView)
        }

        return view
    }

    private fun markOrderAsUndelivered(order: DataModelOrders, markUndeliveredView: TextView) {
        // Update the order's delivery status
        order.itemDelivery = "Undelivered"
        recyclerAdapter.notifyDataSetChanged()

        // Change the text to "Marked Undelivered"
        markUndeliveredView.text = "Marked Undelivered"
        markUndeliveredView.isEnabled = false // Optional: disable the button after marking

        // Disable the TextView to prevent further taps
        markUndeliveredView.isEnabled = false

        // Change text color to indicate it's disabled
        markUndeliveredView.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))

        // Remove the click feedback
        markUndeliveredView.setBackgroundResource(0)

        // Save the change in the database or shared preferences
        Toast.makeText(requireContext(), "Order marked as Undelivered", Toast.LENGTH_SHORT).show()
    }
}

