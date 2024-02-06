package com.example.freshyzo.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.freshyzo.onboarding.screens.OnboardingScreen


class ViewPagerAdapter(
    list: ArrayList<OnboardingScreen>,
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    private val fragmentList: ArrayList<OnboardingScreen> = list

    /*    For multiple fragments, change type of below ArrayList to <Fragment> here and use multiple Screen Constructors [ex- FirstScreen(), SecondScreen()..
          and so on in fragmentList arrayList in ViewPagerFragment */

    /*      private val fragmentList: ArrayList<Fragment> = list */

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
