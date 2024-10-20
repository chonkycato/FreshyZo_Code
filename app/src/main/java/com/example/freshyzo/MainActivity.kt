package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    private lateinit var cartIcon: ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar)
        toolbar = findViewById(R.id.toolbar)

        /** Setup Tool Bar as Action Bar **/
        setToolbarAsActionBar()

        /** Handle activity redirection **/
        activityRedirection(intent.getStringExtra("returned").toString())

        /** Handle Bottom Navigation **/

        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.visibility = View.VISIBLE

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

    private fun setToolbarAsActionBar(){
        setSupportActionBar(toolbar)

        // Customize Toolbar Appearance
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.body))
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
        toolbar.setTitleTextAppearance(this, R.style.Toolbar_TitleText)

        // Set up back button and cart icon
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.primary))

        //Handle Cart Icon clicks
        cartIcon = findViewById(R.id.cart_icon)
        cartIcon.setOnClickListener {
            toolbar.title = "Cart"
            loadFragment(CartFragment(), false, null)
        }
    }

    private fun checkOnBoarding() : Boolean{
        return getSharedPreferences("onBoarding", Context.MODE_PRIVATE).getBoolean("Finished", true)
    }

    private fun activityRedirection(activityName: String){
        when (activityName) {
            "home" -> loadFragment(HomeFragment(), false, null)
            "cart" -> loadFragment(AccountFragment(), true, null)
        }
    }

    private fun unInitialise(){
        getSharedPreferences("initialized", Context.MODE_PRIVATE).edit()
            .putBoolean("isInitialized", false).apply()
    }

    fun clearBackStack() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            val first = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun loadFragment(fragment: Fragment, clearBackStack: Boolean, searchQuery: String?){
        Log.d("FragmentLoading", "Loading fragment: ${fragment.javaClass.simpleName}")

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
        supportFragmentManager.popBackStack()
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

    fun handleNavigationToolbar(title: String? = null, bottomNavVisibility: Boolean) {

        if(title != null){
            toolbar.visibility = View.VISIBLE
            setSupportActionBar(toolbar) // Use instance method

            supportActionBar?.apply {
                setDisplayShowTitleEnabled(true) // Enable title text
                setDisplayHomeAsUpEnabled(true) // Show back button
            }

            toolbar.logo = null // Remove logo in non-Home fragments
            title?.let {
                toolbar.title = it // Set the title dynamically if provided
            } ?: run {
                toolbar.title = "Categories" // Default title if none is provided
            }
        } else{
            supportActionBar?.hide()
        }

        if(bottomNavVisibility){
            bottomNav.visibility = View.VISIBLE
        }
        else{
            bottomNav.visibility = View.GONE
        }
    }

    override fun onStop() {
        unInitialise()
        super.onStop()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val currentFragment: Fragment? = supportFragmentManager.fragments.lastOrNull()

        // Find the current fragment in the fragment container
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()

            // Check the current visible fragment and update the bottom nav selected item
            val lastFragment = supportFragmentManager.fragments.last()

            when (lastFragment) {
                is HomeFragment -> bottomNav.selectedItemId = R.id.home_icon
                is NotificationsFragment -> bottomNav.selectedItemId = R.id.notification_icon
                is WalletFragment -> bottomNav.selectedItemId = R.id.wallet_icon
                is AccountFragment -> bottomNav.selectedItemId = R.id.account_icon
                is CartFragment -> bottomNav.selectedItemId = R.id.cart_icon
            }
        } else {
            if (fragment is HomeFragment) {
                // If it's the HomeFragment, handle exit confirmation
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel()
                    finish() // Exit the app
                } else {
                    backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
                    backToast.show()
                }
                backPressedTime = System.currentTimeMillis()
            } else if (currentFragment is HomeFragment) {
                // Handle back press on HomeFragment (redundant but added for safety)
                bottomNav.selectedItemId = R.id.home_icon
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast.cancel()
                    finish() // Exit the app
                } else {
                    backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
                    backToast.show()
                }
                backPressedTime = System.currentTimeMillis()
            } else {
                // If the fragment isn't HomeFragment, pop the back stack
                supportFragmentManager.popBackStack()
            }
        }
    }

}
