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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.adapter.RecyclerAdapterCart
import com.example.freshyzo.helper.CartManager

class CartFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterCart
    private var dataList = mutableListOf<com.example.freshyzo.model.Cart>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar("Cart", true)

        val mSubscriptions = view.findViewById<Button>(R.id.subscription_cart)
        val mChangeAddressButton = view.findViewById<TextView>(R.id.changeAddressButton)

        // Retrieve cart products from Cart object
        val cartProd = CartManager.getCartItems()

        // Map cart products to DataModelCart (adjust according to your actual model)
        dataList.clear()
        cartProd.forEach { product ->
            dataList.add(
                com.example.freshyzo.model.Cart(
                    title = product.title,
                    itemDetail =  product.description,
                    itemPrice = product.price.toString(),
                    itemSize = product.size,
                    itemQty = product.quantity.toString(),
                    image = product.image
                )
            )
        }

        /** Navigate to subscriptions **/
        mSubscriptions.setOnClickListener {
            startActivity(Intent(activity, SubscriptionsActivity::class.java))
        }

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
            Toast.makeText(context, "Item clicked CART", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).startActivity(intentCart)
        }

        mChangeAddressButton.setOnClickListener {
            (activity as MainActivity).loadFragment(AddressFragment(), false, null)
        }

        return view
    }

    override fun onPause() {
        super.onPause()
    }
}
