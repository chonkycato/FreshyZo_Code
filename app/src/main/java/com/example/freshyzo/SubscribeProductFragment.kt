package com.example.freshyzo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.freshyzo.model.DataModelProduct
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SubscribeProductFragment : Fragment() {

    private lateinit var view: View
    private lateinit var mStartDateET: EditText
    private lateinit var mEndDateET: EditText
    private lateinit var mSpinner: Spinner
    private lateinit var productDetails: DataModelProduct
    private lateinit var prodQty: String
    private var startDate: Calendar? = null
    private var endDate: Calendar? = null
    private var spinnerPosition: Int = 0

    private lateinit var walletBalance: String
    private var productSize: Double = 0.00
    private var productPriceValue: Double = 0.00
    private var totalPriceValue: Double = 0.00
    private var monthlySubTotal: Double = 0.00
    private var walletBalanceValue: Double = 100.00

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subscribe_product, container, false)
        this.view = view

        /** Declare variables **/
        val mDeliveryTypeArray = resources.getStringArray(R.array.DeliveryTypeArray)
        val mQtyDecrementBtn = view.findViewById<Button>(R.id.quantity_decrement_btn)
        val mQtyIncrementBtn = view.findViewById<Button>(R.id.quantity_increment_btn)
        val mQtyDisplayTV = view.findViewById<TextView>(R.id.quantity_display_tv)
        val mSubscribeBtn = view.findViewById<Button>(R.id.subscribeBtnSub)
        val mWeeklySub = view.findViewById<Button>(R.id.weekly_sub)
        val mMonthlySub = view.findViewById<Button>(R.id.monthly_sub)

        mStartDateET = view.findViewById(R.id.startDatePickerET)
        mEndDateET = view.findViewById(R.id.endDatePickerET)
        mSpinner = view.findViewById(R.id.deliveryTypeSpinner)

        /** Get product details from arguments **/
        productDetails = arguments?.getSerializable("productDetails") as DataModelProduct
        val productName = productDetails.title
        val productSize = productDetails.size
        val productPrice = productDetails.price


        /** Handle the onBackPressed callback **/
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Prepare the bundle with productDetails and other data
                val intent = Intent(activity, ProductActivity::class.java).apply {
                    putExtra("product", productDetails?: "null")
                }
                startActivity(intent)
                activity?.finish() // Finish the fragment activity to avoid stacking
            }
        })

        /** Change Subscription Type **/
        mWeeklySub.setOnClickListener { Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show() }
        mMonthlySub.setOnClickListener { Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show() }

        /** Daily Subscription**/
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            R.layout.custom_spinner_layout, mDeliveryTypeArray
        )
        mSpinner.adapter = spinnerAdapter
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        mSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                val textView = view as TextView
                if (position == 0) {
                    textView.setTextColor(resources.getColor(R.color.disabled))
                    spinnerPosition = 0
                } else {
                    spinnerAdapter.notifyDataSetChanged()
                    spinnerPosition = position
                    textView.setTextColor(resources.getColor(R.color.body))
                    Toast.makeText(
                        requireContext(),
                        "Selected : " + mDeliveryTypeArray[position],
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(
                    context,
                    "Please select delivery type to continue!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } //End of listener

        /** Remove non-numeric characters (like "₹") from the string before parsing **/
        productPriceValue = productPrice.replace("[^\\d.]".toRegex(), "").toDouble()
        Log.d("Prices", "$productPriceValue $productPrice $totalPriceValue")

        //End of if block

        /**Product Quantity**/

        var productQuantity = 1
        prodQty = productQuantity.toString()

        mQtyDecrementBtn.setOnClickListener {
            if (productQuantity != 0 ){
                productQuantity -= 1
                prodQty = productQuantity.toString()
                mQtyDisplayTV.text = productQuantity.toString()
            }
        }

        mQtyIncrementBtn.setOnClickListener {
            productQuantity += 1
            prodQty = productQuantity.toString()
            mQtyDisplayTV.text = productQuantity.toString()
        }

        /** Calculate total **/
        totalPriceValue = productPriceValue * productQuantity
        monthlySubTotal = totalPriceValue * 30


        Log.d("Prices", "$productPriceValue $productPrice $totalPriceValue $monthlySubTotal")

        mStartDateET.setOnClickListener {
            showStartDatePicker()
        }

        mEndDateET.setOnClickListener {
            showEndDatePicker()
        }

        mSubscribeBtn.setOnClickListener {
            if(spinnerPosition==0){
                Toast.makeText(context, "Please select the delivery type!", Toast.LENGTH_SHORT).show()
                mSpinner.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.custom_rounded_corner_alert_transparent)
            } else {
                checkAndProceedWithSubscription()
//                handleSubscription()
            }
        }

        return view
    } //End of OnCreateView

    private fun handleSubscription() {
        val selectedSubscriptionType = mSpinner.selectedItem.toString()
        val bundle = Bundle().apply {
            putParcelable("productDetails", productDetails)  // Use putParcelable
            putInt("quantity", prodQty.toInt())
            putString("startDate", formatDate(startDate))
            putString("endDate", formatDate(endDate))
            putString("subscriptionType", selectedSubscriptionType)
        }


        val summaryFragment = SubscriptionSummaryFragment().apply {
            arguments = bundle
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.productFragmentContainerView, summaryFragment)
            .addToBackStack(null)
            .commit()
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

    private fun showStartDatePicker() {
        val calendar = Calendar.getInstance()

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)

                    startDate = selectedDate // Store selected start date

                    val mDate = "${dayOfMonth}-${monthOfYear + 1}-${year}"
                    mStartDateET.setText(mDate)

                    // Optional: Clear end date if it's invalid now
                    if (endDate != null && endDate!!.before(startDate)) {
                        endDate = null
                        mEndDateET.setText("") // Clear the end date field if it was set earlier
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        datePickerDialog?.datePicker?.minDate = calendar.timeInMillis // Disable past dates
        datePickerDialog?.show()
    }

    private fun showEndDatePicker() {
        if (startDate == null) {
            // Show message or set a default start date before selecting the end date
            mStartDateET.error = "Please select a start date first."
            return
        }

        val calendar = startDate ?: Calendar.getInstance() // Set the calendar to startDate if available

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)

                    // Ensure end date is after start date
                    if (selectedDate.before(startDate)) {
                        mEndDateET.error = "End date cannot be before or the same as start date."
                    } else {
                        endDate = selectedDate // Store valid end date
                        val mDate = "${dayOfMonth}-${monthOfYear + 1}-${year}"
                        mEndDateET.setText(mDate)
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        // Set minDate for endDate as the day after the start date
        datePickerDialog?.datePicker?.minDate = startDate!!.timeInMillis + (24 * 60 * 60 * 1000) // Add 1 day
        datePickerDialog?.show()
    }

    private fun formatDate(date: Calendar?): String {
        // Define the date format pattern
        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

        // Ensure startDate is not null, otherwise return an empty string
        return if (date != null) {
            dateFormat.format(date.time)  // .time returns the Date object from Calendar
        } else {
            ""
        }
    }

}

/** Weekly and Monthly Subscription Logic ** /

//Weekly Sub Layout

//    val mSundayBtn = view.findViewById<Button>(R.id.sundayBtn)
//    val mMondayBtn = view.findViewById<Button>(R.id.mondayBtn)
//    val mTuesdayBtn = view.findViewById<Button>(R.id.tuesdayBtn)
//    val mWednesdayBtn = view.findViewById<Button>(R.id.wednesdayBtn)
//    val mThursdayBtn = view.findViewById<Button>(R.id.thursdayBtn)
//    val mFridayBtn = view.findViewById<Button>(R.id.fridayBtn)
//    val mSaturdayBtn = view.findViewById<Button>(R.id.saturdayBtn)
//    val mDailySub = view.findViewById<Button>(R.id.daily_sub)
//    val mDatePickerLayout = view.findViewById<LinearLayout>(R.id.startSubLayout)

//        val mDailySubLayout = view.findViewById<ConstraintLayout>(R.id.dailyConstLayout)
//        val mWeeklySubLayout = view.findViewById<ConstraintLayout>(R.id.weeklyConstLayout)
//        val mMonthlySubLayout = view.findViewById<ConstraintLayout>(R.id.monthlyConstLayout)
//        val mWeeklyQtyLayout  = view.findViewById<LinearLayout>(R.id.weeklyQuantityContainer)

//    val mFlag = arrayOf(true, true, true, true, true, true, true)
//
//    mSundayBtn.setOnClickListener {
//        weekDayOnSelect(mFlag[0], mSundayBtn, "Sunday", 0)
//        mFlag[0] = !mFlag[0]
//    }
//
//    mMondayBtn.setOnClickListener {
//        weekDayOnSelect(mFlag[1], mMondayBtn, "Monday", 1)
//        mFlag[1] = !mFlag[1]
//    }
//
//    mTuesdayBtn.setOnClickListener {
//        weekDayOnSelect(mFlag[2], mTuesdayBtn, "Tuesday", 2)
//        mFlag[2] = !mFlag[2]
//    }
//
//    mWednesdayBtn.setOnClickListener {
//        weekDayOnSelect(mFlag[3], mWednesdayBtn, "Wednesday", 3)
//        mFlag[3] = !mFlag[3]
//    }
//
//    mThursdayBtn.setOnClickListener {
//        weekDayOnSelect(mFlag[4], mThursdayBtn, "Thursday", 4)
//        mFlag[4] = !mFlag[4]
//    }
//
//    mFridayBtn.setOnClickListener {
//        weekDayOnSelect(mFlag[5], mFridayBtn, "Friday", 5)
//        mFlag[5] = !mFlag[5]
//    }
//
//    mSaturdayBtn.setOnClickListener {
//        weekDayOnSelect(mFlag[6], mSaturdayBtn, "Saturday", 6)
//        mFlag[6] = !mFlag[6]
//    }


/** Subscription Type **/

        mDailySub.setOnClickListener {
            changeButtonStyleDark(mDailySub)
            changeButtonStyleLight(mWeeklySub, mMonthlySub)
            changeLayout(mDailySubLayout, mWeeklySubLayout, mMonthlySubLayout)
            mDatePickerLayout.visibility = View.VISIBLE
        }

        mWeeklySub.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show()
            changeButtonStyleDark(mWeeklySub)
            changeButtonStyleLight(mDailySub, mMonthlySub)
            changeLayout(mWeeklySubLayout, mDailySubLayout, mMonthlySubLayout)
            mDatePickerLayout.visibility = View.VISIBLE
}

        mMonthlySub.setOnClickListener {
            Toast.makeText(requireContext(), "Coming soon!", Toast.LENGTH_SHORT).show()
            changeButtonStyleDark(mMonthlySub)
            changeButtonStyleLight(mDailySub, mWeeklySub)
            changeLayout(mMonthlySubLayout, mDailySubLayout, mWeeklySubLayout)
            mDatePickerLayout.visibility = View.GONE
}



