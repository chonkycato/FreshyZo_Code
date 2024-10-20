package com.example.freshyzo

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.freshyzo.api.ApiHandler
import com.example.freshyzo.model.ColonyResponse
import com.example.freshyzo.model.CustomerResponse
import okhttp3.ResponseBody

class ProfileFragment : Fragment() {

    private lateinit var mProfilePictureIV: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var selectedColonyId: String? = null
    private lateinit var selectedColonyName: String
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var address: EditText
    private lateinit var colonySpinner: Spinner
    private lateinit var profilePicture: ImageView
    private lateinit var submitButton: Button
    private lateinit var customerId: String
    private lateinit var customerRole: String
    private val apiHandler = ApiHandler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar("Profile", false)

        /** Intialise Variables */
        firstName = view.findViewById(R.id.first_name)
        lastName = view.findViewById(R.id.last_name)
        phoneNumber = view.findViewById(R.id.phone_number)
        address = view.findViewById(R.id.address)
        colonySpinner = view.findViewById(R.id.colonySpinner)
        profilePicture = view.findViewById(R.id.profile_picture)
        submitButton = view.findViewById(R.id.submitBtnProfile)


        /** Retrieve user data from shared preferences**/
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        customerId = sharedPref.getString("customer_id", "").toString()
        customerRole = sharedPref.getString("customer_role", "").toString()
        Log.d("CustomerId, Role" , "$customerId $customerRole")

        /** Fetch Customer Data*/
        fetchCustomerData("172", "guest")

        /** Update Customer Data **/
        submitButton.setOnClickListener {
            updateCustomerProfile()
        }

        return view
    }

    /** Fetch Customer Information */
    private fun fetchCustomerData(customerId: String, customerRole: String) {
        apiHandler.fetchCustomerDetails(customerId, customerRole, object : ApiHandler.CustomerListCallback {
            override fun onSuccess(customerList: List<CustomerResponse>) {
                if (customerList.isNotEmpty()) {
                    val customer = customerList[0]  // Assuming you want the first customer
                    for (customer in customerList) {
                        Log.d("Customer", "Colony ID: ${customer.colonyId}, Name: ${customer.firstName} ${customer.lastName}, Contact: ${customer.contact}, Address: ${customer.address}")
                    }
                    displayCustomerData(customer)
                }
            }

            override fun onFailure(message: String) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /** Display Customer Information */
    private fun displayCustomerData(customer: CustomerResponse) {
        val imageName = customer.customerImage
        val imageURI = "xyzLink/$imageName" //Fetch and set image URI from the API

        firstName.setText(capitalizeWords(customer.firstName ?: ""))
        lastName.setText(capitalizeWords(customer.lastName ?: ""))
        phoneNumber.setText(customer.contact ?: "")
        address.setText(capitalizeWords(customer.address ?: ""))
        profilePicture.setImageResource(R.drawable.pfp)
        selectedColonyName = (capitalizeWords(customer.colonyName))
        fetchColonies()
    }

    /** Fetch Colonies */
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

    /** Set up spinner with colony data */
    private fun setupAreaSpinner(colonies: List<ColonyResponse>) {
        // Only use actual colony names for the spinner
        val colonyNames = colonies.map { capitalizeWords(it.colonyName) }
        Log.d("COLONY NAMES:", "$colonyNames")

        colonySpinner.background = ContextCompat.getDrawable(requireContext(), R.drawable.custom_edittext_background)

        // Set up the adapter with actual items
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, colonyNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        colonySpinner.adapter = adapter

        // Set a default hint text
        colonySpinner.setSelection(-1) // Set no selection initially
        colonySpinner.background = null // Clear any background for the hint
        colonySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position > 0) { // Check if the selection is valid (not the hint)
                    selectedColonyId = colonies[position - 1].colonyID.toString() // Use `position - 1` to get the correct index
                    Log.d("COLONY ID", "Selected Colony ID: $selectedColonyId")
                } else {
                    // User selected the hint, so no valid colony
                    selectedColonyId = null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedColonyId = null // No selection
            }
        }

        // Add the default hint text manually
        colonySpinner.setSelection(0)
        colonySpinner.setAdapter(object : ArrayAdapter<String>(requireContext(), R.layout.custom_spinner_item, arrayListOf(selectedColonyName) + colonyNames) {
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

    /** Update Profile **/
    private fun updateCustomerProfile() {
        val firstNameText = (firstName.text).toString()
        val lastNameText = (lastName.text).toString()
        val numberText = (phoneNumber.text).toString()
        val addressText = (address.text).toString()
        val colonyID = selectedColonyId.toString()

        if (!customerId.isNullOrEmpty() && !customerRole.isNullOrEmpty()) {
            apiHandler.updateCustomerDetails(
                customerId, customerRole, firstNameText, lastNameText,
                numberText, addressText, colonyID, object : retrofit2.Callback<ResponseBody> {
                    override fun onResponse(call: retrofit2.Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            val responsee = response.body()?.toString()
                            Log.d("UpdateProfile", responsee.toString())
                            if(isAdded){
                               Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                            }
                            // Handle successful profile update
                        } else {
                            Log.d("UpdateProfile", "Profile update failed with status code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                        Log.e("UpdateProfile", "Profile update failed: ${t.message}")
                    }
                }
            )
        }
    }

    /** Function to capitalize the first letter of each word */
    fun capitalizeWords(input: String): String {
        if (input.isNullOrBlank()) return ""
        return input.split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar { it.uppercase() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            mProfilePictureIV.setImageURI(imageUri)
        }
    }



}