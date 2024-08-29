package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdWelcome
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdWelcomeDup
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative1
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.native_ad.NativeAdConfig
import com.example.phonenumberlocator.admob_ads.native_ad.NativeAdHelper
import com.example.phonenumberlocator.databinding.ActivityWelcomeScreenBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.ui.activities.helpScreens.PNLIntroSliderActivity

class WelcomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        handleAds()
        hideNavBar()

    }

    private fun handleAds() {

        // showing first ad that was loaded in language screen
        if (RemoteConfigClass.native_welcome_screen_activity && isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {

            binding.ads.visibility = View.VISIBLE
            nativeAdWelcome.observe(this) { nad ->
                nad?.let { ad ->
                    val config = NativeAdConfig(
                        resources.getString(R.string.admob_native_large),
                        canShowAds = true,
                        canReloadAds = true,
                        layoutId = R.layout.native_ad_03
                    )
                    val nativeAdHelper = NativeAdHelper(this, this, config).apply {
                        TAG = "WelcomeScreenActivity"
                        shimmerLayoutView = binding.includeShimmer.shimmerContainerNative
                        nativeContentView = binding.ads
                    }
                    nativeAdHelper.showLoadedNativeAd(ad)

                }
            }
        } else {
            binding.ads.beGone()
        }

        // Loading Native second ad for welcome screen (native_welcome_screen_dup_activity)
        if (RemoteConfigClass.native_welcome_screen_dup_activity) {
            loadAndReturnAd(
                this@WelcomeScreenActivity, resources.getString(R.string.admob_native_large)
            ) { it1 ->
                Log.d("nativeAdWelcome ", "value: $it1")
                if (it1 != null) {
                    nativeAdWelcomeDup.value = it1
                }
            }
        } else {
            nativeAdWelcomeDup.value = null
        }

        // Loading Native ad for on boarding (welcome slide 1 fragment)
        if (
            RemoteConfigClass.native_welcome_one
            && PhoneNumberLocator.canRequestAd
            && isNetworkAvailable()
        ) {
            loadAndReturnAd(
                this@WelcomeScreenActivity, resources.getString(R.string.admob_native_boarding_low)
            ) { it2 ->
                if (it2 != null) {
                    onBoardNative1.value = it2
                }
            }
        }

    }

    private fun initListeners() {

        binding.apply {

            btnNext.setOnClickListener {

                if ((phoneTrackerRadio.isChecked || GPSTrackerRadio.isChecked)) {

                    startActivity(
                        Intent(
                            this@WelcomeScreenActivity,
                            PNLIntroSliderActivity::class.java
                        )
                    )
                } else {
                    Toast.makeText(
                        this@WelcomeScreenActivity,
                        "Please select a service first",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            cvPhoneTracker.setOnClickListener {
                phoneTrackerRadio.isChecked = true
                GPSTrackerRadio.isChecked = false
                showSecondAdDup()
            }

            cvGPSTracker.setOnClickListener {
                GPSTrackerRadio.isChecked = true
                phoneTrackerRadio.isChecked = false
                showSecondAdDup()
            }
        }

    }

    private fun showSecondAdDup() {

        // to be shown on click of any one radio button
        if (RemoteConfigClass.native_welcome_screen_dup_activity && isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {

            binding.ads.visibility = View.VISIBLE
            nativeAdWelcomeDup.observe(this) { nativeAd ->

                val config = NativeAdConfig(
                    resources.getString(R.string.admob_native_large),
                    canShowAds = true,
                    canReloadAds = true,
                    layoutId = R.layout.native_ad_03
                )
                val nativeAdHelper = NativeAdHelper(this, this, config).apply {
                    TAG = "WelcomeScreenActivity"
                    shimmerLayoutView = binding.includeShimmer.shimmerContainerNative
                    nativeContentView = binding.ads
                }
                nativeAdHelper.showLoadedNativeAd(nativeAd)
            }
        } else {
            binding.ads.beGone()
        }

    }

}