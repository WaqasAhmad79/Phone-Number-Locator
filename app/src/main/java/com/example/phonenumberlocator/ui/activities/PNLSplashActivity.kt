package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLang
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative1
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative2
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative3
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.onBoardNative4
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.AdsConsentManager
import com.example.phonenumberlocator.admob_ads.interstitialAdPriority
import com.example.phonenumberlocator.admob_ads.isShowAD
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.loadHighOrLowNativeAd
import com.example.phonenumberlocator.admob_ads.loadPriorityAdmobInterstitial
import com.example.phonenumberlocator.databinding.ActivityPnlsplashBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlUtil.setCurrentLocale
import com.example.phonenumberlocator.ui.MainActivity
import com.example.phonenumberlocator.ui.activities.helpScreens.PNLIntroSliderActivity
import com.example.phonenumberlocator.pnlSharedPreferencesLang.PNLMySharePreferences
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

        adsConsentManager.requestUMP(
            true,
            "51B1AB5BA7D2641277DFA65518CC6E7A",
            false
        ) { canRequestAds ->
            Log.d("AdsConsentManager", "adsConsentManager response :$canRequestAds ")
            if (canRequestAds) {
                handleConsentFormAndAdsRequests()
                splashEnd()
            }
        }

//        adsConsentManager.requestUMP { canRequest ->
//            Log.e("AdsConsentManager", "adsConsentManager response :$canRequest ")
//            runOnUiThread(this::handleConsentFormAndAdsRequests)
//        }


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
                        this@PNLSplashActivity,
                        PNLIntroSliderActivity::class.java
                    ).putExtra("isComingFromSplash", true)
                )
                finish()
            } else {
                isShowAD = true
                startActivity(
                    Intent(
                        this@PNLSplashActivity,
                        MainActivity::class.java
                    )
                )
                finish()
            }
        }, 8000)

    }


    private fun handleConsentFormAndAdsRequests() {
        MobileAds.initialize(this) { }
        PhoneNumberLocator.instance.setAppOpenAd()

        // consent done .............load ads here
        loadPriorityAdmobInterstitial(
            getString(R.string.admob_splash_interistitial_high),
            getString(R.string.admob_splash_interistitial_low)
        ) {
            Log.d("NL", "loadAdmobInterstitialPriority successful")
            interstitialAdPriority = it
        }
        handleAds()
    }

    private fun handleAds() {
        if (!baseConfig.appStarted) {
            if (isNetworkAvailable()) {
                loadHighOrLowNativeAd(
                    this,
                    getString(R.string.admob_native_lang_high),
                    getString(R.string.admob_native_lang_low)
                ) { nativeAd, adType ->
                    Log.d("danishTest1", "loadHighOrLowNativeAd: ad:$nativeAd typr:$adType")
                    if (nativeAd != null) {
                        nativeAdLang.postValue(nativeAd)
                    } else {
                        nativeAdLang.postValue(null)
                    }
                }
            } else {
                nativeAdLang.postValue(null)
            }

        }

        if (!baseConfig.isAppIntroComplete) {
            loadHighOrLowNativeAd(
                this,
                getString(R.string.admob_native_boarding_high),
                getString(R.string.admob_native_boarding_low)
            ) { nativeAd, adType ->
                Log.d("danishTest1", "loadHighOrLowNativeAd: ad:$nativeAd typr:$adType")
                if (nativeAd != null) {
                    onBoardNative1.postValue(nativeAd)
                } else {
                    onBoardNative1.postValue(null)
                }
            }

            loadHighOrLowNativeAd(
                this,
                getString(R.string.admob_native_boarding_high),
                getString(R.string.admob_native_boarding_low)
            ) { nativeAd, adType ->
                Log.d("danishTest1", "loadHighOrLowNativeAd: ad:$nativeAd typr:$adType")
                if (nativeAd != null) {
                    onBoardNative2.postValue(nativeAd)
                } else {
                    onBoardNative2.postValue(null)
                }
            }

            loadHighOrLowNativeAd(
                this,
                getString(R.string.admob_native_boarding_high),
                getString(R.string.admob_native_boarding_low)
            ) { nativeAd, adType ->
                Log.d("danishTest1", "loadHighOrLowNativeAd: ad:$nativeAd typr:$adType")
                if (nativeAd != null) {
                    onBoardNative3.postValue(nativeAd)
                } else {
                    onBoardNative3.postValue(null)
                }
            }

            loadHighOrLowNativeAd(
                this,
                getString(R.string.admob_native_boarding_high),
                getString(R.string.admob_native_boarding_low)
            ) { nativeAd, adType ->
                Log.d("danishTest1", "loadHighOrLowNativeAd: ad:$nativeAd typr:$adType")
                if (nativeAd != null) {
                    onBoardNative4.postValue(nativeAd)
                } else {
                    onBoardNative4.postValue(null)
                }
            }

        }

    }

}