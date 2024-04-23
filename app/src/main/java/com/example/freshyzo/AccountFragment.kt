package com.example.freshyzo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.freshyzo.model.BottomNavVisibilityListener

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
    }

    override fun onPause() {
        super.onPause()
    }
}
