package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.adapter.RecyclerAdapterOrderDetails
import com.example.freshyzo.model.DataModelOrderDetails

class OrderDetailsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterOrderDetails
    private var dataList = mutableListOf<DataModelOrderDetails>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_details, container, false)

        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar("Order Details", false)

        // Retrieve passed order details
        val orderID = arguments?.getString("orderID")
        Toast.makeText(requireContext(), orderID.toString(), Toast.LENGTH_SHORT).show()


        // Display the order details in the UI
        view.findViewById<TextView>(R.id.order_id).text = orderID

        dataList.add(DataModelOrderDetails(resources.getString(R.string.freshyzo_malai_dahi), R.drawable.img_dahi, "500 gm", "2"))
        dataList.add(DataModelOrderDetails(resources.getString(R.string.freshyzo_ghee_butter), R.drawable.img_ghee, "500 gm", "3"))

        recyclerView = view.findViewById(R.id.recyclerView_order_items)
        recyclerAdapter = RecyclerAdapterOrderDetails()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.setDataList(dataList)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // This will pop the back stack and navigate back to OrderFragment
                parentFragmentManager.popBackStack("OrderFragment", 0)
            }
        })
    }

}