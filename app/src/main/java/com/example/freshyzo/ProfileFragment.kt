package com.example.freshyzo

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    private lateinit var mProfilePictureIV: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        (activity as MainActivity).setBottomNavVisibility(false)


//        mProfilePictureIV = view.findViewById<ImageView>(R.id.profile_picture)
//        val mFirstNameET = view.findViewById<EditText>(R.id.first_name)
//        val mLastNameET = view.findViewById<EditText>(R.id.last_name)
//        val mPhoneNumberET = view.findViewById<EditText>(R.id.phone_number)
//        val mEmailAddressET = view.findViewById<EditText>(R.id.email_address)
//        val mEditPfpIV = view.findViewById<TextView>(R.id.edit_profile_picture)
//        val mSubmitProfileBtn = view.findViewById<Button>(R.id.submitBtnProfile)
        val mBackBtn = view.findViewById<Button>(R.id.back_icon_profile)
        val mSearchIconIV = view.findViewById<ImageView>(R.id.search_icon_prof)

        mBackBtn.setOnClickListener {
           Toast.makeText(context, "BACK PRESSED", Toast.LENGTH_SHORT).show()
            (activity as MainActivity).loadFragment(AccountFragment(), false)
        }


//        mEditPfpIV.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//            startActivityForResult(gallery, pickImage)
//        }
//
//        mSubmitProfileBtn.setOnClickListener {
//            val firstName = mFirstNameET.text.toString()
//            val lastName = mLastNameET.text.toString()
//            val phoneNumber = mPhoneNumberET.text.toString()
//            val emailAddress = mEmailAddressET.text.toString()
//            Toast.makeText(context, firstName, Toast.LENGTH_SHORT).show()
//        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            mProfilePictureIV.setImageURI(imageUri)
        }
    }

}