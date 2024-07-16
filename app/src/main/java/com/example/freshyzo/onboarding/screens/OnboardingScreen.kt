package com.example.freshyzo.onboarding.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.freshyzo.MainActivity
import com.example.freshyzo.R
import com.example.freshyzo.login.LoginFragment


class OnboardingScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_onboarding, container, false)

        val imageViewGIF = view.findViewById<ImageView>(R.id.imageView_gif)
        val imageViewBG = view.findViewById<ImageView>(R.id.green_background)
        val backTv = view.findViewById<TextView>(R.id.back)
        val nextTv = view.findViewById<TextView>(R.id.next)
        val skipTv = view.findViewById<TextView>(R.id.skip)
        val titleTv = view.findViewById<TextView>(R.id.title_heading)
        val subTitleTv = view.findViewById<TextView>(R.id.sub_title)

        var navCount = 0

        //Load a GIF into image view
        Glide.with(this)
            .load(R.drawable.gif_onboarding_one)
            .override(830, 726)
            .into(imageViewGIF)

        nextTv.setOnClickListener {
            navCount += 1
            navigateOnboarding(
                navCount,
                imageViewGIF,
                imageViewBG,
                nextTv,
                backTv,
                skipTv,
                titleTv,
                subTitleTv
            )
        }

        backTv.setOnClickListener {
            navCount -= 1
            navigateOnboarding(
                navCount,
                imageViewGIF,
                imageViewBG,
                nextTv,
                backTv,
                skipTv,
                titleTv,
                subTitleTv
            )

        }

        skipTv.setOnClickListener {
//            findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
            (activity as MainActivity).loadFragment(LoginFragment(), true, null)
            (activity as MainActivity?)!!.onBoardingFinished()
        }
        return view
    }

    private fun navigateOnboarding(
        navCount: Int,
        imageView: ImageView,
        imageViewBG: ImageView,
        next: TextView,
        back: TextView,
        skip: TextView,
        title: TextView,
        subTitle: TextView
    ) {
        when (navCount) {

            0 -> {
                back.visibility = View.INVISIBLE
                skip.visibility = View.VISIBLE

                title.text = getString(R.string.title_ffs)
                subTitle.text = getString(R.string.sub_title_ffs)
                imageViewBG.setImageResource(R.drawable.img_background_one)

                Glide.with(this).load(R.drawable.gif_onboarding_one).override(830, 726)
                    .into(imageView)
            }

            1 -> {
                back.visibility = View.VISIBLE
                //skip.visibility = View.VISIBLE

                title.text = getString(R.string.title_fss)
                subTitle.text = getString(R.string.sub_title_fss)
                imageViewBG.setImageResource(R.drawable.img_background_two)

                Glide.with(this).load(R.drawable.gif_onboarding_two).override(830, 726)
                    .into(imageView)
            }

            2 -> {
                //skip.visibility = View.INVISIBLE
                back.visibility = View.VISIBLE

                title.text = getString(R.string.title_fts)
                subTitle.text = getString(R.string.sub_title_fts)
                imageViewBG.setImageResource(R.drawable.img_background_three)

                Glide.with(this).load(R.drawable.gif_onboarding_three).override(830, 726)
                    .into(imageView)

                next.setOnClickListener {
//                    findNavController().navigate(R.id.action_viewPagerFragment_to_loginFragment)
                    (activity as MainActivity).loadFragment(LoginFragment(), clearBackStack = true, null)
                    (activity as MainActivity?)!!.onBoardingFinished()
                }
            }
        }
    }
}