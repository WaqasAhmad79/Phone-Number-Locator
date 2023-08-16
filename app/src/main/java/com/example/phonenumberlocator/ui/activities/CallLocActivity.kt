package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.phonenumberlocator.R
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
        handleClicks()
    }

    private fun handleClicks() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.searchNumber.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLLiveWeatherActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, PNLCallLocatorActivity::class.java))
//            }

        }
        binding.phoneContacts.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLLiveTrafficActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, PNLPhoneContactsActivity::class.java))
//            }

        }
        binding.isdStd.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLNearbyPlacesActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, PNLIsdStdActivity::class.java))
//            }

        }
    }
}