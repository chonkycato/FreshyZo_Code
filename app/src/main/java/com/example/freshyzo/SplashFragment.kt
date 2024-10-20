package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.freshyzo.HomeFragment
import com.example.freshyzo.MainActivity
import com.example.freshyzo.R
import com.example.freshyzo.login.LoginFragment
import com.example.freshyzo.onboarding.ViewPagerFragment

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).handleNavigationToolbar(null, false)

        val mainActivity = requireActivity() as MainActivity

        Handler(Looper.getMainLooper()).postDelayed({

            when {
                !onBoardingFinished() -> {
                    mainActivity.loadFragment(ViewPagerFragment(), clearBackStack = true, searchQuery = null)
                }
                mainActivity.isLoggedIn() -> {
                    mainActivity.loadFragment(HomeFragment(), clearBackStack = true, searchQuery = null)
                }
                else -> {
                    mainActivity.loadFragment(LoginFragment(), clearBackStack = true, searchQuery = null)
                }
            }
        }, 300)

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref!!.getBoolean("Finished", false)
    }
}