/**  Functions  **/

//    private fun changeButtonStyleDark(mSubType: Button) {
//        mSubType.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
//        mSubType.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_corner_primary)
//    }
//
//    private fun changeButtonStyleLight(mLightSubOne: Button, mLightSubTwo: Button) {
//        mLightSubOne.setTextColor(ContextCompat.getColor(requireContext(), R.color.body))
//        mLightSubOne.background = ContextCompat.getDrawable(requireContext(),R.drawable.custom_rounded_corner_primary_outline)
//        mLightSubTwo.setTextColor(ContextCompat.getColor(requireContext(), R.color.body))
//        mLightSubTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_corner_primary_outline)
//    }
//
//    private fun changeLayout(mSubLayout: ConstraintLayout, mUnSubLayoutOne: ConstraintLayout, mUnSubLayoutTwo: ConstraintLayout){
//        mSubLayout.visibility = View.VISIBLE
//        mUnSubLayoutOne.visibility = View.GONE
//        mUnSubLayoutTwo.visibility = View.GONE
//    }
//
//    private fun weekDayOnSelect(isSelected: Boolean, buttonSelected: Button, weekDay: String, weekDayIndex: Int){
//        val parentLayout = view.findViewById<LinearLayout>(R.id.weeklyQuantityContainer)
//        val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val rowView: View = inflater.inflate(R.layout.custom_quantity_layout, parentLayout, false)
//        val weeklyIndex : MutableList<String> = ArrayList()
//        val weekDayTV : MutableList<String> = ArrayList()
//        weeklyIndex.add(weekDay)
//
//        if (isSelected){
//            buttonSelected.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_button_shadow_primary)
//            buttonSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
//            parentLayout.addView(rowView, parentLayout.childCount + 1)
//            rowView.findViewById<TextView>(R.id.delivery_day_weekly_tv).text = weekDay
//            updateViewIndex(weekDay, false, 0)
//        }
//        else {
//            buttonSelected.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_button_shadow_light)
//            buttonSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
//            //val indexOfMyView = (parentLayout.parent as ConstraintLayout).indexOfChild(rowView)
//            //Log.d("INDEX", indexOfMyView.toString())
//
//            weeklyIndex.forEachIndexed{ index, _ ->
//                if (weekDayTV[index] == weeklyIndex[index]){
//                    parentLayout.removeViewAt(index)
//                }
//            }
//
//            weeklyIndex.remove(weekDay)
//        }
//    }

 **/