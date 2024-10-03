package com.example.freshyzo.login

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
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
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.freshyzo.HomeFragment
import com.example.freshyzo.MainActivity
import com.example.freshyzo.R
import com.example.freshyzo.api.ApiHandler
import com.example.freshyzo.model.ColonyResponse
import com.example.freshyzo.model.SignUpResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SignupFragment : Fragment() {

    private lateinit var areaSpinner: Spinner
    private lateinit var signupButton: Button
    private lateinit var fullNameInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var otpInput: EditText
    private lateinit var mGetOTP: TextView
    private var selectedColonyId: String? = null
    private var countDownTimer: CountDownTimer? = null
    private val apiHandler: ApiHandler = ApiHandler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        /** Handle top and bottom nav **/
        (activity as MainActivity).handleNavigationToolbar(null, false)

        fullNameInput = view.findViewById(R.id.fullNameInput)
        phoneInput = view.findViewById(R.id.phoneInput)
        otpInput = view.findViewById(R.id.otpInput)
        signupButton = view.findViewById(R.id.signupButton)
        areaSpinner = view.findViewById(R.id.areaSelectionSpinner)
        mGetOTP = view.findViewById(R.id.getOTP)

        /** Fetch colony data from API **/
        fetchColonies()

        // Get OTP action
        mGetOTP.setOnClickListener {
            val phoneNumber = phoneInput.text.toString()
            val method = "signup"
            if (validateForOtp()) {
                requestOTP(phoneNumber, method)
                //Store start time of timer
                val sharedPref = requireContext().getSharedPreferences("otp_timer", Context.MODE_PRIVATE)
                val startTime = System.currentTimeMillis() // Current time in milliseconds
                with(sharedPref.edit()) {
                    putLong("start_time", startTime)
                    apply()
                }
            }
        }

        signupButton.setOnClickListener {
            if (validateInputs()) {
                registerUser() // Proceed with signup
            }
        }

        view.findViewById<TextView>(R.id.loginInstead).setOnClickListener {
            (activity as MainActivity).loadFragment(LoginFragment(), false, null)
        }

        return view
    }

    /** OTP request API call **/
    private fun requestOTP(phoneNumber: String, method: String) {
        apiHandler.requestOTP(phoneNumber, method, object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val rawResponse = response.body()?.string()

                // Log the raw response
                Log.d("RAW_RESPONSE", rawResponse ?: "No response body")

                if (response.isSuccessful && rawResponse != null) {
                    // Handle the case where the OTP is sent successfully
                    Log.d("OTP", "OTP sent successfully: $rawResponse")
                    Toast.makeText(context, "$rawResponse", Toast.LENGTH_LONG).show()

                    // Start the timer only if the OTP request was successful
                    startOTPTimer()
                } else {
                    // Handle failure cases with the plain text response
                    Log.e("OTP Error", "Failed to send OTP: $rawResponse")
                    Toast.makeText(context, "Failed to send OTP: $rawResponse", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Log and show a toast for failure
                Log.e("OTP Failure", "Request failed: ${t.message}")
                Toast.makeText(context, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

    }

    /** User registration API call **/
    private fun registerUser() {
        val (first_name, last_name) = splitNames(fullNameInput.text.toString())
        val mobile_no = phoneInput.text.toString()
        val colony = selectedColonyId
        val otp = otpInput.text.toString() // Changed to fetch OTP from input, assuming it is an EditText field

        // Check if colony and other inputs are valid
        if (colony.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please select a colony.", Toast.LENGTH_SHORT).show()
            return
        } else if(first_name.isEmpty() || last_name.isEmpty()){
            Toast.makeText(requireContext(), "Please enter your full name.", Toast.LENGTH_SHORT).show()
            return
        }else if(mobile_no.isEmpty()){
            Toast.makeText(requireContext(), "Please enter a valid phone number.", Toast.LENGTH_SHORT).show()
            return
        }else if(otp.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter a valid OTP.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        apiHandler.registerUser(first_name, last_name, mobile_no, colony, otp, object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                // Extract the raw response from the headers added by the LoggingInterceptor
                val rawResponse = response.headers()["RAW_RESPONSE_BODY"].toString()

                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        Log.d("REGISTRATION", rawResponse)
                        Toast.makeText(requireContext(), "Registration successful! Raw response: $rawResponse", Toast.LENGTH_LONG).show()
                        saveSignUpData(signUpResponse) // Method to save registration data
                        (activity as MainActivity).changeLoginState(true)
                        (activity as MainActivity).clearBackStack()
                        (activity as MainActivity).loadFragment(HomeFragment(), true, null)

                    } else {
                        Toast.makeText(requireContext(), "Registration failed! Raw response: $rawResponse", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Registration failed! Raw response: $rawResponse", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Registration failed due to network error.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /** Fetch colony data from the API **/
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

    /** Set up spinner with colony data **/
    private fun setupAreaSpinner(colonies: List<ColonyResponse>) {
        // Map the colony names for display in the spinner
        val colonyNames = colonies.map { capitalizeWords(it.colonyName) }
        Log.d("COLONY NAMES:", "$colonyNames")

        // Prepare the adapter with a default hint item "Select your area"
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.custom_spinner_item,
            arrayListOf("Select your area") + colonyNames // Adding the hint as the first item
        ) {
            override fun isEnabled(position: Int): Boolean {
                // Disable the hint (position 0)
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view.findViewById<TextView>(R.id.spinnerTextView)
                textView.text = getItem(position)
                if (position == 0) {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.disabled)) // Hint color
                } else {
                    textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.body)) // Regular color
                }
                return view
            }
        }

        // Set the adapter to the spinner
        areaSpinner.adapter = adapter

        // Set the spinner to show the hint by default
        areaSpinner.setSelection(0) // Position 0 is the hint

        // Set the ItemSelectedListener
        areaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
    }

    /** Validate inputs before requesting OTP **/
    private fun validateForOtp(): Boolean {
        val fullName = fullNameInput.text.toString().trim()
        val phone = phoneInput.text.toString()

        if (fullName.isEmpty() || phone.isEmpty() || selectedColonyId == null) {
            Toast.makeText(context, "Please enter valid name, phone, and area.", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.length != 10) {
            Toast.makeText(context, "Phone number must be 10 digits.", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /** Validate inputs for final signup **/
    private fun validateInputs(): Boolean {
        if (fullNameInput.text.isEmpty() || phoneInput.text.isEmpty() || otpInput.text.isEmpty() || selectedColonyId.isNullOrEmpty()) {
            Log.d("COLONY ID", selectedColonyId.toString())
            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /** Store signup response **/
    private fun saveSignUpData(signUpResponse: SignUpResponse) {
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("customer_id", signUpResponse.ragistration_id)
            putString("customer_role", signUpResponse.customer_role)
            apply()
        }
    }

    /** Start OTP Timer **/
    private fun startOTPTimer() {
        val sharedPref = requireContext().getSharedPreferences("otp_timer", Context.MODE_PRIVATE)
        val startTime = sharedPref.getLong("start_time", 0L)
        val currentTime = System.currentTimeMillis()

        val elapsedTime = currentTime - startTime
        val totalDuration = 59000L // 1 minute in milliseconds
        val remainingTime = totalDuration - elapsedTime

        if (remainingTime > 0) {
            // Start the countdown timer
            countDownTimer = object : CountDownTimer(remainingTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if(isAdded && context != null) {
                        val minutesRemaining = millisUntilFinished / 1000 / 60
                        val secondsRemaining = millisUntilFinished / 1000
                        mGetOTP.text = String.format(
                            Locale.getDefault(),
                            "%02d:%02d",
                            minutesRemaining,
                            secondsRemaining
                        )

                        // Set text style and color
                        mGetOTP.setTextColor(ContextCompat.getColor(requireContext(),R.color.disabled))
                        mGetOTP.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_regular)
                        mGetOTP.isEnabled = false
                    }
                }

                override fun onFinish() {
                    if (isAdded && context != null) {
                        mGetOTP.text = getString(R.string.resendOTP) // Restore the original text
                        mGetOTP.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                        mGetOTP.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
                        mGetOTP.isEnabled = true // Enable the button again
                    }
                }
            }.start()
        } else {
            // If the timer already expired, reset the button
            mGetOTP.text = getString(R.string.getOTP)
            mGetOTP.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
            mGetOTP.typeface = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)
            mGetOTP.isEnabled = true
        }
    }

    // Function to capitalize the first letter of each word
    private fun capitalizeWords(text: String): String {
        return text.split(" ").joinToString(" ") { it.capitalize() }
    }

    // Split full name into first and last name
    private fun splitNames(fullName: String): Pair<String, String> {
        val nameParts = fullName.trim().split(" ")
        val firstName = if (nameParts.isNotEmpty()) nameParts[0] else ""
        val lastName = if (nameParts.size > 1) nameParts[nameParts.size - 1] else ""
        return Pair(firstName, lastName)
    }

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel() // Cancel the timer when fragment is paused
        countDownTimer = null // Clear the reference
    }

    override fun onResume() {
        super.onResume()
        startOTPTimer() // Resume the timer when the fragment is visible again
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        countDownTimer = null
    }
}
