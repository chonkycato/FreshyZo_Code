package com.example.freshyzo.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
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

        var mPhoneNumber = mPhoneInput.text.toString()
        var mOtpEntered = mOtpInput.text.toString()

        Log.d("Phone Number:", mPhoneNumber)
        Log.d("OTP:", mOtpEntered)


        return view
    }

}