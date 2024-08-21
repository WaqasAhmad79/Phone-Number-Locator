package com.example.phonenumberlocator.admob_ads.banner_ad

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.phonenumberlocator.admob_ads.OpenApp
import com.example.phonenumberlocator.admob_ads.isInterstitialRecentClosed
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*

class BannerAdHelper(
    val activity: Activity,
    lifecycleOwner: LifecycleOwner,
    val config: BannerAdConfig
) {

    var counterBanner = 0
    var TAG = "BannerAdHelper12"
    var myView: FrameLayout? = null
    var shimmer: ShimmerFrameLayout? = null
    private val adSize: Int = 50
    private var bannerAdState = AdState.FAILED

    enum class AdState {
        LOADING,
        LOADED,
        FAILED
    }


    private val lifecycleObserver: DefaultLifecycleObserver = object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            //Log.d(TAG, "onCreate: ")
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            //Log.d(TAG, "onDestroy: ")
        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
            //Log.d(TAG, "onPause: ")
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            // Log.d(TAG, "onStart: ")
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
            //Log.d(TAG, "onStop: ")
        }

        override fun onResume(owner: LifecycleOwner) {
            Log.d(TAG, "onResume: ")
            if (!isInterstitialRecentClosed) {
                if (!OpenApp.adOpenAppVisible) {
                    if (config.canReloadAds) {
                        if (config.isCollapsibleAd) {
                            showBannerAdmob()
                        } else {
                            showBannerAdmob()
                        }
                    }
                }
            }
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }


    fun showBannerAdmob(
    ) {
        val admobView = AdView(activity)
        admobView.adUnitId = config.idAds
        getAdSize(activity).let { admobView.setAdSize(it) }
        val adRequest = AdRequest.Builder().build()
        if (bannerAdState != AdState.LOADING) {
            counterBanner++
            bannerAdState = AdState.LOADING
            admobView.loadAd(adRequest)
            myView?.let {
                setAdListener(admobView, it) {

                }
            }
        }
    }

    private fun setAdListener(admobView: AdView, myView: FrameLayout, adCallBack: () -> Unit) {
        admobView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                bannerAdState = AdState.FAILED
                Log.d(TAG, "onAdFailedToLoad: Banner fail to load ")
            }

            override fun onAdLoaded() {
                adCallBack.invoke()
                shimmer?.beGone()
                shimmer?.stopShimmer()
                myView.removeAllViews()
                myView.addView(admobView)
                bannerAdState = AdState.LOADED
                Log.d(TAG, "onAdLoaded: Banner loaded counter $counterBanner")
            }
        }
    }

    private fun getAdSize(context: Context): AdSize {
        val displayMetricsOut = findRelevantOutMetrics(context)
        val widthPixels = displayMetricsOut.widthPixels.toFloat()
        val density = displayMetricsOut.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getInlineAdaptiveBannerAdSize(adWidth, adSize)
    }

    private fun findRelevantOutMetrics(context: Context): DisplayMetrics {
        return if (androidVersionIs11OrAbove())
            getOutMetricsForApiLevel30orAbove(context)
        else
            getOutMetricsForOtherVersions(context)
    }

    private fun androidVersionIs11OrAbove(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getOutMetricsForApiLevel30orAbove(context: Context): DisplayMetrics {
        val display: Display? = context.display
        val outMetrics = DisplayMetrics()
        display?.getRealMetrics(outMetrics)
        return outMetrics
    }

    @Suppress("DEPRECATION")
    private fun getOutMetricsForOtherVersions(context: Context): DisplayMetrics {
        return context.resources.displayMetrics
    }


    private fun getCollapsibleAdSize(activity: Activity): AdSize {
        val displayMetricsOut = findRelevantOutMetrics(activity)
        val widthPixels = displayMetricsOut.widthPixels.toFloat()
        val density = displayMetricsOut.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
    }

    fun loadAndShowCollapsibleBannerAd() {
        myView?.let { flAd ->
            val adView = AdView(activity)
            adView.adUnitId = config.idAds
            val adSize = getCollapsibleAdSize(activity)
            adView.setAdSize(adSize)
            val extras = Bundle()
            extras.putString("collapsible", "bottom")
            val adRequest = AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()
            flAd.addView(adView)
            if (bannerAdState != AdState.LOADING) {
                adView.loadAd(adRequest)
            }
            myView?.let {
                setAdListener(adView, it) {
                }
            }
        }
    }


}