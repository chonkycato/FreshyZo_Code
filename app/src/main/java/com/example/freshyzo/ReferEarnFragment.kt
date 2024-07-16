package com.example.freshyzo

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ReferEarnFragment : Fragment() {

    private lateinit var refCodeTextView : TextView
    private lateinit var copyCodeTextView: TextView
    private lateinit var shareInviteTextView: TextView
    private lateinit var referralCode : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_refer_earn, container, false)
        (activity as MainActivity).hideBottomNav()

        refCodeTextView = view.findViewById(R.id.referral_code)
        copyCodeTextView = view.findViewById(R.id.copyCodeTV)
        shareInviteTextView = view.findViewById(R.id.inviteTV)
        referralCode = refCodeTextView.text.toString()

        copyCodeTextView.setOnClickListener {
            copyReferralCode()
            Toast.makeText(
                requireContext(),
                "Referral code copied successfully!",
                Toast.LENGTH_SHORT
            ).show()

        }

        shareInviteTextView.setOnClickListener {
            copyReferralCode()
            val inviteCode = "Hey! Use the code $referralCode when you join FreshyZo and avail discount on products across platform."
            val shareIntent = Intent().apply{
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, inviteCode)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        return view
    }

    private fun copyReferralCode(){
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("referralCode", referralCode)
        clipboard.setPrimaryClip(clip)
    }
}