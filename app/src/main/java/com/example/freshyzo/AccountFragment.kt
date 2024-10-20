package com.example.freshyzo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.freshyzo.helper.LocaleHelper
import com.example.freshyzo.login.LoginFragment

class AccountFragment : Fragment() {

    private lateinit var mLogOutButton: Button
    private lateinit var customerName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_account, container, false)

        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar("Account", true)

        val ordersLL = view.findViewById<LinearLayout>(R.id.my_orders)
        val subscriptionLL = view.findViewById<LinearLayout>(R.id.subscriptions)
        val vacationLL = view.findViewById<LinearLayout>(R.id.vacation)
        val walletLL = view.findViewById<LinearLayout>(R.id.payments_wallet)

        val profileLL = view.findViewById<LinearLayout>(R.id.profile)
        val referLL = view.findViewById<LinearLayout>(R.id.refer_earn)
        val languageLL = view.findViewById<LinearLayout>(R.id.language_pref)
        val aboutLL = view.findViewById<LinearLayout>(R.id.about)

        val termsLL = view.findViewById<LinearLayout>(R.id.terms)
        val policyLL = view.findViewById<LinearLayout>(R.id.privacy_policy)
        val returnLL = view.findViewById<LinearLayout>(R.id.returns)

        val userGreeting = view.findViewById<TextView>(R.id.greetUser)
        mLogOutButton = view.findViewById(R.id.logout_button)

        customerName = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE).getString("customer_name", "").toString()
        userGreeting.text = getString(R.string.welcome_user, customerName)

        ordersLL.setOnClickListener {
            loadFragment(OrdersFragment())
        }

        subscriptionLL.setOnClickListener {
            val intent = Intent(requireActivity(), SubscriptionsActivity::class.java)
            startActivity(intent)
        }

        vacationLL.setOnClickListener{
            loadFragment(VacationFragment())
        }

        walletLL.setOnClickListener {
            loadFragment(WalletFragment())
        }

        profileLL.setOnClickListener {
            loadFragment(ProfileFragment())
        }

        referLL.setOnClickListener {
            loadFragment(ReferEarnFragment())
        }

        /** Language Selection **/
        languageLL.setOnClickListener{
            languageSelectionDialog()
        }


        /** URLs to be opened in browser **/
        termsLL.setOnClickListener {
            val url = "terms_and_conditions.html"
            openInBrowser(url)
        }

        policyLL.setOnClickListener {
            val url = "privacy_policy.html"
            openInBrowser(url)
        }

        returnLL.setOnClickListener {
            val url = "refund_and_cancellation.html"
            openInBrowser(url)
        }

        aboutLL.setOnClickListener {
            val url = "about.html"
            openInBrowser(url)
        }

        mLogOutButton.setOnClickListener {
            logOut()
        }

        return view
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

    private fun openInBrowser(urlEndpoint: String){
        val url = "https://freshyzo.com/view/$urlEndpoint"
        val urlIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(urlIntent)
    }

    private fun logOut(){
        val mainActivity = activity as? MainActivity

        mainActivity?.apply {
            changeLoginState(false)
            clearBackStack()  // Clear all fragments from the backstack
            loadFragment(LoginFragment(), true, null)  // Load LoginFragment and don't add it to the backstack
        }
    }

    private fun loadFragment(fragment: Fragment){
        (activity as MainActivity).loadFragment(fragment, false, null)
    }

}