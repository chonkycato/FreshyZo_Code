package com.example.freshyzo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.adapter.RechargeAdapter
import com.example.freshyzo.api.ApiHandler
import com.example.freshyzo.model.RechargeHistoryResponse
import com.example.freshyzo.model.WalletResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WalletFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RechargeAdapter
    private lateinit var mEmptyTransHistory: TextView
    private lateinit var mWalletBalanceTV: TextView
    private lateinit var mThousandButton: Button
    private lateinit var mTwoThousandButton: Button
    private lateinit var mThreeThousandButton: Button
    private lateinit var mFiveThousandButton: Button
    private lateinit var mRechargeAmountEditText: EditText
    private lateinit var mRechargeButton: Button
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mCustomerID: String
    private lateinit var mCustomerRole: String

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

        /** Retrieve User Data from Shared Preferences **/
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        mCustomerID = sharedPref.getString("customer_id", "").toString()
        mCustomerRole = sharedPref.getString("customer_role", "").toString()

        /** Initialise member variables **/
        mWalletBalanceTV = view.findViewById(R.id.wallet_balance)
        mThousandButton = view.findViewById(R.id.thousandBtn)
        mTwoThousandButton = view.findViewById(R.id.twoThousandBtn)
        mThreeThousandButton = view.findViewById(R.id.threeThousandBtn)
        mFiveThousandButton = view.findViewById(R.id.fiveThousandBtn)
        mRechargeAmountEditText = view.findViewById(R.id.recharge_amount_editText)
        mRechargeButton = view.findViewById(R.id.rechargeButton)
        mProgressBar = view.findViewById(R.id.progressBarWallet)
        mEmptyTransHistory = view.findViewById(R.id.emptyHistoryTV)


        /** Fetch Wallet Balance **/
        fetchWalletBalance()

        /** Fetch Recharge History **/
        fetchRechargeHistory()

        /** Setup textWatcher on mRechargeAmountEditText**/
        setTextWatcher()

        /** Initialise Recycler View **/
        recyclerView = view.findViewById(R.id.transactions_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
//                updateButtonSelection(button, buttonList)
            }
        }

        /** Step 2: Set click listener for the payment button **/
//        mAddMoneyButton = view.findViewById(R.id.add_money_button)
//        mAddMoneyButton.setOnClickListener {
//            val intent = Intent(requireContext(), PaymentActivity::class.java)
//            paymentActivityResultLauncher.launch(intent)
//        }

        mRechargeButton.setOnClickListener {
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

//    private fun updateButtonSelection(selectedButton: Button, buttonList: List<Button>) {
//        for (button in buttonList) {
//            if (button == selectedButton) {
//                // Set the selected button background and text color
//                button.background = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.custom_rounded_corner_primary
//                )
//                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
//            } else {
//                // Set the other buttons to the default background and text color
//                button.background = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.custom_rounded_corner_primary_outline
//                )
//                button.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
//            }
//        }
//    }

    /** Fetch Wallet Balance **/
    private fun fetchWalletBalance() {
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val customerID = sharedPref.getString("customer_id", null)
        val customerRole = sharedPref.getString("customer_role", null)
        Log.d("Customer Details Wallet", "$customerID $customerRole")

        val apiHandler = ApiHandler()
        if (customerRole != null && customerID != null) {
            apiHandler.fetchWalletBalance(customerID, customerRole, object :
                Callback<List<WalletResponse>> {
                override fun onResponse(call: Call<List<WalletResponse>>, response: Response<List<WalletResponse>>) {
                    val walletResponse = response.body()

                    // Check if the response body is not null and contains at least one element
                    if (response.isSuccessful && !walletResponse.isNullOrEmpty()) {
                        // Extract the balance_amount from the first item
                        val balanceAmount = walletResponse[0].balance_amount
                        setWalletBalance(balanceAmount)
                    } else {
                        Log.e("Wallet Error", "Failed to fetch Wallet Balance or empty response")
                        Toast.makeText(context, "Failed to fetch Wallet Balance", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<WalletResponse>>, t: Throwable) {
                    Log.e("Failed to fetch Wallet Balance", "Request failed: ${t.message}")
                    Toast.makeText(context, "Wallet Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.d("Wallet Balance", "Failed to fetch wallet balance. CustomerID/Role is null")
        }
    }

    /** Set wallet balance **/
    private fun setWalletBalance(amount: String) {
        val balanceAmount = getString(R.string.money, amount)
        mWalletBalanceTV.text = balanceAmount
    }

    /** Fetch Recharge History**/
    private fun fetchRechargeHistory(){
        val apiHandler = ApiHandler()
        mProgressBar.visibility = View.VISIBLE
        if (mCustomerID.isNotEmpty() && mCustomerRole.isNotEmpty()) {
            apiHandler.fetchRechargeHistory("745", "customer", object : ApiHandler.RechargeListCallback {
                    override fun onSuccess(rechargeList: List<RechargeHistoryResponse>) {
                        mProgressBar.visibility = View.GONE
                        Log.d("Recharge List Size", rechargeList.size.toString())
                        if (rechargeList.isEmpty()){
                            mEmptyTransHistory.visibility = View.VISIBLE
                        }
                        else{
                            mEmptyTransHistory.visibility = View.GONE
                            recyclerAdapter = RechargeAdapter(rechargeList)
                            recyclerView.adapter = recyclerAdapter
                            // Handle the success and use the rechargeList
                            for (recharge in rechargeList) {
                                Log.d(
                                    "Recharge Details: ", "Recharge ID: ${recharge.rechargeID}, " +
                                            "Amount: ${recharge.rechargeAmount}, Recharge Date: ${recharge.rechargeDate} "
                                )
                            }
                        }
                    }
                    override fun onFailure(message: String) {
                        // Handle the error
                        mProgressBar.visibility = View.GONE
                        Log.d("Failed to fetch recharge history: ", "$message")
                    }
                })
        }
    }

    /** Add TextWatcher to EditText to monitor changes **/
    private fun setTextWatcher(){

        mRechargeAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Check if the EditText is empty or not
                if (s.isNullOrEmpty()) {
                    // Set the button's background and text color to the "empty" state
                    mRechargeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_tint_light))
                    mRechargeButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_small_rounded_corner_primary)
                    mRechargeButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary_tint)
                } else {
                    // Set the button's background and text color to the "filled" state
                    mRechargeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
                    mRechargeButton.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_small_rounded_corner_primary)
                    mRechargeButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.primary)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed in this case
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed in this case
            }
        })
    }

}