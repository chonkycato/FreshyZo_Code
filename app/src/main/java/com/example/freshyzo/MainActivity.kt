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

        /** Handle activity redirection **/
        val intentData = intent.getStringExtra("returned")
        if (intentData.equals("home")){
            loadFragment(HomeFragment(), true, null)
        }

        if (!checkOnBoarding()) {
            loadFragment(OnboardingScreen(), true, null)
        }

        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        //Bottom Navigation
        showBottomNav()
        //loadFragment(HomeFragment(), true)
        bottomNav.setOnItemSelectedListener {
            val homeIcon = R.id.home_icon
            val notificationIcon = R.id.notification_icon
            val walletIcon = R.id.wallet_icon
            val accountIcon = R.id.account_icon
            val cartIcon = R.id.cart_icon

            when (it.itemId){
                 homeIcon -> {
                    loadFragment(HomeFragment(), false, null)

                    true
                }

                notificationIcon -> {
                    loadFragment(NotificationsFragment(), false, null)
                    true
                }

                walletIcon -> {
                    loadFragment(WalletFragment(), false, null)
                    true
                }

                accountIcon -> {
                    loadFragment(AccountFragment(), false, null)
                    true
                }

                cartIcon -> {
                    loadFragment(CartFragment(), false, null)
                    true
                }

                else -> false
            }
        }
    }

    private fun checkOnBoarding() : Boolean{
        return getSharedPreferences("onBoarding", Context.MODE_PRIVATE).getBoolean("Finished", true)
    }

    fun unInitialise(){
        getSharedPreferences("initialized", Context.MODE_PRIVATE).edit()
            .putBoolean("isInitialized", false).apply()
    }

    fun loadFragment(fragment: Fragment, clearBackStack: Boolean, searchQuery: String?){

        val supportFragmentManager = supportFragmentManager
        val transaction = supportFragmentManager.beginTransaction()

        if(searchQuery!=null){
            val bundle = Bundle().apply{
                putString("Search Query", searchQuery)
            }
            fragment.arguments = bundle
        }

        transaction.replace(R.id.container, fragment)
        if(!clearBackStack){
            transaction.addToBackStack(null)
        }
        else{
            supportFragmentManager.popBackStackImmediate()
        }
        transaction.commit()
    }

    fun backNavigation(){
        val supportFragmentManager = supportFragmentManager
        supportFragmentManager?.popBackStack()
    }


    fun onBoardingFinished() {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("Finished", true).apply()
    }



    fun changeLoginState(loginState : Boolean) {
        val sharedPref = getSharedPreferences("loggedIn", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isLoggedIn", loginState).apply()
    }

    fun isLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("loggedIn", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    fun isInitialized(): Boolean {
        getSharedPreferences("initialized", Context.MODE_PRIVATE).edit()
            .putBoolean("isInitialized", true).apply()
        return true
    }

    fun generateOTP() : Long{
        return (Math.random() * 900000).toLong() + 100000
    }

    fun addToCart(productId: Int){
        val sharedPref = getSharedPreferences("cart", Context.MODE_PRIVATE)
        sharedPref.edit().putInt("productId", productId).apply()
        var value = sharedPref.getInt("productId", 0)
    }

    fun displayCartItems(): Int {
        val sharedPref = getSharedPreferences("cart", Context.MODE_PRIVATE)
        return sharedPref.getInt("productId", 0)
    }

    override fun setBottomNavVisibility(isVisible: Boolean) {
//        if (isVisible){
//            try{
//                bottomNav.visibility = View.VISIBLE
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
//        else{
//            try{
//                bottomNav.visibility = View.GONE
//            } catch (e: Exception){
//                e.printStackTrace()
//            }
//        }
    }

    fun hideBottomNav(){
        bottomNav.visibility = View.GONE
    }

    fun showBottomNav(){
        bottomNav.visibility = View.VISIBLE
    }

    override fun onStop() {
        unInitialise()
        super.onStop()
    }


}