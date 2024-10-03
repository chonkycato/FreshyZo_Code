package com.example.freshyzo

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.freshyzo.adapter.ViewPagerAdapterSub
import com.example.freshyzo.helper.OnItemClick

open class SubscriptionsActivity : AppCompatActivity(), OnItemClick {

    private lateinit var mTabLayout: com.google.android.material.tabs.TabLayout
    private lateinit var mViewPager: ViewPager
    private lateinit var mCartIcon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscriptions)

        /** Handle back navigation **/
        val backNavIcon = findViewById<Button>(R.id.back_icon_prod_subscription)
        backNavIcon.setOnClickListener { finish() }

        mTabLayout = findViewById(R.id.subscriptionsTabLayout)
        mViewPager = findViewById(R.id.subscriptionsViewPager)

        val viewPagerAdapter = ViewPagerAdapterSub(supportFragmentManager)
        viewPagerAdapter.addFragment(SubActiveFragment(), resources.getString(R.string.active))
        viewPagerAdapter.addFragment(SubPausedFragment(), resources.getString(R.string.paused))
        
        mViewPager.adapter = viewPagerAdapter
        mTabLayout.setupWithViewPager(mViewPager)
    }


    override fun onClick() {
        TODO("Not yet implemented")
    }
}