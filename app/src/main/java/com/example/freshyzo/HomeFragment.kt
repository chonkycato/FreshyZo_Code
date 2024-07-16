package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.login.LoginFragment
import com.example.freshyzo.model.BottomNavVisibilityListener
import com.example.freshyzo.model.ButtonClickListener
import com.example.freshyzo.model.DataModelProduct
import com.example.freshyzo.model.RecyclerAdapterHome
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment(), ButtonClickListener {

    private var carouselArray : ArrayList<Int> = ArrayList()
    private var carouselView: CarouselView? = null

    private lateinit var recyclerView : RecyclerView
    private lateinit var  mRecyclerAdapterHome: RecyclerAdapterHome
    private var productDataList = mutableListOf<DataModelProduct>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
        val mLogOutButton = view.findViewById<Button>(R.id.logoutButton)
        Toast.makeText(context, (activity as MainActivity).isLoggedIn().toString(), Toast.LENGTH_SHORT).show()

        //Carousel View
        carouselView = view.findViewById(R.id.carouselView)
        carouselArray.add(R.drawable.carousel_one)
        carouselArray.add(R.drawable.carousel_two)
        carouselArray.add(R.drawable.carousel_three)
        carouselView!!.pageCount = carouselArray.size
        carouselView!!.setImageListener(imageListener)

        //add data
        productDataList.add(DataModelProduct("FreshyZo Ghee Butter","250 gms","₹350",R.drawable.img_ghee))
        productDataList.add(DataModelProduct("FreshyZo Cow Milk","200 ml","₹25",R.drawable.img_milk))
        productDataList.add(DataModelProduct("FreshyZo Buffalo Milk","200 ml","₹25",R.drawable.img_milk_buff))
        productDataList.add(DataModelProduct("FreshyZo Malai Dahi","250 ml","₹35",R.drawable.img_dahi))
        productDataList.add(DataModelProduct("FreshyZo Paneer","250 gms","₹250",R.drawable.img_paneer))

        //Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = GridLayoutManager(context,2)
//        recyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerAdapterHome = RecyclerAdapterHome(requireContext(), this)
        recyclerView.adapter = mRecyclerAdapterHome

        mRecyclerAdapterHome.onItemClick = {
            val intentHome = Intent(activity, ProductActivity::class.java)
            intentHome.putExtra("product", it)
            Toast.makeText(context, "Item clicked", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).startActivity(intentHome)
        }


        mRecyclerAdapterHome.setDataList(productDataList)

        mLogOutButton.setOnClickListener{
            (activity as MainActivity).changeLoginState(false)
//            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
            (activity as MainActivity).loadFragment(LoginFragment(), false)
        }
        return view
    }

    override fun onButtonClicked(position : Int, dataModelProduct: DataModelProduct){
        Log.d("Position", position.toString())
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
    }

    override fun onPause() {
        super.onPause()
    }

    private var imageListener = ImageListener { position, imageView -> imageView.setImageResource(carouselArray[position]) }
}