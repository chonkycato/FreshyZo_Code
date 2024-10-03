package com.example.freshyzo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshyzo.adapter.RecyclerAdapterSubPaused
import com.example.freshyzo.model.DataModelPausedSub
import java.util.Calendar

class SubPausedFragment : Fragment() {

    private lateinit var view: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapterSubPaused
    private var dataList = mutableListOf<DataModelPausedSub>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view = inflater.inflate(R.layout.fragment_subscriptions_paused, container, false)



        dataList.add(
            DataModelPausedSub(
                R.drawable.img_milk_buff,
                resources.getString(R.string.buffalo_milk),
                resources.getString(R.string.itemPrice),
                resources.getString(R.string.sample_start_date),
                resources.getString(R.string.sample_start_date),
                resources.getString(R.string.paused)
//              , resources.getString(R.string.address_body)
            )
        )

        recyclerView = view.findViewById(R.id.recyclerViewPausedSub)
        recyclerAdapter = RecyclerAdapterSubPaused()

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        recyclerView.adapter = recyclerAdapter

        recyclerAdapter.onItemClickResume = {
            showAlertDialog("Resume", it)
        }

        recyclerAdapter.onItemClickCancel = {
            showAlertDialog("Cancel", it)
        }

        recyclerAdapter.setDataList(dataList)

        return view
    }

    private fun showAlertDialog(alertTitle: String, it: DataModelPausedSub) {

        val productTitle = it.productTitle
        val alertDialogBuilder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        alertDialogBuilder.setTitle("$alertTitle Subscription?")
        alertDialogBuilder.setMessage("Are you sure you want to $alertTitle subscription for $productTitle?")

        alertDialogBuilder.setPositiveButton("NO") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("YES") { dialog, _ ->
            dialog.dismiss()

            if (alertTitle == "Resume") {
                // Call method to handle date selection for resuming
                showResumeDatePicker(it)
            } else if (alertTitle == "Cancel") {
                // Call method to handle date selection for canceling
                showCancelDatePicker(it)
            }
        }

        alertDialogBuilder.create().show()
    }

    private fun showResumeDatePicker(it: DataModelPausedSub) {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        // Check if the current time is past 9 PM
        val isAfter9PM = currentHour >= 21

        // Set the minimum date:
        // If past 9 PM, the minimum date is day after tomorrow
        // Otherwise, the minimum date is tomorrow
        if (isAfter9PM) {
            // Set to day after tomorrow
            calendar.add(Calendar.DAY_OF_MONTH, 2)
        } else {
            // Set to tomorrow
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val resumeDate = "$dayOfMonth/${month + 1}/$year"

                // Update the pauseDate field in DataModelPausedSub
                it.pauseEndDate = resumeDate

                // Notify the adapter that data has changed
                recyclerAdapter.notifyDataSetChanged()

                // Store resume date (in model or to the server)
                storeResumeDate(it, resumeDate)

                // Show success dialog after date is stored
                showSuccessDialog(resumeDate, "resume")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Disable dates up to today or the day after tomorrow if past 9 PM
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.setTitle("Select date to resume subscription")
        datePickerDialog.show()
    }


    private fun storeResumeDate(it: DataModelPausedSub, resumeDate: String) {
        // Logic to store the selected resume date (could be local or send to server)
        // For example, update your item in the list with the new resume date
    }

    private fun showCancelDatePicker(it: DataModelPausedSub) {
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

    private fun storeCancelDate(it: DataModelPausedSub, cancelDate: String) {
        // Logic to store the selected cancel date (could be local or send to server)
        // For example, update your item in the list with the new cancel date
    }

    private fun showSuccessDialog(selectedDate: String, action: String) {
        val actionMessage = if (action == "resume") {
            "resumed from"
        } else {
            "canceled starting"
        }

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Subscription $actionMessage")
        alertDialogBuilder.setMessage("Your subscription will be $actionMessage $selectedDate.")
        alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.create().show()
    }
}
