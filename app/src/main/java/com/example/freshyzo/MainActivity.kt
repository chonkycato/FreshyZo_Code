package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.freshyzo.model.BottomNavVisibilityListener
import com.example.freshyzo.onboarding.screens.OnboardingScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavVisibilityListener {

    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!checkOnBoarding()) {
            loadFragment(OnboardingScreen(), true)
        }

        bottomNav = findViewById(R.id.bottomNav)

        //Bottom Navigation
        bottomNav.visibility = View.VISIBLE
        //loadFragment(HomeFragment(), true)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home_icon -> {
                    loadFragment(HomeFragment(), false)
                    true
                }

                R.id.notification_icon -> {
                    loadFragment(NotificationsFragment(), false)
                    true
                }

                R.id.account_icon -> {
                    loadFragment(ProductFragment(), false)
                    true
                }

                R.id.cart_icon -> {
                    loadFragment(CartFragment(), false)
                    true
                }

                else -> {
                    loadFragment(HomeFragment(), false)
                    true
                }
            }
        }
    }
//    }

    fun loadFragment(fragment: Fragment, clearBackStack: Boolean){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
        val fragmentManager = supportFragmentManager
        if (clearBackStack){
            fragmentManager.popBackStackImmediate()
        }
    }

    fun onBoardingFinished() {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("Finished", true).apply()
    }

    private fun checkOnBoarding() : Boolean{
        return getSharedPreferences("onBoarding", Context.MODE_PRIVATE).getBoolean("Finished", true)
    }

    fun changeLoginState(loginState : Boolean) {
        val sharedPref = getSharedPreferences("loggedIn", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", loginState).apply()
    }

    fun isLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("loggedIn", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    fun generateOTP() : Long{
        return (Math.random() * 900000).toLong() + 100000
    }

    override fun setBottomNavVisibility(isVisible: Boolean) {
        if (isVisible){
            try{
                bottomNav.visibility = View.VISIBLE
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        else{
            try{
                bottomNav.visibility = View.GONE
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}