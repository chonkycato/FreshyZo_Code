package com.example.freshyzo.login

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.freshyzo.HomeFragment
import com.example.freshyzo.MainActivity
import com.example.freshyzo.R
import com.example.freshyzo.api.ApiHandler
import com.example.freshyzo.model.LoginResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class LoginFragment : Fragment() {

    private lateinit var apiHandler: ApiHandler
    private lateinit var mLoginButton: Button
    private lateinit var mOtpInput: EditText
    private lateinit var mGetOTP: TextView
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        /** Handle top and bottom nav**/
        (activity as MainActivity).handleNavigationToolbar(null, false)

        // Initialize views and API handler
        val mPhoneInput = view.findViewById<EditText>(R.id.phoneInput)
        mOtpInput = view.findViewById(R.id.otpInput)
        mLoginButton = view.findViewById(R.id.loginButton)
        mGetOTP = view.findViewById(R.id.getOTP)
        val mSignUpInstead = view.findViewById<TextView>(R.id.signUpInstead)
        apiHandler = ApiHandler()

        // Disable login button until OTP is entered
        mLoginButton.isEnabled = false
        mOtpInput.addTextChangedListener { validateOTPInput() }

        mGetOTP.setOnClickListener {
            val phoneNumber = mPhoneInput.text.toString()
            val method = "login"
            if (isValidPhoneNumber(phoneNumber)) {
                requestOTP(phoneNumber, method)
                Toast.makeText(requireContext(), "OTP Sent Successfully!", Toast.LENGTH_SHORT).show()

                //Store start time of timer
                val sharedPref = requireContext().getSharedPreferences("otp_timer", Context.MODE_PRIVATE)
                val startTime = System.currentTimeMillis() // Current time in milliseconds
                with(sharedPref.edit()) {
                    putLong("start_time", startTime)
                    apply()
                }

            } else {
                Toast.makeText(requireContext(), "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
            }
        }

        mLoginButton.setOnClickListener {
            val otp = mOtpInput.text.toString()
            val phoneNumber = mPhoneInput.text.toString()
            if (isValidOTP(otp) && isValidPhoneNumber(phoneNumber)) {
                verifyLogin(phoneNumber, otp)
            } else {
                showToast("Invalid OTP!")
            }
        }

        mSignUpInstead.setOnClickListener {
            (activity as MainActivity).loadFragment(SignupFragment(), false, null)
        }

        return view
    }


    /** OTP Timer **/
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
                        mGetOTP.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.disabled
                            )
                        )
                        mGetOTP.typeface =
                            ResourcesCompat.getFont(requireContext(), R.font.roboto_regular)
                        mGetOTP.isEnabled = false
                    }
                }

                override fun onFinish() {
                    if (isAdded && context != null) {
                        mGetOTP.text = getString(R.string.resendOTP) // Restore the original text
                        mGetOTP.isEnabled = true // Enable the button again
                    }
                }
            }.start()
        } else {
            // If the timer already expired, reset the button
            mGetOTP.text = getString(R.string.getOTP)
            mGetOTP.isEnabled = true
        }
    }

    /** Validate OTP input and enable login button **/
    private fun validateOTPInput() {
        val isLoggedIn = (activity as MainActivity).isLoggedIn()
        Log.d("isLoggedIn", isLoggedIn.toString())
        mLoginButton.isEnabled = mOtpInput.text.toString().length == 6
    }

    /** OTP request API call **/
    private fun requestOTP(phoneNumber: String, method: String) {
        apiHandler.requestOTP(phoneNumber, method, object : Callback<ResponseBody> { // Change OneTimePasswordResponse to ResponseBody
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val rawResponse = response.body()?.string() // Get the raw response as plain text

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

    /** login verification API call **/
    private fun verifyLogin(phoneNumber: String, otp: String) {
        apiHandler.verifyLogin(phoneNumber, otp, object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        Log.d("Saved Login Data: ", " ${response.body()}")
                        saveLoginData(loginResponse)
                        (activity as MainActivity).changeLoginState(true)
                        (activity as MainActivity).clearBackStack()
                        (activity as MainActivity).loadFragment(HomeFragment(), true, null)
                    } else {
                        showToast("Login failed!")
                    }
                } else {
                    showToast("Login failed! Please try again.")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LOGIN ERROR", t.message.toString())
                showToast("Login failed: ${t.message}")
            }
        })
    }

    // Save login data in SharedPreferences
    private fun saveLoginData(loginResponse: LoginResponse) {
        val sharedPref = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            Log.d("Saved Login Data: ${loginResponse.customerID} ", "${loginResponse.customerRole}")
            putString("customer_id", loginResponse.customerID)
            putString("customer_role", loginResponse.customerRole)
            apply()
        }
    }

    // Validate phone number
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
    }

    // Validate OTP
    private fun isValidOTP(otp: String): Boolean {
        return otp.length == 6 && otp.all { it.isDigit() }
    }

    // Utility function for showing toast messages
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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
    }
}
