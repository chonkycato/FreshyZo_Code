package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener

class HomeFragment : Fragment() {

    private var carouselArray : ArrayList<Int> = ArrayList()
    private var carouselView: CarouselView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val mLogOutButton = view.findViewById<Button>(R.id.logoutButton)

        carouselArray.add(R.drawable.carousel_one)
        carouselArray.add(R.drawable.carousel_two)
        carouselArray.add(R.drawable.carousel_three)

        carouselView = view.findViewById(R.id.carouselView)

        carouselView!!.pageCount = carouselArray.size
        carouselView!!.setImageListener(imageListener)

        mLogOutButton.setOnClickListener{
            (activity as MainActivity).loginState(false)
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        return view
    }

    private var imageListener = ImageListener { position, imageView -> imageView.setImageResource(carouselArray[position]) }
}