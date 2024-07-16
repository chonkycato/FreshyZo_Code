package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class WalletFragment : Fragment() {

    private lateinit var mWalletBalanceTV : TextView
    private lateinit var mAddMoneyButton : Button

    // Step 1: Register for Activity Result
    private val paymentActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Step 4: Handle the result
        if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
            val paymentStatus = result.data?.getStringExtra("paymentStatus")
            Toast.makeText(requireContext(), "Payment Status: $paymentStatus", Toast.LENGTH_SHORT).show()

            // Handle different payment statuses
            if (paymentStatus == "success") {
                // Payment was successful, update wallet balance
                updateWalletBalance()
            } else {
                // Payment failed, handle accordingly
                Toast.makeText(requireContext(), "Payment Failed!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_wallet, container, false)

        mWalletBalanceTV = view.findViewById(R.id.wallet_balance)

        // Step 2: Set click listener for the payment button
        mAddMoneyButton = view.findViewById(R.id.add_money_button)
        mAddMoneyButton.setOnClickListener {
            val intent = Intent(requireContext(), PaymentActivity::class.java)
            paymentActivityResultLauncher.launch(intent)
        }

        return view
    }

    private fun updateWalletBalance() {
        Toast.makeText(requireContext(), "Wallet balance updated!", Toast.LENGTH_SHORT).show()
    }

}