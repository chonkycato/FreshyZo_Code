package com.example.freshyzo

import android.app.DatePickerDialog
import android.content.Context
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


class SubscribeProductFragment : Fragment() {

    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_subscribe_product, container, false)
        this.view = view
        (activity as MainActivity).hideBottomNav()

        val mDailySubLayout = view.findViewById<ConstraintLayout>(R.id.dailyConstLayout)
        val mWeeklySubLayout = view.findViewById<ConstraintLayout>(R.id.weeklyConstLayout)
        val mMonthlySubLayout = view.findViewById<ConstraintLayout>(R.id.monthlyConstLayout)
        val mWeeklyQtyLayout  = view.findViewById<LinearLayout>(R.id.weeklyQuantityContainer)

        val mDeliveryTypeArray = resources.getStringArray(R.array.DeliveryTypeArray)
        val mSpinner = view.findViewById<Spinner>(R.id.deliveryTypeSpinner)
        val mStartDateET = view.findViewById<EditText>(R.id.startDatePickerET)
        val mEndDateET = view.findViewById<EditText>(R.id.endDatePickerET)
        val mQtyDecrementBtn = view.findViewById<Button>(R.id.quantity_decrement_btn)
        val mQtyIncrementBtn = view.findViewById<Button>(R.id.quantity_increment_btn)
        val mQtyDisplayTV = view.findViewById<TextView>(R.id.quantity_display_tv)
        val mSubscribeBtn = view.findViewById<Button>(R.id.subscribeBtnSub)

        val mDailySub = view.findViewById<Button>(R.id.daily_sub)
        val mWeeklySub = view.findViewById<Button>(R.id.weekly_sub)
        val mMonthlySub = view.findViewById<Button>(R.id.monthly_sub)
        val mDatePickerLayout = view.findViewById<LinearLayout>(R.id.startVacationLayout)

        //Weekly Sub Layout

        val mSundayBtn = view.findViewById<Button>(R.id.sundayBtn)
        val mMondayBtn = view.findViewById<Button>(R.id.mondayBtn)
        val mTuesdayBtn = view.findViewById<Button>(R.id.tuesdayBtn)
        val mWednesdayBtn = view.findViewById<Button>(R.id.wednesdayBtn)
        val mThursdayBtn = view.findViewById<Button>(R.id.thursdayBtn)
        val mFridayBtn = view.findViewById<Button>(R.id.fridayBtn)
        val mSaturdayBtn = view.findViewById<Button>(R.id.saturdayBtn)


        /** Subscription Type **/

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


        /** Daily Subscription**/

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
            } //End of listener



            mStartDateET.setOnClickListener {
                datePicker(mStartDateET)
            }

            mEndDateET.setOnClickListener {
                datePicker(mEndDateET)
            }


        } //End of if block

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


        /** Weekly Subscription **/

        val mFlag = arrayOf(true, true, true, true, true, true, true)

        mSundayBtn.setOnClickListener {
            weekDayOnSelect(mFlag[0], mSundayBtn, "Sunday", 0)
            mFlag[0] = !mFlag[0]
        }

        mMondayBtn.setOnClickListener {
            weekDayOnSelect(mFlag[1], mMondayBtn, "Monday", 1)
            mFlag[1] = !mFlag[1]
        }

        mTuesdayBtn.setOnClickListener {
            weekDayOnSelect(mFlag[2], mTuesdayBtn, "Tuesday", 2)
            mFlag[2] = !mFlag[2]
        }

        mWednesdayBtn.setOnClickListener {
            weekDayOnSelect(mFlag[3], mWednesdayBtn, "Wednesday", 3)
            mFlag[3] = !mFlag[3]
        }

        mThursdayBtn.setOnClickListener {
            weekDayOnSelect(mFlag[4], mThursdayBtn, "Thursday", 4)
            mFlag[4] = !mFlag[4]
        }

        mFridayBtn.setOnClickListener {
            weekDayOnSelect(mFlag[5], mFridayBtn, "Friday", 5)
            mFlag[5] = !mFlag[5]
        }

        mSaturdayBtn.setOnClickListener {
            weekDayOnSelect(mFlag[6], mSaturdayBtn, "Saturday", 6)
            mFlag[6] = !mFlag[6]
        }

        return view
    } //End of OnCreateView

    private fun changeButtonStyleDark(mSubType: Button) {
        mSubType.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
        mSubType.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_corner_primary)
    }

    private fun changeButtonStyleLight(mLightSubOne: Button, mLightSubTwo: Button) {
        mLightSubOne.setTextColor(ContextCompat.getColor(requireContext(), R.color.body))
        mLightSubOne.background = ContextCompat.getDrawable(requireContext(),R.drawable.custom_rounded_corner_primary_outline)
        mLightSubTwo.setTextColor(ContextCompat.getColor(requireContext(), R.color.body))
        mLightSubTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_rounded_corner_primary_outline)
    }

    private fun changeLayout(mSubLayout: ConstraintLayout, mUnSubLayoutOne: ConstraintLayout, mUnSubLayoutTwo: ConstraintLayout){
        mSubLayout.visibility = View.VISIBLE
        mUnSubLayoutOne.visibility = View.GONE
        mUnSubLayoutTwo.visibility = View.GONE
    }

    private fun weekDayOnSelect(isSelected: Boolean, buttonSelected: Button, weekDay: String, weekDayIndex: Int){
        val parentLayout = view.findViewById<LinearLayout>(R.id.weeklyQuantityContainer)
        val inflater = requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.custom_quantity_layout, parentLayout, false)
        val weeklyIndex : MutableList<String> = ArrayList()
        val weekDayTV : MutableList<String> = ArrayList()
        weeklyIndex.add(weekDay)

        if (isSelected){
            buttonSelected.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_button_shadow_primary)
            buttonSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.background))
            parentLayout.addView(rowView, parentLayout.childCount + 1)
            rowView.findViewById<TextView>(R.id.delivery_day_weekly_tv).text = weekDay
            updateViewIndex(weekDay, false, 0)
        }
        else {
            buttonSelected.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_button_shadow_light)
            buttonSelected.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled))
            //val indexOfMyView = (parentLayout.parent as ConstraintLayout).indexOfChild(rowView)
            //Log.d("INDEX", indexOfMyView.toString())

            weeklyIndex.forEachIndexed{ index, _ ->
                if (weekDayTV[index] == weeklyIndex[index]){
                    parentLayout.removeViewAt(index)
                }
            }

            weeklyIndex.remove(weekDay)
        }
    }

    private fun updateViewIndex(weekDayOnTV: String, findIndex: Boolean, returnIndex: Int) : Int{
        val weekDayTV : MutableList<String> = ArrayList()
        weekDayTV.add(weekDayOnTV)
        if(findIndex && returnIndex != 0){
            // TODO: WEEKDAY
        }
        return returnIndex
    }

    private fun datePicker(datePickerET: EditText){

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)

        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val nextDate = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = context?.let { it1 ->
            DatePickerDialog(
                // on below line we are passing context.
                it1,
                { _, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val mDate = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                    datePickerET.setText(mDate)
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
}
