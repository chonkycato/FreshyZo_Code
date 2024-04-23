package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.freshyzo.model.BottomNavVisibilityListener

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
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(false)

        var backIcon = view.findViewById<ImageView>(R.id.back_icon_prod)
        /* TODO("implement a proper back navigation") */
        backIcon.setOnClickListener {
            try {
//                findNavController().navigate(R.id.action_productFragment_to_homeFragment)
                (activity as MainActivity).loadFragment(HomeFragment(), false)
            }
            catch (e : Exception){
                e.printStackTrace()
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(false)
    }

    override fun onPause() {
        super.onPause()
    }

}