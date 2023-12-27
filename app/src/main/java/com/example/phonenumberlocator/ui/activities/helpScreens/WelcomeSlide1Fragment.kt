package com.example.phonenumberlocator.ui.activities.helpScreens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.databinding.FragmentWelcomeSlide1Binding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable


class WelcomeSlide1Fragment: Fragment() {
    private lateinit var binding: FragmentWelcomeSlide1Binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentWelcomeSlide1Binding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG42", "onViewCreated: 1")
        nativeAdControl()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG42", "onResume: OnboardingScreen1Fragment")
    }


    private fun nativeAdControl() {
        activity?.let {
            if (it.isNetworkAvailable()) {
                binding.flAdNative.beVisible()
                PhoneNumberLocator.onBoardNative1.observe(viewLifecycleOwner){ nad->
                    nad?.let {ad->
                        showLoadedNativeAd(
                            requireActivity(),
                            binding.flAdNative,
                            R.layout.layout_admob_native_ad, ad
                        )
                    }
                }
            } else {
                binding.flAdNative.beGone()
            }
        }
    }

}
