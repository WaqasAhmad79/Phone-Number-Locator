package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdConfig
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdHelper
import com.example.phonenumberlocator.databinding.ActivityGpsTrackBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.ui.activities.gpsTracker.GpsLocationActivity
import com.example.phonenumberlocator.ui.activities.gpsTracker.PNLFindAddressActivity
import com.example.phonenumberlocator.ui.activities.gpsTracker.PNLShareLocationActivity

class GpsTrackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGpsTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpsTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleClicks()
        handleBannerAd()
        hideNavBar()

    }

    fun handleBannerAd() {

        if (RemoteConfigClass.banner_gps_track_activity) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()
                val config = BannerAdConfig(
                    getString(R.string.ad_mob_banner_id), true, true, true
                )
                val bannerAdHelperClass = BannerAdHelper(this, this, config)
                bannerAdHelperClass.myView = binding.ads
                bannerAdHelperClass.shimmer = binding.bannerView.customBannerShimmer
                bannerAdHelperClass.showBannerAdmob()
            }
        } else {
            binding.ads.beGone()
        }

    }

    private fun handleClicks() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.gpsLocation.setOnClickListener {

            startActivity(Intent(this, GpsLocationActivity::class.java))

        }
        binding.shareLocation.setOnClickListener {

            startActivity(Intent(this, PNLShareLocationActivity::class.java))

        }
        binding.findAddress.setOnClickListener {

            startActivity(Intent(this, PNLFindAddressActivity::class.java))

        }
    }
}