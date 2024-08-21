package com.example.phonenumberlocator.ui.activities.helpScreens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative3
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative4
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.databinding.FragmentWelcomeSlide2Binding
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable

class WelcomeSlide2Fragment : Fragment() {
    private lateinit var binding: FragmentWelcomeSlide2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeSlide2Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG42", "onViewCreated: 2")
        nativeAdControl()
    }


    override fun onResume() {
        super.onResume()
        Log.d("TAG42", "onResume: OnboardingScreen2Fragment")
    }


    private fun nativeAdControl() {

        // Loading ad for the onboarding welcome slide 3
        if (
            RemoteConfigClass.native_welcome_three
            && PhoneNumberLocator.canRequestAd
            && requireContext().isNetworkAvailable()
        ) {
            loadAndReturnAd(
                requireActivity(),
                resources.getString(R.string.admob_native_boarding_low)
            ) { it2 ->
                if (it2 != null) {
                    onBoardNative3.value = it2
                } else {
                    onBoardNative3.postValue(null)
                }
            }
        }

        // Loading ad for the onboarding welcome slide 4
        if (
            RemoteConfigClass.native_welcome_four
            && PhoneNumberLocator.canRequestAd
            && requireContext().isNetworkAvailable()
        ) {
            loadAndReturnAd(
                requireActivity(), resources.getString(R.string.admob_native_boarding_low)
            ) { it2 ->
                if (it2 != null) {
                    onBoardNative4.value = it2
                } else {
                    onBoardNative4.postValue(null)
                }
            }
        }


    }

}
