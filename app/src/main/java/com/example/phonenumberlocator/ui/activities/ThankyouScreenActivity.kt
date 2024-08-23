package com.example.phonenumberlocator.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdExit
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.databinding.ActivityThankyouScreenBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable

class ThankyouScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThankyouScreenBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityThankyouScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showAd()
        handler.postDelayed({
            finishAffinity()
        }, 5000)
    }

    private fun showAd() { // for native ads
        if (RemoteConfigClass.native_exit_ad) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()
                nativeAdExit.observe(this) { it2 ->
                    showLoadedNativeAd(
                        this,
                        binding.ads,
                        binding.includeShimmer.shimmerContainerNative,
                        R.layout.layout_admob_native_ad_withou_tmedia,
                        it2
                    )
                }
            }
        } else {
            binding.ads.beGone()
        }
    }


}