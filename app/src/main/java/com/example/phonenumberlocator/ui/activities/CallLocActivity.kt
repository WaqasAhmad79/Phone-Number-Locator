package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.BannerState
import com.example.phonenumberlocator.admob_ads.loadCollapsibleBanner
import com.example.phonenumberlocator.databinding.ActivityCallLocBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
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

        handleClicks()
        if (isNetworkAvailable()) {
            binding.ads.beVisible()
            loadCollapsibleBanner(this, getString(R.string.adaptive_mob_banner_id), binding.ads)
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

  /*  fun showBannerAds() {
        if (isNetworkAvailable()) {
            binding.ads.beVisible()
            loadCollapsibleBanner(this, getString(R.string.adaptive_mob_banner_id), binding.ads) {
                if (it == BannerState.LOADED) {
                    binding.bannerView.customBannerShimmer.stopShimmer()
                    binding.bannerView.customBannerShimmer.beGone()
                    binding.bannerView.bannerContainer.beVisible()
                } else {
                    binding.ads.beGone()
                }
            }
        } else {
            binding.ads.beGone()
        }
    }*/

}