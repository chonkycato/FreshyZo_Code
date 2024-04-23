package com.example.freshyzo

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
import com.example.freshyzo.model.DataModelHome
import com.example.freshyzo.model.RecyclerAdapter
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment(), ButtonClickListener {

    private var carouselArray : ArrayList<Int> = ArrayList()
    private var carouselView: CarouselView? = null

    private lateinit var  recyclerAdapter: RecyclerAdapter
    private var dataList = mutableListOf<DataModelHome>()
    private lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
        val mLogOutButton = view.findViewById<Button>(R.id.logoutButton)
        Toast.makeText(context, (activity as MainActivity).isLoggedIn().toString(), Toast.LENGTH_SHORT).show()


        //Populate Horizontal Scroll View dynamically
//        val imageList = listOf<Int>(R.drawable.img_milk, R.drawable.img_ghee, R.drawable.img_milk_buff, R.drawable.img_paneer)
//        val textList = listOf<Int>(R.string.hvs_item_text_1, R.string.hvs_item_text_2, R.string.hvs_item_text_3, R.string.hvs_item_text_4)
//        val itemContainer = view.findViewById<LinearLayout>(R.id.hvs_container)
//
//        for(i in imageList.indices){
//            val itemLayout = LayoutInflater.from(context).inflate(R.layout.fragment_home, itemContainer, false) as ConstraintLayout
//            val itemImageView = itemLayout.findViewById<ImageView>(R.id.item_image_hsv)
//            val itemTextView = itemLayout.findViewById<TextView>(R.id.item_text_hsv)
//
//            itemImageView.setImageResource(imageList[i])
//            itemTextView.setText(textList[i])
//
//            itemContainer.addView(itemLayout)
//        }

        //Carousel View
        carouselView = view.findViewById(R.id.carouselView)
        carouselArray.add(R.drawable.carousel_one)
        carouselArray.add(R.drawable.carousel_two)
        carouselArray.add(R.drawable.carousel_three)
        carouselView!!.pageCount = carouselArray.size
        carouselView!!.setImageListener(imageListener)



        //add data
        dataList.add(DataModelHome("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModelHome("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModelHome("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModelHome("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModelHome("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModelHome("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))

        //Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewHome)
        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerAdapter = RecyclerAdapter(requireContext(), this)
        recyclerView.adapter = recyclerAdapter


        recyclerAdapter.setDataList(dataList)

        mLogOutButton.setOnClickListener{
            (activity as MainActivity).changeLoginState(false)
//            findNavController().navigate(R.id.action_homeFragment_to_productFragment)
            (activity as MainActivity).loadFragment(LoginFragment(), false)
        }
        return view
    }

    override fun onButtonClicked(position : Int, dataModelHome: DataModelHome){
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