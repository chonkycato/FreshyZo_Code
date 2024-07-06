package com.example.freshyzo

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.Calendar

class SubscriptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subscription, container, false)

        val mDailySubLayout = view.findViewById<ConstraintLayout>(R.id.dailyConstLayout)
        val mWeeklySubLayout = view.findViewById<ConstraintLayout>(R.id.weeklyConstLayout)
        val mMonthlySubLayout = view.findViewById<ConstraintLayout>(R.id.monthlyConstLayout)

        val mDeliveryTypeArray = resources.getStringArray(R.array.DeliveryTypeArray)
        val mSpinner = view.findViewById<Spinner>(R.id.deliveryTypeSpinner)
        val mDatePickerET = view.findViewById<EditText>(R.id.datePickerET)
        val mQtyDecrementBtn = view.findViewById<Button>(R.id.quantity_decrement_btn)
        val mQtyIncrementBtn = view.findViewById<Button>(R.id.quantity_increment_btn)
        val mQtyDisplayTV = view.findViewById<TextView>(R.id.quantity_display_tv)
        val mSubscribeBtn = view.findViewById<Button>(R.id.subscribeBtnSub)

        val mDailySub = view.findViewById<Button>(R.id.daily_sub)
        val mWeeklySub = view.findViewById<Button>(R.id.weekly_sub)
        val mMonthlySub = view.findViewById<Button>(R.id.monthly_sub)
        val mDatePickerLayout = view.findViewById<LinearLayout>(R.id.datePickerLayout)


        mDailySub.setOnClickListener {
            changeButtonStyleDark(mDailySub)
            changeButtonStyleLight(mWeeklySub, mMonthlySub)
            changeLayout(mDailySubLayout, mWeeklySubLayout, mMonthlySubLayout)
            mDatePickerLayout.visibility = View.VISIBLE
        }

        mWeeklySub.setOnClickListener {
            changeButtonStyleDark(mWeeklySub)
            changeButtonStyleLight(mDailySub, mMonthlySub)
            changeLayout(mWeeklySubLayout, mDailySubLayout, mMonthlySubLayout)
            mDatePickerLayout.visibility = View.VISIBLE

        }

        mMonthlySub.setOnClickListener {
            changeButtonStyleDark(mMonthlySub)
            changeButtonStyleLight(mDailySub, mWeeklySub)
            changeLayout(mMonthlySubLayout, mDailySubLayout, mWeeklySubLayout)
            mDatePickerLayout.visibility = View.GONE

        }


        if (mSpinner != null) {
            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, mDeliveryTypeArray
            )
            mSpinner.adapter = spinnerAdapter
            spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_layout)

            mSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Selected : " + mDeliveryTypeArray[position],
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(
                        context,
                        "Please select delivery type to continue!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } //End of block

            mDatePickerET.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val date = calendar.get(Calendar.DAY_OF_MONTH)

                calendar.add(Calendar.DAY_OF_MONTH, 1)
                val nextDate = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = context?.let { it1 ->
                    DatePickerDialog(
                        // on below line we are passing context.
                        it1,
                        { _, year, monthOfYear, dayOfMonth ->
                            // on below line we are setting
                            // date to our edit text.
                            val date = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                            mDatePickerET.setText(date)
                        },
                        // on below line we are passing year, month
                        // and day for the selected date in our date picker.
                        year,
                        month,
                        nextDate
                    )
                }
                datePickerDialog?.datePicker?.minDate = calendar.timeInMillis
                // at last we are calling show
                // to display our date picker dialog.
                datePickerDialog?.show()
            }


        } //End of block

        var prodQtyCount = 0

        mQtyDecrementBtn.setOnClickListener {
            if (prodQtyCount != 0 ){
                prodQtyCount -= 1
                mQtyDisplayTV.text = prodQtyCount.toString()
            }
        }

        mQtyIncrementBtn.setOnClickListener {
            prodQtyCount += 1
            mQtyDisplayTV.text = prodQtyCount.toString()
        }

        return view
    } //End of OnCreateView

    private fun changeButtonStyleDark(mSubType: Button) {
        mSubType.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
        mSubType.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_corner_dark)
    }

    private fun changeButtonStyleLight(mLightSubOne: Button, mLightSubTwo: Button) {
        mLightSubOne.setTextColor(ContextCompat.getColor(requireContext(), R.color.body))
        mLightSubOne.background = ContextCompat.getDrawable(requireContext(),R.drawable.custom_rounded_corner_dark_transparent)
        mLightSubTwo.setTextColor(ContextCompat.getColor(requireContext(), R.color.body))
        mLightSubTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_corner_dark_transparent)
    }

    private fun changeLayout(mSubLayout: ConstraintLayout, mUnSubLayoutOne: ConstraintLayout, mUnSubLayoutTwo: ConstraintLayout){
        mSubLayout.visibility = View.VISIBLE
        mUnSubLayoutOne.visibility = View.GONE
        mUnSubLayoutTwo.visibility = View.GONE
    }
}
