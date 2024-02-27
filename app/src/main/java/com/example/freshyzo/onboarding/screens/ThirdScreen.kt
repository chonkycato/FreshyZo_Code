package com.example.freshyzo.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.freshyzo.MainActivity
import com.example.freshyzo.R


class ThirdScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third_screen, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        //Load GIF into ImageView
        val imageView = view.findViewById<ImageView>(R.id.gif_fts)
        Glide.with(this)
            .load(R.drawable.gif_onboarding_three)
            .override(830, 726)
            .into(imageView)

        view.findViewById<TextView>(R.id.back_fts).setOnClickListener {
            viewPager?.currentItem = 1
        }

        view.findViewById<TextView>(R.id.finish_fts).setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
            (activity as MainActivity?)!!.onBoardingFinished()
        }
        return view
    }

}