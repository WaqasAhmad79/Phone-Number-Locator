package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.loadCollapsibleBanner
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.databinding.ActivityGpsTrackBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.ui.activities.camAddress.PNLAreaCalculatorActivity
import com.example.phonenumberlocator.ui.activities.camAddress.PNLDistanceFinderActivity
import com.example.phonenumberlocator.ui.activities.camAddress.PNLGpsAddressActivity
import com.example.phonenumberlocator.ui.activities.gpsTracker.GpsLocationActivity
import com.example.phonenumberlocator.ui.activities.gpsTracker.PNLFindAddressActivity
import com.example.phonenumberlocator.ui.activities.gpsTracker.PNLShareLocationActivity

class GpsTrackActivity : AppCompatActivity() {

    private lateinit var binding:ActivityGpsTrackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGpsTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleClicks()

        if (isNetworkAvailable()){
            binding.ads.beVisible()
            loadCollapsibleBanner(this,getString(R.string.adaptive_mob_banner_id),binding.ads)
        }else{
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