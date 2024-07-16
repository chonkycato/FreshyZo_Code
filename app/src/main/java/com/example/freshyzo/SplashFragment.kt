package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.freshyzo.login.LoginFragment
import com.example.freshyzo.onboarding.ViewPagerFragment

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).hideBottomNav()
        val mainActivity: MainActivity = requireActivity() as MainActivity

        do {

            Handler(Looper.getMainLooper()).postDelayed({

                if (!onBoardingFinished()) {
                    (mainActivity).loadFragment(ViewPagerFragment(), true, null)
                } else {

                    if ((mainActivity).isLoggedIn()) {
                        (mainActivity).loadFragment(HomeFragment(), true, null)
                        // findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    } else if (!mainActivity.isLoggedIn()) {
                        (mainActivity).loadFragment(LoginFragment(), true, null)
                        //  findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                }
            }, 1000)

            mainActivity.unInitialise()

            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_splash, container, false)

        } while (mainActivity.isInitialized())

    }


    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }


}