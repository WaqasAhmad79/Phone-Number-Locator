package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.interstitialAdPriority
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.showPriorityInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.databinding.ActivityCallLocBinding
import com.example.phonenumberlocator.ui.activities.callLocator.PNLCallLocatorActivity
import com.example.phonenumberlocator.ui.activities.callLocator.PNLIsdStdActivity
import com.example.phonenumberlocator.ui.activities.callLocator.PNLPhoneContactsActivity

class CallLocActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCallLocBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallLocBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* showPriorityInterstitialAdWithTimeAndCounter(
            true,
            getString(R.string.admob_interistitial_search_high),
            getString(R.string.admob_interistitial_others_one)
            , {
                interstitialAdPriority=it
            })*/
//        showSimpleInterstitialAdWithTimeAndCounter()
        handleClicks()

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