package com.example.freshyzo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.freshyzo.model.BottomNavVisibilityListener
import com.example.freshyzo.model.LocaleHelper

class AccountFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_account, container, false)

        (activity as MainActivity).showBottomNav()

        val ordersLL = view.findViewById<LinearLayout>(R.id.my_orders)
        val subscriptionLL = view.findViewById<LinearLayout>(R.id.subscriptions)
        val vacationLL = view.findViewById<LinearLayout>(R.id.vacation)
        val walletLL = view.findViewById<LinearLayout>(R.id.payments_wallet)

        val profileLL = view.findViewById<LinearLayout>(R.id.profile)
        val addressesLL = view.findViewById<LinearLayout>(R.id.saved_addresses)
        val referLL = view.findViewById<LinearLayout>(R.id.refer_earn)
        val languageLL = view.findViewById<LinearLayout>(R.id.language_pref)
        val aboutLL = view.findViewById<LinearLayout>(R.id.about)

        val termsLL = view.findViewById<LinearLayout>(R.id.terms)
        val policyLL = view.findViewById<LinearLayout>(R.id.privacy_policy)
        val returnLL = view.findViewById<LinearLayout>(R.id.returns)

        val backNavIcon = view.findViewById<ImageView>(R.id.back_icon_account)



        /** Set-up back navigation **/
        backNavIcon.setOnClickListener { (activity as MainActivity).backNavigation() }

        /** New fragment **/

        ordersLL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(OrdersFragment(), false, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        subscriptionLL.setOnClickListener {
            try {
                val intent = Intent(requireActivity(), SubscriptionsActivity::class.java)
                (activity as MainActivity).startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        vacationLL.setOnClickListener{
            try{
                (activity as MainActivity).loadFragment(VacationFragment(), false, null)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        walletLL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(WalletFragment(), false, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        profileLL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(ProfileFragment(), false, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        addressesLL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(AddressFragment(), false, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        referLL.setOnClickListener {
            try {
                (activity as MainActivity).loadFragment(ReferEarnFragment(), false, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        /** Pop-up dialogs **/

        languageLL.setOnClickListener{
            languageSelectionDialog()
        }

        /** URLs to be opened in browser **/

        termsLL.setOnClickListener {
            try{
                val url = "https://freshyzo.com/view/terms_and_conditions.html"
                openInBrowser(url)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        policyLL.setOnClickListener {
            try{
                val url = "https://freshyzo.com/view/privacy_policy.html"
                openInBrowser(url)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        returnLL.setOnClickListener {
            try{
                val url = "https://freshyzo.com/view/refund_and_cancellation.html"
                openInBrowser(url)
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }

        aboutLL.setOnClickListener {
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

    private fun languageSelectionDialog() {
        val items = arrayOf("English (IN)", "Hindi")
        var selectedItem = 0

        AlertDialog.Builder(requireContext())
            .setTitle("Select Your Preferred Language")
            .setSingleChoiceItems(items, selectedItem){ _, which ->
                selectedItem = which
            }
            .setPositiveButton("APPLY"){ _, _ ->
                var selectedOption = "en"
                if(selectedItem.equals(0)){
                    selectedOption = "en"
                }
                else if(selectedItem.equals(1)){
                    selectedOption = "hi"
                }
                LocaleHelper.setLocale(requireContext(), selectedOption)
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)

            }
            .setNegativeButton("CANCEL", null)
            .show()

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