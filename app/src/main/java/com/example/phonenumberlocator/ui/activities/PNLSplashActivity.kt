package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLang
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative1
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative2
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative3
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative4
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.AdsConsentManager
import com.example.phonenumberlocator.admob_ads.OpenApp
import com.example.phonenumberlocator.admob_ads.isShowAD
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.loadSplashAdmobInterstitial
import com.example.phonenumberlocator.databinding.ActivityPnlsplashBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlSharedPreferencesLang.PNLMySharePreferences
import com.example.phonenumberlocator.pnlUtil.setCurrentLocale
import com.example.phonenumberlocator.ui.MainActivity
import com.example.phonenumberlocator.ui.activities.helpScreens.PNLIntroSliderActivity
import com.google.android.gms.ads.MobileAds

class PNLSplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnlsplashBinding

    var started = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnlsplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCurrentLocale(baseConfig.appLanguage.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        } else {
            val w: Window = this.window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            )
        }


//51B1AB5BA7D2641277DFA65518CC6E7A for oppo
        val adsConsentManager = AdsConsentManager(this)


//        adsConsentManager.requestUMP(
//            true,
////            "0254F76DB20D82B091C239339F2DE723",
//            "ADBD1C00EB2F9B3B519E880F72F8F341",
//            true
//        ) { canRequestAds ->
//            runOnUiThread(this::splashEnd)
//            Log.e("AdsConsentManager", "adsConsentManager response :$canRequestAds ")
//            if (canRequestAds) {
//                handleConsentFormAndAdsRequests()
//            } else {
//                splashEnd()
//            }
//        }


        adsConsentManager.requestUMP { canRequest ->
            Log.e("AdsConsentManager", "adsConsentManager response :$canRequest ")
            runOnUiThread(this::splashEnd)
            if (canRequest) {
                handleConsentFormAndAdsRequests()
            } else {
                splashEnd()
            }
        }


        val mySharePreferences = PNLMySharePreferences(this)
        started = mySharePreferences.appStarted


        binding.button.setOnClickListener {
            startActivity(Intent(this@PNLSplashActivity, PNLLanguageActivity::class.java))
            finish()
        }

    }

    private fun splashEnd() {
        Handler(Looper.getMainLooper()).postDelayed({

            if (!baseConfig.appStarted) {
                binding.button.beVisible()
                binding.progressBar.beGone()
                // The user has seen the Onboard

            } else if (!baseConfig.isAppIntroComplete) {
                startActivity(
                    Intent(
                        this@PNLSplashActivity, PNLIntroSliderActivity::class.java
                    ).putExtra("isComingFromSplash", true)
                )
                finish()
            } else {
                isShowAD = true
                startActivity(
                    Intent(
                        this@PNLSplashActivity, MainActivity::class.java
                    )
                )
                finish()
            }
        }, 8000)

    }


    private fun handleConsentFormAndAdsRequests() {
        PhoneNumberLocator.instance.let {
            OpenApp.initialize(it)
            OpenApp.fetchAd()
        }

        // consent done .............load ads here
        loadSplashAdmobInterstitial()
        handleAds()
    }

    private fun handleAds() {
        if (!baseConfig.appStarted) {
            if (isNetworkAvailable()) {
                loadAndReturnAd(
                    this@PNLSplashActivity, resources.getString(R.string.admob_native_lang_low)
                ) { it2 ->
                    if (it2 != null) {
                        nativeAdLang.value = it2
                    }
                }
            } else {
                nativeAdLang.postValue(null)
            }
        }
//        loadAndReturnAd(
//            this@PNLSplashActivity, resources.getString(R.string.admob_native_main_low)
//        ) { it2 ->
//            if (it2 != null) {
//                nativeAdMain.value = it2
//            }
//        }
        if (!baseConfig.isAppIntroComplete) {
            loadAndReturnAd(
                this@PNLSplashActivity, resources.getString(R.string.admob_native_boarding_low)
            ) { it2 ->
                if (it2 != null) {
                    onBoardNative1.value = it2
                }
            }

            loadAndReturnAd(
                this@PNLSplashActivity, resources.getString(R.string.admob_native_boarding_low)
            ) { it2 ->
                if (it2 != null) {
                    onBoardNative2.value = it2
                }
            }

            loadAndReturnAd(
                this@PNLSplashActivity, resources.getString(R.string.admob_native_boarding_low)
            ) { it2 ->
                if (it2 != null) {
                    onBoardNative3.value = it2
                }
            }

            loadAndReturnAd(
                this@PNLSplashActivity, resources.getString(R.string.admob_native_boarding_low)
            ) { it2 ->
                if (it2 != null) {
                    onBoardNative4.value = it2
                }
            }
        }

    }

}