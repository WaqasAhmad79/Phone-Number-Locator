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
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canLoadAndShowAd
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.GDPR
import com.example.phonenumberlocator.admob_ads.interstitialAdPriority
import com.example.phonenumberlocator.admob_ads.isShowAD
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
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

class PNLSplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnlsplashBinding
    private var gdpr: GDPR? = null
    var started = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnlsplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleConsentFormAndAdsRequests()

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

        val mySharePreferences = PNLMySharePreferences(this)
        started = mySharePreferences.appStarted

        Handler(Looper.getMainLooper()).postDelayed({
            binding.button.beVisible()
            binding.progressBar.beGone()
        }, 5000)
        binding.button.setOnClickListener {
            splashEnd()
        }

    }

    private fun splashEnd() {
            if (!baseConfig.appStarted) {
                // The user has seen the Onboard
                startActivity(Intent(this@PNLSplashActivity, PNLLanguageActivity::class.java))
                finish()
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
    }

    private fun handleConsentFormAndAdsRequests() {
        gdpr = GDPR(this)
        gdpr?.setGDPRConsent() {
            if (it) {
                canLoadAndShowAd = true
                // consent done .............load ads here
                loadPriorityAdmobInterstitial(
                    getString(R.string.admob_splash_interistitial_high),
                    getString(R.string.admob_splash_interistitial_low)
                ) {
                    interstitialAdPriority = it
                }
                handleAds()
            } else {
                canLoadAndShowAd = false
                // consent Un_success .............do not load ads
            }
        }
    }

    private fun handleAds() {
        if (isNetworkAvailable() && canLoadAndShowAd) {
            if (!baseConfig.appStarted) {
                loadAndReturnAd(
                    this@PNLSplashActivity,
                    resources.getString(R.string.admob_native_lang_high)
                ) {
                    if (it != null) {
                        PhoneNumberLocator.instance.nativeAdLang.value = it
                    } else {
                        loadAndReturnAd(
                            this@PNLSplashActivity,
                            resources.getString(R.string.admob_native_lang_low)
                        ) { it2 ->
                            if (it2 != null) {
                                PhoneNumberLocator.instance.nativeAdLang.value = it2
                            }
                        }
                    }
                }
            } else if (!baseConfig.isAppIntroComplete) {
                loadAndReturnAd(
                    this@PNLSplashActivity,
                    resources.getString(R.string.admob_native_boarding_high)
                ) {
                    if (it != null) {
                        PhoneNumberLocator.instance.nativeAdBoarding.value = it
                    } else {
                        loadAndReturnAd(
                            this@PNLSplashActivity,
                            resources.getString(R.string.admob_native_boarding_low)
                        ) { it2 ->
                            if (it2 != null) {
                                PhoneNumberLocator.instance.nativeAdBoarding.value = it2
                            }
                        }
                    }
                }
            }

            loadPriorityAdmobInterstitial(
                getString(R.string.admob_splash_interistitial_high),
                getString(R.string.admob_splash_interistitial_low)
            ) {
                Log.d("TAG", "ad:$it ")
                interstitialAdPriority = it
            }
        }
    }


}