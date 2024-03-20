package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [ProductFragment. newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        var backIcon = view.findViewById<ImageView>(R.id.back_icon)
        backIcon.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_productFragment_to_homeFragment)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        return view
    }

}