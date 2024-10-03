package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.freshyzo.api.ApiHandler
import com.example.freshyzo.model.ColonyResponse

class AddressFragment : Fragment() {

    private var parentConstraintLayout : ConstraintLayout? = null
    private lateinit var areaSpinner: Spinner
    private var selectedColonyId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_address, container, false)

        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar("Saved Addresses", true)

        parentConstraintLayout = view.findViewById(R.id.parent_constraint_layout)

        val addAddress = view.findViewById<EditText>(R.id.address_field)

        /** Initialise Area Spinner and adapter **/
        areaSpinner = view.findViewById(R.id.area_spinner)
        fetchColonies()

        addAddress.setOnClickListener {
            try {
                
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    fun onAddNewAddressField(view: View) {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.address_edit_text_layout, null)
        parentConstraintLayout!!.addView(rowView, parentConstraintLayout!!.childCount - 1)
    }

    private fun fetchColonies() {
        val apiHandler = ApiHandler()
        apiHandler.fetchColonies(object : ApiHandler.ColonyListCallback {
            override fun onSuccess(colonies: List<ColonyResponse>) {
                setupAreaSpinner(colonies)
            }

            override fun onFailure(errorMessage: String) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to set up spinner with colony data
    private fun setupAreaSpinner(colonies: List<ColonyResponse>) {
        // Only use actual colony names for the spinner
        val colonyNames = colonies.map { capitalizeWords(it.colonyName) }
        Log.d("COLONY NAMES:", "$colonyNames")

        areaSpinner.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edittext_background)

        // Set up the adapter with actual items
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, colonyNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        areaSpinner.adapter = adapter

        // Set a default hint text
        areaSpinner.setSelection(-1) // Set no selection initially
        areaSpinner.background = null // Clear any background for the hint
        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position >= 0 && position < colonies.size) {
                    selectedColonyId = colonies[position].colonyID.toString() // Access the correct colony ID
                    Log.d("COLONY ID", "Selected Colony ID: $selectedColonyId")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedColonyId = null
            }
        }

        // Add the default hint text manually
        areaSpinner.setSelection(0)
        areaSpinner.setAdapter(object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, arrayListOf("Select your area") + colonyNames) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the hint (default item)
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.spinnerTextView)
                textView.text = getItem(position)
                if (position == 0) {
                    textView.setTextColor(resources.getColor(R.color.disabled))
                } else {
                    textView.setTextColor(resources.getColor(R.color.body))
                }
                return view
            }
        })
    }

    // Function to capitalize the first letter of each word
    private fun capitalizeWords(text: String): String {
        return text.split(" ").joinToString(" ") { it.capitalize() }
    }
}