package com.example.phonenumberlocator.ui.activities

import android.annotation.SuppressLint
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
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.AdsConsentManager
import com.example.phonenumberlocator.admob_ads.OpenApp
import com.example.phonenumberlocator.admob_ads.OpenApp.isShowingAd
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.loadNormalAdmobInterstitial
import com.example.phonenumberlocator.admob_ads.showBannerAdmob
import com.example.phonenumberlocator.databinding.ActivityPnlsplashBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlSharedPreferencesLang.PNLMySharePreferences
import com.example.phonenumberlocator.pnlUtil.setCurrentLocale
import com.example.phonenumberlocator.ui.MainActivity

class PNLSplashActivity : AppCompatActivity() {
    private val TAG = "PNLSplashActivity"
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
        startSplash()
        val mySharePreferences = PNLMySharePreferences(this)
        started = mySharePreferences.appStarted


        binding.button.setOnClickListener {
            startActivity(Intent(this@PNLSplashActivity, PNLLanguageActivity::class.java))
            finish()
        }

        hideNavBar()

    }

    @SuppressLint("SuspiciousIndentation")
    fun startSplash() {

        RemoteConfigClass.fetchRecord(this) {
            Log.d(TAG, "startSplash: Remote config success")
            val adsConsentManager = AdsConsentManager(this)


            // Ads consent for testing

            /*  adsConsentManager.requestUMP(
                  true,
                  "B6DAE15C2DF0205112DDD118589C1EA7", // your testing device id
                  true
              ) { canRequestAds ->
                  PhoneNumberLocator.canRequestAd = canRequestAds
                  runOnUiThread(this::splashEnd)
                  Log.e("AdsConsentManager", "adsConsentManager response :$canRequestAds ")
                  if (canRequestAds) {
                      handleConsentFormAndAdsRequests()
                  } else {
                      splashEnd()
                  }
              }*/


            // Ads consent for release build
            adsConsentManager.requestUMP { canRequest ->
                Log.e("$TAG AdsConsentManager", "adsConsentManager response :$canRequest ")
                PhoneNumberLocator.canRequestAd = canRequest
                runOnUiThread(this::splashEnd)
                if (canRequest) {
                    handleConsentFormAndAdsRequests()
                } else {
                    splashEnd()
                }
            }

            /////

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
                        this@PNLSplashActivity, PNLLanguageActivity::class.java
                    ).putExtra("isComingFromSplash", true)
                )
                finish()
            } else {
                isShowingAd = true
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

            if (RemoteConfigClass.App_Open_Ad && PhoneNumberLocator.canRequestAd) {
                OpenApp.initialize(it)
                OpenApp.fetchAd()
            }

        }

        // consent done .............load ads here
        Log.d(TAG, "handleConsentFormAndAdsRequests: ${RemoteConfigClass.interSplash}")

        if (RemoteConfigClass.interSplash && PhoneNumberLocator.canRequestAd) {
            loadNormalAdmobInterstitial(getString(R.string.admob_splash_interistitial_low))
        }

        handleAds()
    }

    private fun handleAds() {

        if (RemoteConfigClass.interSplash || RemoteConfigClass.interOtherHome) {
            if (PhoneNumberLocator.canRequestAd) {
                loadNormalAdmobInterstitial(getString(R.string.admob_splash_interistitial_low))
            }
        }

        if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {

            if (RemoteConfigClass.banner_splash) { // Banner Splash
                binding.ads.beVisible()
                showBannerAdmob(binding.ads, this, getString(R.string.ad_mob_banner_id))
            } else {
                binding.ads.beGone()
            }

            if (RemoteConfigClass.native_language) { // Native Language Ad

                loadAndReturnAd(
                    this@PNLSplashActivity, resources.getString(R.string.admob_native_lang_low)
                ) { it2 ->
                    Log.d("Native_language_ad ", "value: $it2")
                    if (it2 != null) {
                        nativeAdLang.value = it2
                    }
                }
            }
        } else {
            binding.ads.beGone()
            nativeAdLang.postValue(null)
        }

    }
}