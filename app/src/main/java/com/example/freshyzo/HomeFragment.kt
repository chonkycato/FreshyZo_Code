package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.model.DataModel
import com.example.freshyzo.model.RecyclerAdapter
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {

    private var carouselArray : ArrayList<Int> = ArrayList()
    private var carouselView: CarouselView? = null
    private lateinit var  recyclerAdapter: RecyclerAdapter
    private var dataList = mutableListOf<DataModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val mLogOutButton = view.findViewById<Button>(R.id.logoutButton)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        //Horizontal Scroll View
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


        recyclerView.layoutManager = GridLayoutManager(context,2)
        recyclerAdapter = RecyclerAdapter(requireContext())
        recyclerView.adapter = recyclerAdapter

        //add data
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))
        dataList.add(DataModel("FreshyZo Ghee Butter","200 ml","₹25",R.drawable.img_ghee))


        recyclerAdapter.setDataList(dataList)

        mLogOutButton.setOnClickListener{
            (activity as MainActivity).loginState(false)
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        return view
    }

    private var imageListener = ImageListener { position, imageView -> imageView.setImageResource(carouselArray[position]) }
}