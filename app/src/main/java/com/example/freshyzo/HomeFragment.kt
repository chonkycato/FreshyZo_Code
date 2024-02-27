package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val mLogOutButton = view.findViewById<Button>(R.id.logoutButton)

        mLogOutButton.setOnClickListener{
            (activity as MainActivity).loginState(false)
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        return view
    }
}