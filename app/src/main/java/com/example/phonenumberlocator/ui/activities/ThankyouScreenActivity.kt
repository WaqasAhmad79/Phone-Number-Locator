package com.example.phonenumberlocator.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.native_ad.NativeAdConfig
import com.example.phonenumberlocator.admob_ads.native_ad.NativeAdHelper
import com.example.phonenumberlocator.databinding.ActivityThankyouScreenBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable

class ThankyouScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThankyouScreenBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityThankyouScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideNavBar()
        showAd()
        handler.postDelayed({
            finishAffinity()
        }, 6000)


    }

    private fun showAd() { // for native ads
        if (RemoteConfigClass.native_exit_ad) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()

                val config = NativeAdConfig(
                    resources.getString(R.string.admob_native_small),
                    canShowAds = true,
                    canReloadAds = true,
                    layoutId = R.layout.native_ad_06
                )
                val nativeAdHelper = NativeAdHelper(this, this, config).apply {
                    TAG = "ThankyouScreenActivity"
                    shimmerLayoutView = binding.includeShimmer.shimmerContainerNative
                    nativeContentView = binding.ads
                }
                nativeAdHelper.loadAndShowNativeAd()


            }
        } else {
            binding.ads.beGone()
        }
    }


}