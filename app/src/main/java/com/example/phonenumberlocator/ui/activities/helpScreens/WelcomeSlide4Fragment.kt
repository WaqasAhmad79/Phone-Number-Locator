package com.example.phonenumberlocator.ui.activities.helpScreens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.canShowAppOpen
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.databinding.ActivityAppPermissionBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlHelper.PERMISSION_ACCESS_FINE_LOCATION


class WelcomeSlide4Fragment : PNLPermissionBaseFragment() {
    private lateinit var binding: ActivityAppPermissionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= ActivityAppPermissionBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        canShowAppOpen = true
        Log.d("TAG42", "onResume: OnboardingScreen4Fragment")
    }

    override fun onDestroy() {
        super.onDestroy()
//        showAppOpenAds = false
        canShowAppOpen = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAG42", "onViewCreated:4 ")
        binding.permissionSwitch.setOnToggledListener() { _, isChecked ->
            handlePermission(PERMISSION_ACCESS_FINE_LOCATION){
                binding.permissionSwitch.isOn = it
            }
        }

        nativeAdControl()
    }

    private fun nativeAdControl() {
        activity?.let {
            if (it.isNetworkAvailable()) {
                binding.flAdNative.beVisible()
                PhoneNumberLocator.onBoardNative4.observe(viewLifecycleOwner){ nad->
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
