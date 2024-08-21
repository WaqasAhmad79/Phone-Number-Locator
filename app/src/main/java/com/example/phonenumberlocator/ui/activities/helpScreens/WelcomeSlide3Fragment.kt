package com.example.phonenumberlocator.ui.activities.helpScreens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.databinding.FragmentWelcomeSlide3Binding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable


class WelcomeSlide3Fragment : Fragment() {
    private lateinit var binding: FragmentWelcomeSlide3Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeSlide3Binding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nativeAdControl()
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG42", "onResume: OnboardingScreen3Fragment")
    }

    private fun nativeAdControl() {
        activity?.let {
            if (RemoteConfigClass.native_welcome_three) {
                if (it.isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                    binding.flAdNative.beVisible()
                    PhoneNumberLocator.onBoardNative3.observe(viewLifecycleOwner) { nad ->
                        nad?.let { ad ->
                            showLoadedNativeAd(
                                requireActivity(),
                                binding.flAdNative,
                                binding.includeShimmer.shimmerContainerNative,
                                R.layout.layout_admob_native_ad_full_scr,
                                ad
                            )
                        }
                    }
                }
            } else {
                binding.flAdNative.beGone()
            }
        }
    }
}
