package com.example.freshyzo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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

        parentConstraintLayout = view.findViewById(R.id.parent_constraint_layout)

        val addAddress = view.findViewById<Button>(R.id.add_address)

        val backIconAddress = view.findViewById<ImageView>(R.id.back_icon_address)

        addAddress.setOnClickListener {
            try {
                
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        backIconAddress.setOnClickListener {
            try {
                (activity as MainActivity)!!.loadFragment(HomeFragment(), false)
            } catch (e: Exception){
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