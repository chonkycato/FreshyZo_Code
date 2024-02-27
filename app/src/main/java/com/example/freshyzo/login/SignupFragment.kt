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
 * Use the [SignupFragment. newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        val mNameInput = view.findViewById<EditText>(R.id.nameInput)
        val mPhoneInput = view.findViewById<EditText>(R.id.phoneInput)
        val mOTPInput = view.findViewById<EditText>(R.id.otpInput)
        val mGetOTP = view.findViewById<TextView>(R.id.getOTP)
        val mSignupButton = view.findViewById<Button>(R.id.signupButton)
        val mLoginInstead = view.findViewById<TextView>(R.id.loginInstead)

        mSignupButton.setOnClickListener{
            val mUserName = mNameInput.text.toString()
            val mPhoneNumber = mPhoneInput.text.toString().toLong()
            val mOTPEntered = mOTPInput.text.toString().toLong()
            val mSampleOTP = 123456.toLong()
            if (mSampleOTP == mOTPEntered){
                findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                Toast.makeText(requireContext(), "$mUserName $mPhoneNumber $mOTPEntered", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(), "Invalid OTP or Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        mLoginInstead.setOnClickListener{
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        mGetOTP.setOnClickListener{
            val otp = (activity as MainActivity).generateOTP()
            Toast.makeText(context, "Your OTP is $otp", Toast.LENGTH_LONG).show()
        }

        return view

    }

}