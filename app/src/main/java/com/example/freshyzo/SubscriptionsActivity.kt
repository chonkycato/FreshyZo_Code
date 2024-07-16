package com.example.freshyzo

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.freshyzo.model.DataModelPausedSub
import com.example.freshyzo.model.OnItemClick
import com.example.freshyzo.model.ViewPagerAdapterSub

open class SubscriptionsActivity : AppCompatActivity(), OnItemClick {

    private lateinit var mTabLayout: com.google.android.material.tabs.TabLayout
    private lateinit var mViewPager: ViewPager
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


    open fun showAlertDialog(alertTitle: String, it: DataModelPausedSub, view: View){

        val productTitle = it.productTitle
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.CustomAlertDialogStyle)
        val title = view.findViewById<TextView>(resources.getIdentifier("alertTitle", "id", "android"))
        title?.setTextAppearance(R.style.DialogTitleStyle)
        val message = view.findViewById<TextView>(resources.getIdentifier("message", "id", "android"))
        message?.setTextAppearance(R.style.DialogMessageStyle)

        alertDialogBuilder.setTitle("$alertTitle Subscription?")
        alertDialogBuilder.setMessage("Are you sure you want to $alertTitle subscription for $productTitle?" )
        alertDialogBuilder.setPositiveButton("NO"){ dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("YES"){ dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()

    }


    override fun onClick() {
        TODO("Not yet implemented")
    }
}