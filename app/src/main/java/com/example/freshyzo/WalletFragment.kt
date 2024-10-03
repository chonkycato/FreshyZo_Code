package com.example.freshyzo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class WalletFragment : Fragment() {

    private lateinit var mWalletBalanceTV: TextView
    private lateinit var mAddMoneyButton: Button
    private lateinit var mThousandButton: Button
    private lateinit var mTwoThousandButton: Button
    private lateinit var mThreeThousandButton: Button
    private lateinit var mFiveThousandButton: Button
    private lateinit var mRechargeAmountEditText: EditText
    private lateinit var mRechargeButton: Button
    private lateinit var mCustomerID: String

    /** Step 1: Register for Activity Result **/
    private val paymentActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
    /** Step 4: Handle the result **/
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

        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar("Wallet", true)


        /** Initialise member variables **/
        mWalletBalanceTV = view.findViewById(R.id.wallet_balance)
        mThousandButton = view.findViewById(R.id.thousandBtn)
        mTwoThousandButton = view.findViewById(R.id.twoThousandBtn)
        mThreeThousandButton = view.findViewById(R.id.threeThousandBtn)
        mFiveThousandButton = view.findViewById(R.id.fiveThousandBtn)
        mRechargeAmountEditText = view.findViewById(R.id.recharge_amount_editText)
        mRechargeButton = view.findViewById(R.id.rechargeButton)

        val buttonAmountMap = mapOf(
            mThousandButton to 1000,
            mTwoThousandButton to 2000,
            mThreeThousandButton to 3000,
            mFiveThousandButton to 5000
        )

        // List of all buttons for easier processing
        val buttonList = buttonAmountMap.keys.toList()

        for(button in buttonList){
            button.setOnClickListener {
                val rechargeAmount = buttonAmountMap[button] ?: 0
                mRechargeAmountEditText.text = Editable.Factory.getInstance().newEditable(rechargeAmount.toString())
                updateButtonSelection(button, buttonList)
            }
        }

        /** Step 2: Set click listener for the payment button **/
        mAddMoneyButton = view.findViewById(R.id.add_money_button)
        mAddMoneyButton.setOnClickListener {
            val intent = Intent(requireContext(), PaymentActivity::class.java)
            paymentActivityResultLauncher.launch(intent)
        }

        mRechargeButton.setOnClickListener {
            mCustomerID = "123"
            val rechargeAmount = mRechargeAmountEditText.text.toString()
            if (rechargeAmount.isNotEmpty()){
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("customerID", mCustomerID)
                intent.putExtra("rechargeAmount", rechargeAmount)
                paymentActivityResultLauncher.launch(intent)
            }
            else{
                mRechargeAmountEditText.error = "Please enter an amount"
            }

        }

        return view
    }

    private fun updateWalletBalance() {
        Toast.makeText(requireContext(), "Wallet balance updated!", Toast.LENGTH_SHORT).show()
    }

    private fun updateButtonSelection(selectedButton: Button, buttonList: List<Button>) {
        for (button in buttonList) {
            if (button == selectedButton) {
                // Set the selected button background and text color
                button.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.custom_rounded_corner_primary
                )
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
            } else {
                // Set the other buttons to the default background and text color
                button.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.custom_rounded_corner_primary_outline
                )
                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            }
        }

    }
}