package com.example.freshyzo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.model.DataModelActiveSub
import com.example.freshyzo.model.RecyclerAdapterSubActive
import java.util.Calendar

class SubActiveFragment : Fragment() {

    private lateinit var view: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterSubActive
    private var dataList = mutableListOf<DataModelActiveSub>()
    private lateinit var selectedStartDate: String
    private lateinit var selectedEndDate: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_subscriptions_active, container, false)
        this.view = view

        dataList.add(
            DataModelActiveSub(
                R.drawable.img_milk_buff, resources.getString(R.string.buffalo_milk), resources.getString(R.string.itemPrice),
                resources.getString(R.string.sample_start_date), resources.getString(R.string.sample_start_date), resources.getString(R.string.active)
//                , resources.getString(R.string.address_body)
            )
        )

        dataList.add(
            DataModelActiveSub(
                R.drawable.img_ghee, resources.getString(R.string.ghee), resources.getString(R.string.itemPrice),
                resources.getString(R.string.sample_start_date), resources.getString(R.string.sample_start_date), resources.getString(R.string.active)
//                , resources.getString(R.string.address_body)
            )
        )

        recyclerView = view.findViewById(R.id.recyclerViewActiveSub)
        recyclerAdapter = RecyclerAdapterSubActive(requireContext(), this)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.onItemClickedPause = {
            showPauseDialog(it)
        }

        recyclerAdapter.onItemClickedCancel = {
            showAlertDialog("Cancel", it)
        }

        recyclerAdapter.setDataList(dataList)

        return view
    }

    private fun showPauseDialog(it: DataModelActiveSub) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

        // AlertDialog to explain date selection
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Pause Subscription")
        alertDialogBuilder.setMessage("You will be prompted to select the start and end dates for pausing the subscription. " +
                "Please note: You cannot pause the subscription for tomorrow if it is past 9 pm.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()

            // Date selection logic after user confirms
            if (currentHour >= 21) {
                calendar.add(Calendar.DAY_OF_YEAR, 2) // Minimum date is day after tomorrow
            } else {
                calendar.add(Calendar.DAY_OF_YEAR, 1) // Minimum date is tomorrow
            }

            // DatePickerDialog for start date
            val startDatePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedStartDate = "$dayOfMonth/${month + 1}/$year"
                    showEndDatePicker(it, selectedStartDate, year, month, dayOfMonth)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            startDatePicker.datePicker.minDate = calendar.timeInMillis // Set the minimum selectable date
            startDatePicker.setTitle("Select Start Date")
            startDatePicker.show()
        }

        alertDialogBuilder.create().show() // Show the prompt first
    }

    private fun showEndDatePicker(it: DataModelActiveSub, startDate: String, startYear: Int, startMonth: Int, startDay: Int) {
        // Calendar instance for the end date
        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.set(startYear, startMonth, startDay)

        // DatePickerDialog for end date
        val endDatePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Validate that the end date is after the start date
                val selectedEndCalendar = Calendar.getInstance()
                selectedEndCalendar.set(year, month, dayOfMonth)

                if (selectedEndCalendar.timeInMillis <= endDateCalendar.timeInMillis) {
                    // Show error if end date is the same or before start date
                    Toast.makeText(
                        requireContext(),
                        "End date must be after the start date.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Re-open the end date picker if validation fails
                    showEndDatePicker(it, startDate, startYear, startMonth, startDay)
                } else {
                    selectedEndDate = "$dayOfMonth/${month + 1}/$year"
                    // Proceed with storing dates or further logic
                    storePauseSubscription(it, selectedStartDate, selectedEndDate)
                }
            },
            endDateCalendar.get(Calendar.YEAR),
            endDateCalendar.get(Calendar.MONTH),
            endDateCalendar.get(Calendar.DAY_OF_MONTH)
        )

        endDatePicker.datePicker.minDate = endDateCalendar.timeInMillis + (24 * 60 * 60 * 1000) // Ensure minimum end date is the day after start date
        endDatePicker.setTitle("Select End Date")
        endDatePicker.show()
    }


    // Store the selected dates and show a confirmation toast
    private fun storePauseSubscription(it: DataModelActiveSub, startDate: String, endDate: String?) {
        // Here you can save startDate and endDate to your model or database
        Toast.makeText(
            requireContext(),
            "Subscription paused from $startDate to ${endDate ?: "unspecified end date"}",
            Toast.LENGTH_LONG
        ).show()

        // Example: Mark the item as paused in the list and update the RecyclerView
        it.subStatus = "Paused"
        recyclerAdapter.notifyDataSetChanged()
    }

    private fun showCancelDatePicker(it: DataModelActiveSub) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val cancelDate = "$dayOfMonth/${month + 1}/$year"

                // Store cancel date (in model or to the server)
                storeCancelDate(it, cancelDate)

                // Show success dialog after date is stored
                showSuccessDialog(cancelDate, "cancel")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setTitle("Select date to cancel subscription")
        datePickerDialog.show()
    }

    private fun showAlertDialog(alertTitle: String, it: DataModelActiveSub) {
        val productTitle = it.productTitle
        val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        val title = view.findViewById<TextView>(resources.getIdentifier("alertTitle", "id", "android"))
        title?.setTextAppearance(R.style.DialogTitleStyle)
        val message = view.findViewById<TextView>(resources.getIdentifier("message", "id", "android"))
        message?.setTextAppearance(R.style.DialogMessageStyle)

        alertDialogBuilder.setTitle("$alertTitle Subscription?")
        alertDialogBuilder.setMessage("Are you sure you want to $alertTitle subscription for $productTitle?")
        alertDialogBuilder.setPositiveButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("YES") { dialog, _ ->
            removeItemFromList(it)
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    private fun storeSubscriptionDates(it: DataModelActiveSub) {
        // Storing the dates (in model, shared preferences, or sending to the server)

        // Show success dialog after the dates have been stored or sent
        showSuccessDialog(selectedStartDate, selectedEndDate)
    }

    private fun storeCancelDate(it: DataModelActiveSub, cancelDate: String) {
        // Logic to store the selected cancel date (could be local or send to server)
        // For example, update your item in the list with the new cancel date
    }

    private fun showSuccessDialog(startDate: String?, endDate: String?) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Subscription Paused")
        alertDialogBuilder.setMessage("Your subscription has been successfully paused from $startDate to $endDate.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }

    private fun removeItemFromList(item: DataModelActiveSub) {
        val position = dataList.indexOf(item)
        if (position != -1) {
            dataList.removeAt(position)
            recyclerAdapter.notifyItemRemoved(position)
        }
    }
}
