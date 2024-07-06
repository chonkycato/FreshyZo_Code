package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.freshyzo.login.LoginFragment
import com.example.freshyzo.onboarding.ViewPagerFragment

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mainActivity: MainActivity = requireActivity() as MainActivity

        do {

            (mainActivity).setBottomNavVisibility(false)

            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(context, "HELLO", Toast.LENGTH_LONG).show()

                if (!onBoardingFinished()) {
                    Toast.makeText(context, "View Pager", Toast.LENGTH_SHORT).show()
                    (mainActivity).loadFragment(ViewPagerFragment(), true)
                } else {

                    if ((mainActivity).isLoggedIn()) {
                        Toast.makeText(
                            context,
                            mainActivity.isLoggedIn().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        (mainActivity).loadFragment(HomeFragment(), true)
                        // findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    } else if (!mainActivity.isLoggedIn()) {
                        Toast.makeText(
                            context,
                            (mainActivity).isLoggedIn().toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        (mainActivity).loadFragment(LoginFragment(), true)
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