package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityGpsTrackBinding
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

    }

    private fun handleClicks() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.gpsLocation.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLLiveWeatherActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, GpsLocationActivity::class.java))
//            }

        }
        binding.shareLocation.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLLiveTrafficActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, PNLShareLocationActivity::class.java))
//            }

        }
        binding.findAddress.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLNearbyPlacesActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, PNLFindAddressActivity::class.java))
//            }

        }
    }
}