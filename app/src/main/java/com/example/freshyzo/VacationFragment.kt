package com.example.freshyzo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VacationFragment : Fragment() {

    private lateinit var mStartVacationLayout: LinearLayout
    private lateinit var mEndVacationLayout: LinearLayout
    private lateinit var mCancelVacationButton: Button
    private lateinit var mAlertLabel: TextView
    private lateinit var mVacationDuration: TextView // TextView to display the vacation duration

    private var startVacationTappedOnce = false // Flag to track button taps
    private var startDateMillis: Long = 0 // Store the selected start date in milliseconds

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vacation, container, false)

        val mStartVacationDateET = view.findViewById<EditText>(R.id.start_date)
        val mEndVacationDateOptET = view.findViewById<EditText>(R.id.end_date_opt)
        val mEndVacationDateReqET = view.findViewById<EditText>(R.id.end_date_req)
        val mStartVacationButton = view.findViewById<Button>(R.id.start_vacation_button)
        val mEndVacationButton = view.findViewById<Button>(R.id.end_vacation_button)

        mAlertLabel = view.findViewById(R.id.alert_label) // Alert TextView
        mVacationDuration = view.findViewById(R.id.vacation_duration) // TextView to show vacation duration

        mStartVacationLayout = view.findViewById(R.id.layout_start_vacation)
        mEndVacationLayout = view.findViewById(R.id.layout_end_vacation)
        mCancelVacationButton = view.findViewById(R.id.cancel_vacation_button)

        onVacationMode()

        mStartVacationDateET.setOnClickListener {
            datePicker(mStartVacationDateET, isStartDate = true)
        }

        mEndVacationDateOptET.setOnClickListener {
            if (startDateMillis != 0L) {
                datePicker(mEndVacationDateOptET, isStartDate = false, minDateMillis = startDateMillis)
            } else {
                Toast.makeText(context, "Please select a start date first.", Toast.LENGTH_SHORT).show()
            }
        }

        mEndVacationDateReqET.setOnClickListener {
            if (startDateMillis != 0L) {
                datePicker(mEndVacationDateReqET, isStartDate = false, minDateMillis = startDateMillis)
            } else {
                Toast.makeText(context, "Please select a start date first.", Toast.LENGTH_SHORT).show()
            }
        }

        mStartVacationButton.setOnClickListener {
            if (!startVacationTappedOnce) {
                // First tap: show the alert label
                mAlertLabel.visibility = View.VISIBLE
                startVacationTappedOnce = true
                Toast.makeText(context, "Tap again to confirm starting vacation", Toast.LENGTH_SHORT).show()
            } else {
                // Second tap: validate dates and start vacation
                if (validateDates(mStartVacationDateET.text.toString(), mEndVacationDateOptET.text.toString())) {
                    updateDatabaseStart(mStartVacationDateET.text.toString(), mEndVacationDateOptET.text.toString())
                    AlertDialog.Builder(requireContext())
                        .setTitle("Vacation Mode Started")
                        .setMessage("All your subscriptions will be paused")
                        .setPositiveButton("OK", null)
                        .show()

                    // Reset the tap tracker and hide alert label
                    startVacationTappedOnce = false
                    mAlertLabel.visibility = View.GONE
                }
            }
        }

        mEndVacationButton.setOnClickListener {
            updateDatabaseEnd(mEndVacationDateReqET.text.toString())
            AlertDialog.Builder(requireContext())
                .setTitle("Vacation Mode Ended")
                .setMessage("All your subscriptions will be resumed")
                .setPositiveButton("OK", null)
                .show()
        }

        mCancelVacationButton.setOnClickListener {
            cancelVacation()
        }

        return view
    }

    private fun datePicker(datePickerET: EditText, isStartDate: Boolean, minDateMillis: Long = 0L) {
        val calendar = Calendar.getInstance()

        if (isStartDate) {
            // Get current hour to check if it's before or after 9 pm
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

            // Set the minimum selectable date based on the current time
            if (currentHour >= 21) {
                calendar.add(Calendar.DAY_OF_MONTH, 2)
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
        } else {
            // If it's an end date, use the selected start date as the minimum date
            calendar.timeInMillis = minDateMillis
        }

        val datePickerDialog = context?.let { it1 ->
            DatePickerDialog(it1, { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                datePickerET.setText(selectedDate)

                if (isStartDate) {
                    // Store the start date in milliseconds for future use
                    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                    val selectedDateString = "$dayOfMonth-${monthOfYear + 1}-$year"
                    val date = sdf.parse(selectedDateString)
                    startDateMillis = date?.time ?: 0
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }

        if (!isStartDate) {
            // For the end date, set the minimum selectable date to the selected start date
            datePickerDialog?.datePicker?.minDate = minDateMillis
        } else {
            // For the start date, set the minimum selectable date to the day after tomorrow or tomorrow
            datePickerDialog?.datePicker?.minDate = calendar.timeInMillis
        }

        datePickerDialog?.show()
    }

    private fun validateDates(startDate: String, endDate: String?): Boolean {
        if (endDate != null && endDate == startDate) {
            Toast.makeText(context, "End date cannot be the same as start date.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun updateDatabaseStart(startDate: String, endDate: String?) {
        val sharedPref = requireActivity().getSharedPreferences("vacation", Context.MODE_PRIVATE)
        val currentTime = System.currentTimeMillis()

        sharedPref.edit().putString("startDate", startDate).apply()
        sharedPref.edit().putLong("startTime", currentTime).apply() // Storing the start time
        if (endDate != null) {
            sharedPref.edit().putString("endDate", endDate).apply()
        }
        sharedPref.edit().putString("vacation_mode", "ON").apply()
        onVacationMode()
    }

    private fun updateDatabaseEnd(endDate: String) {
        val sharedPref = requireActivity().getSharedPreferences("vacation", Context.MODE_PRIVATE)
        sharedPref.edit().putString("endDate", endDate).apply()
        sharedPref.edit().putString("vacation_mode", "OFF").apply()
        onVacationMode()
    }

    private fun cancelVacation() {
        val sharedPref = requireActivity().getSharedPreferences("vacation", Context.MODE_PRIVATE)
        val startTime = sharedPref.getLong("startTime", 0L)
        val currentTime = System.currentTimeMillis()

        if (currentTime - startTime <= 24 * 60 * 60 * 1000) {
            sharedPref.edit().clear().apply()
            AlertDialog.Builder(requireContext())
                .setTitle("Vacation Mode Cancelled")
                .setMessage("Your vacation mode has been successfully cancelled.")
                .setPositiveButton("OK", null)
                .show()
            onVacationMode()
        } else {
            Toast.makeText(context, "Vacation mode can only be cancelled within 24 hours of enabling.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onVacationMode() {
        val sharedPref = requireActivity().getSharedPreferences("vacation", Context.MODE_PRIVATE)
        val vacationMode = sharedPref.getString("vacation_mode", "OFF")

        if (vacationMode == "OFF") {
            mStartVacationLayout.visibility = View.VISIBLE
            mEndVacationLayout.visibility = View.GONE
            mCancelVacationButton.visibility = View.GONE
            mVacationDuration.visibility = View.GONE // Hide vacation duration when vacation mode is off
        } else if (vacationMode == "ON") {
            mStartVacationLayout.visibility = View.GONE
            mEndVacationLayout.visibility = View.VISIBLE

            val startDate = sharedPref.getString("startDate", "")
            val endDate = sharedPref.getString("endDate", "")

            if (!startDate.isNullOrEmpty()) {
                mVacationDuration.visibility = View.VISIBLE // Show vacation duration
                if (!endDate.isNullOrEmpty()) {
                    mVacationDuration.text = "Vacation from $startDate to $endDate"
                } else {
                    mVacationDuration.text = "Vacation started on $startDate"
                }
            }

            val startTime = sharedPref.getLong("startTime", 0L)
            val currentTime = System.currentTimeMillis()

            if (currentTime - startTime <= 24 * 60 * 60 * 1000) {
                mCancelVacationButton.visibility = View.VISIBLE
            } else {
                mCancelVacationButton.visibility = View.GONE
            }
        }
    }
}
