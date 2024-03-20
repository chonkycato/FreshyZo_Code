package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.freshyzo.login.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragments = fragmentManager.fragments


        bottomNav = findViewById(R.id.bottomNav)

        val currentFragment = fragments.firstOrNull { it.isVisible }

        if (currentFragment is ProductFragment) {
            bottomNav.visibility = View.INVISIBLE
        }
        else {
            //Bottom Navigation
            bottomNav.visibility = View.VISIBLE
            loadFragment(HomeFragment())
            bottomNav.setOnItemSelectedListener {
                when (it.itemId){
                    R.id.home_icon -> {
                        loadFragment(HomeFragment())
                        true
                    }

                    R.id.notification_icon -> {
                        loadFragment(NotificationsFragment())
                        true
                    }

                    R.id.account_icon -> {
                        loadFragment(LoginFragment())
                        true
                    }

                    R.id.cart_icon -> {
                        loadFragment(CartFragment())
                        true
                    }

                    else -> {
                        loadFragment(HomeFragment())
                        true
                    }
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()

    }

     fun onBoardingFinished() {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("Finished", true).apply()
    }

    fun loginState(loginState : Boolean) {
        val sharedPref = getSharedPreferences("loggedIn", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", loginState).apply()
    }

    fun loginState(): Boolean {
        val sharedPref = getSharedPreferences("loggedIn", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", false).apply()
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    fun generateOTP() : Long{
        return (Math.random() * 900000).toLong() + 100000
    }


}