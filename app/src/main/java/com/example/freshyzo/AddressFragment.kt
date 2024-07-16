package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

class AddressFragment : Fragment() {

    private var parentConstraintLayout : ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_address, container, false)
        (activity as MainActivity).hideBottomNav()

        parentConstraintLayout = view.findViewById(R.id.parent_constraint_layout)

        /** Handle back navigation **/
        val backNavIcon = view.findViewById<Button>(R.id.back_icon_address)
        backNavIcon.setOnClickListener { (activity as MainActivity).backNavigation() }


        val addAddress = view.findViewById<EditText>(R.id.address_field)

        /** Initialise Area Spinner and adapter **/
        val areaSpinner = view.findViewById<Spinner>(R.id.area_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            requireContext(), // Use requireContext() to get the context in fragments
            R.array.area_name, // The array defined in strings.xml
            android.R.layout.simple_spinner_item // Default spinner layout
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        areaSpinner.adapter = adapter

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
}