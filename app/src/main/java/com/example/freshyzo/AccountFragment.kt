package com.example.freshyzo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.car.ui.AlertDialogBuilder
import com.example.freshyzo.model.BottomNavVisibilityListener

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val ordersRL = view.findViewById<RelativeLayout>(R.id.my_orders_rl)
        val subscriptionRL = view.findViewById<RelativeLayout>(R.id.subscriptions_rl)
        val walletRL = view.findViewById<RelativeLayout>(R.id.payments_wallet_rl)

        val profileRL = view.findViewById<RelativeLayout>(R.id.profile_rl)
        val addressesRL = view.findViewById<RelativeLayout>(R.id.saved_addresses_rl)
        val referRL = view.findViewById<RelativeLayout>(R.id.refer_earn_rl)
        val languageRL = view.findViewById<RelativeLayout>(R.id.language_pref_rl)

        val termsRL = view.findViewById<RelativeLayout>(R.id.terms_rl)
        val policyRL = view.findViewById<RelativeLayout>(R.id.privacy_policy_rl)
        val returnRL = view.findViewById<RelativeLayout>(R.id.returns_rl)
        val aboutRL = view.findViewById<RelativeLayout>(R.id.about_rl)


        /** New fragment **/

        ordersRL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(OrdersFragment(), false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        subscriptionRL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(SubscriptionFragment(), false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        walletRL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(WalletFragment(), false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        profileRL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(ProfileFragment(), false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        addressesRL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(AddressFragment(), false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        referRL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(ReferEarnFragment(), false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        /** Pop-up dialogs **/

        languageRL.setOnClickListener {
            var checkedItem = 1
            val items = arrayOf("English", "Hindi")


            AlertDialogBuilder(requireContext())
                .setTitle("Language")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK") { _, _ ->
                    when (checkedItem) {
                        0 -> {
                            Toast.makeText(context, "English", Toast.LENGTH_SHORT).show()
                        }

                        1 -> {
                            Toast.makeText(context, "Hindi", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            Toast.makeText(context, "English", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setSingleChoiceItems(items, checkedItem) { _, selectedItemIndex ->
                    checkedItem = selectedItemIndex
                }
                .setCancelable(false)
                .show()
        }

        /** URLs to be opened in browser **/

        termsRL.setOnClickListener {
            try{
                val url = "https://freshyzo.com/view/terms_and_conditions.html"
                openInBrowser(url)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        policyRL.setOnClickListener {
            try{
                val url = "https://freshyzo.com/view/privacy_policy.html"
                openInBrowser(url)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        returnRL.setOnClickListener {
            try{
                val url = "https://freshyzo.com/view/refund_and_cancellation.html"
                openInBrowser(url)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        aboutRL.setOnClickListener {
            try{
                val url = "https://freshyzo.com/view/about.html"
                openInBrowser(url)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as BottomNavVisibilityListener).setBottomNavVisibility(true)
    }

    override fun onPause() {
        super.onPause()
    }

    private fun openInBrowser(url: String){
        val url = url
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(urlIntent)
    }
}