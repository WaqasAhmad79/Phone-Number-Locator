package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdConfig
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdHelper
import com.example.phonenumberlocator.databinding.ActivityCallLocBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.ui.activities.callLocator.PNLCallLocatorActivity
import com.example.phonenumberlocator.ui.activities.callLocator.PNLIsdStdActivity
import com.example.phonenumberlocator.ui.activities.callLocator.PNLPhoneContactsActivity

class CallLocActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallLocBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallLocBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleBannerAd()
        handleClicks()
        hideNavBar()
    }

    private fun handleBannerAd() {
        if (RemoteConfigClass.banner_call_loc_activity && isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()
                val config = BannerAdConfig(
                    getString(R.string.adaptive_mob_banner_id),
                    canShowAds = true,
                    canReloadAds = true,
                    isCollapsibleAd = true
                )
                val bannerAdHelperClass = BannerAdHelper(
                    activity = this,
                    lifecycleOwner = this,
                    config = config
                )
                bannerAdHelperClass.myView = binding.ads
                bannerAdHelperClass.shimmer = binding.bannerView.customBannerShimmer
                bannerAdHelperClass.loadAndShowCollapsibleBannerAd()

        } else {
            binding.ads.beGone()
        }
    }

    private fun handleClicks() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.searchNumber.setOnClickListener {
            startActivity(Intent(this, PNLCallLocatorActivity::class.java))
        }
        binding.phoneContacts.setOnClickListener {
            startActivity(Intent(this, PNLPhoneContactsActivity::class.java))
        }
        binding.isdStd.setOnClickListener {
            startActivity(Intent(this, PNLIsdStdActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("showSimpleInterstitialAdNew8", "onPause: ")
    }

}