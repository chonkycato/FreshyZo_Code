package com.example.freshyzo.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.freshyzo.MainActivity
import com.example.freshyzo.R

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment. newInstance] factory method to
 * create an instance of this fragment.
 */

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val mPhoneInput = view.findViewById<EditText>(R.id.phoneInput)
        val mOtpInput = view.findViewById<EditText>(R.id.otpInput)
        val mLoginButton = view.findViewById<Button>(R.id.loginButton)
        val mGetOtp = view.findViewById<TextView>(R.id.getOTP)
        val mSignUpInstead = view.findViewById<TextView>(R.id.signUpInstead)


        mLoginButton.setOnClickListener {
            val sampleOTP: Long = 123456

            val phoneString = mPhoneInput.text.toString()
            val otpString = mOtpInput.text.toString()

            if ((phoneString.isNotEmpty() && otpString.isNotEmpty()) && (phoneString.length == 10 && otpString.length == 6)) {
                try {
                    val mPhoneNumber = phoneString.toLong()
                    val mOtpEntered = otpString.toLong()
                    if (mOtpEntered == sampleOTP) {
                        (activity as MainActivity).loginState(true)
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment2)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Invalid OTP or Phone Number!",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        requireContext(),
                        "Login failed, please try again later.",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else if (phoneString.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please enter the phone number",
                    Toast.LENGTH_SHORT)
                    .show()
            }
            else if(otpString.isEmpty()){
                Toast.makeText(
                    requireContext(),
                    "Please enter the OTP",
                    Toast.LENGTH_SHORT).show()
            }
            else if ((phoneString.length != 10) || (otpString.length != 6)) {
                Toast.makeText(
                    requireContext(),
                    "Invalid OTP or Phone Number",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }

        mSignUpInstead.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        mGetOtp.setOnClickListener {
            val otp = (activity as MainActivity).generateOTP()
            Toast.makeText(context, "Your OTP is $otp", Toast.LENGTH_LONG).show()
        }
        return view
    }



}