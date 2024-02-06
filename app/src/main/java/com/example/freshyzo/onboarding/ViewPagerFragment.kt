package com.example.freshyzo.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.freshyzo.R
import com.example.freshyzo.onboarding.screens.OnboardingScreen

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)


        /* For multiple fragments, change type of below ArrayList to <Fragment> in ViewPagerAdapter
        and use multiple Screen Constructors [ex- FirstScreen(), SecondScreen()..
        and so on in the below fragment arrayList*/
        val fragmentList = arrayListOf(
            OnboardingScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.findViewById<ViewPager2>(R.id.viewPager).adapter = adapter
        return view
    }

}