package com.example.freshyzo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.freshyzo.model.DataModelProduct

class SubscriptionSummaryFragment : Fragment() {
    private lateinit var productDetails: DataModelProduct
    private lateinit var productQuantity: String
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var subscriptionType: String
    private lateinit var lowBalanceAlertTV: TextView
    private lateinit var walletBalance: String
    private var productPriceValue: Double = 0.00
    private var totalPriceValue: Double = 0.00
    private var monthlySubTotal: Double = 0.00
    private var walletBalanceValue: Double = 500.00


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subscription_summary, container, false)

        val productNameTV = view.findViewById<TextView>(R.id.productNameText)
        val productSizeTV = view.findViewById<TextView>(R.id.productSizeText)
        val productPriceTV = view.findViewById<TextView>(R.id.productPriceText)
        val productQuantityTV = view.findViewById<TextView>(R.id.productQuantityText)
        val subscriptionTypeTV = view.findViewById<TextView>(R.id.subscriptionTypeText)
        val subStartDateTV = view.findViewById<TextView>(R.id.subscriptionStartText)
        val subEndDateTV = view.findViewById<TextView>(R.id.subscriptionEndText)
        val confirmSubscriptionButton = view.findViewById<Button>(R.id.confirmSubscriptionButton)
        lowBalanceAlertTV  = view.findViewById(R.id.insufficientBalanceWarning)

//        val walletBalanceTV = view.findViewById<TextView>(R.id.walletBalanceText)
//        val totalPriceTV = view.findViewById<TextView>(R.id.totalPriceText)


        // Retrieve data from arguments
        val arguments = arguments ?: throw IllegalStateException("Arguments are missing")
        productDetails = arguments.getParcelable("productDetails")
            ?: throw IllegalStateException("Product details are missing")

        val productName = productDetails.title
        val productSize = productDetails.size
        val productPrice = productDetails.price
        val productQuantity = arguments.getInt("quantity")
        subscriptionType = arguments.getString("subscriptionType") ?: throw IllegalStateException("Subscription type is missing")
        startDate = arguments.getString("startDate") ?: throw IllegalStateException("Start date is missing")
        endDate = arguments.getString("endDate") ?: ("")
        if (arguments.getString("endDate").equals("")){
            endDate = "Not provided"
        }

        // Parse the price as double (assuming it's a string)
        walletBalance = "₹200"

        // Remove non-numeric characters (like "₹") from the string before parsing
//        walletBalanceValue = walletBalance.replace("[^\\d.]".toRegex(), "").toDouble()
        productPriceValue = productPrice.replace("[^\\d.]".toRegex(), "").toDouble()

        /** Calculate total **/
        totalPriceValue = productPriceValue * productQuantity
        monthlySubTotal = totalPriceValue * 30

        productNameTV.text = productName
        productQuantityTV.text = productQuantity.toString()
        productSizeTV.text = productSize
        productPriceTV.text = productPrice
        subscriptionTypeTV.text = subscriptionType
        subStartDateTV.text = startDate
        subEndDateTV.text = endDate
//        walletBalanceTV.text = walletBalanceValue.toString()
//        totalPriceTV.text = totalPriceValue.toString()

        confirmSubscriptionButton.setOnClickListener {
                checkAndProceedWithSubscription()
        }

        /** Handle the onBackPressed callback **/
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Prepare the bundle with productDetails and other data
                val bundle = Bundle().apply {
                    putSerializable("productDetails", productDetails)
                }

                // Navigate back to SubscribeProductFragment
                val subscribeProductFragment = SubscribeProductFragment()
                subscribeProductFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.productFragmentContainerView, subscribeProductFragment)
                    .addToBackStack(null)  // Add to back stack to handle navigation properly
                    .commit()
            }
        })

        return view
    }

    private fun checkAndProceedWithSubscription() {

        val dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        val message1: TextView = dialogView.findViewById(R.id.message1)
        val message2: TextView = dialogView.findViewById(R.id.message2)
        val messageStringOne = "Amount to pay: ₹$totalPriceValue"
        val messageStringTwo = "We recomment a recharge of ₹$monthlySubTotal to continue with your subscription for the next 30 days."

        message2.text = messageStringTwo
        message1.text = messageStringOne

        if (walletBalanceValue >= totalPriceValue) {
            AlertDialog.Builder(requireContext())
                .setTitle("Note")
                .setView(dialogView)
                .setPositiveButton("OK") { dialog, _ ->
                    showSubscriptionSuccessDialog()
                }
                .create()
                .show()

        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Insufficient Balance")
                .setView(dialogView)
                .setPositiveButton("OK") { dialog, _ ->
                    navigateToWalletFragment()
                }
                .create()
                .show()
        }
    }

    private fun showSubscriptionSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Subscription Successful")
            .setMessage("Your subscription has been successfully processed!")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun navigateToWalletFragment() {
        lowBalanceAlertTV.visibility = View.VISIBLE
        AlertDialog.Builder(requireContext())
            .setTitle("Insufficient Balance")
            .setMessage("You need to recharge your wallet.")
            .setPositiveButton("Recharge") { _, _ ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.productFragmentContainerView, WalletFragment())
                    .addToBackStack(null)
                    .commit()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
