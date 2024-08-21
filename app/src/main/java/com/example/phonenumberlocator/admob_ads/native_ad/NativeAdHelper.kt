package com.example.phonenumberlocator.admob_ads.native_ad

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.phonenumberlocator.admob_ads.OpenApp.adOpenAppVisible
import com.example.phonenumberlocator.admob_ads.isInterstitialRecentClosed
import com.example.phonenumberlocator.R
import com.facebook.shimmer.BuildConfig
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NativeAdHelper(
    val activity: Activity,
    val lifecycleOwner: LifecycleOwner,
    val config: NativeAdConfig
) : LifecycleEventObserver {
    enum class AdState {
        LOADING,
        LOADED,
        FAILED
    }

    var adState = AdState.LOADED
    var isActivityPaused = false
    var isAppPaused = false

    var counterAdsLoading = 0
    var TAG = "NativeAdHelper12"
    var nativeContentView: FrameLayout? = null
    var shimmerLayoutView: ShimmerFrameLayout? = null
    val adView = activity.layoutInflater.inflate(config.layoutId, null, false) as NativeAdView

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
            isActivityPaused = true
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
                if (isActivityPaused && !adOpenAppVisible) {
                    if (config.canReloadAds) {
                        loadAndShowNativeAd()
                    }
                }
                isActivityPaused = false
                isAppPaused = false
            }
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
    }

    fun requestAd() {
        loadAndShowNativeAd()
    }

    fun showNativeLog(msg: String) {
        Log.d(TAG, "showNativeLog: $msg")
    }


    fun loadAndReturnAd(
        activity: Activity, nativeId: String, adResult: ((NativeAd?) -> Unit)
    ) {
        if (adState == AdState.LOADING) {
            Log.d(TAG, "loadAndReturnAd:  LOADING...")
            return
        }
        Log.d(TAG, "loadAndReturnAd: new ad loading")
        adState = AdState.LOADING
        val builder = AdLoader.Builder(activity, nativeId)
        builder.forNativeAd { nativeAd ->
            adResult.invoke(nativeAd)
        }

        val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                adState = AdState.LOADED
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                adState = AdState.FAILED
                val error =
                    "${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
                showNativeLog("failed to load native ad 1 $error")
                adResult.invoke(null)
            }
        }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun showLoadedNativeAd(
        nativeAd: NativeAd,
        scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
    ) {
        Log.d(TAG, "showLoadedNativeAd: counter $counterAdsLoading")
        populateUnifiedNativeAdView(
            nativeAd,
            scaleType
        )
        nativeContentView?.removeAllViews()
        nativeContentView?.addView(adView)
    }


    fun loadAndShowNativeAd(
    ) {
        counterAdsLoading++
        config.idAds?.let {
            CoroutineScope(Dispatchers.IO).launch {
                loadAndReturnAd(activity, it) { nativeAd ->
                    nativeAd?.let {
                        CoroutineScope(Dispatchers.Main).launch {
                            val activityDestroyed: Boolean = activity.isDestroyed
                            if (activityDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                                nativeAd.destroy()
                                return@launch
                            }

                            populateUnifiedNativeAdView(nativeAd, null)
                            nativeContentView?.removeAllViews()
                            nativeContentView?.addView(adView)
                        }
                    }
                }
            }
        }
    }


    private fun populateUnifiedNativeAdView(
        nativeAd: NativeAd, scaleType: ImageView.ScaleType?
    ) {
        Log.d(TAG, "populateUnifiedNativeAdView: counter $counterAdsLoading")
        shimmerLayoutView?.stopShimmer()
        shimmerLayoutView?.visibility = View.GONE
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        nativeAd.mediaContent?.let {
            adView.mediaView?.apply {
                mediaContent = it
                scaleType?.let { sType ->
                    setImageScaleType(sType)
                }
            }
        }

        nativeAd.headline?.let {
            (adView.headlineView as TextView).text = it
        } ?: changeTextToEmpty(adView.headlineView as TextView)

        nativeAd.body?.let {
            (adView.bodyView as TextView).text = it
        } ?: changeTextToEmpty(adView.bodyView as TextView)


        nativeAd.icon?.let {
            (adView.iconView as ImageView?)?.apply {
                setImageDrawable(it.drawable)
            }
        } ?: hideView(adView.iconView)

        nativeAd.callToAction?.let {
            (adView.callToActionView as TextView).text = it
        } ?: hideView(adView.callToActionView)

        val starRatingView = adView.findViewById<RatingBar>(R.id.ratingBar)
        starRatingView?.let {
            nativeAd.starRating?.let {
                when {
                    it > 0 -> starRatingView.rating = it.toFloat()
                    else -> hideView(starRatingView)
                }
            } ?: myAddRating(starRatingView)
        }


        adView.setNativeAd(nativeAd)


        val vc = nativeAd.mediaContent?.videoController

        vc?.apply {
            if (hasVideoContent()) {

                videoLifecycleCallbacks = object : VideoController.VideoLifecycleCallbacks() {
                    override fun onVideoEnd() {
                        showNativeLog("native ad video ended 1")
                        super.onVideoEnd()
                    }
                }
            } else {
                showNativeLog("native ad not contains video 1")
            }
        }


    }

    private fun myAddRating(starRatingView: RatingBar) {
        when {
            BuildConfig.DEBUG -> starRatingView.rating = 3.5F
            else -> hideView(starRatingView)
        }
    }

    private fun changeTextToEmpty(textView: TextView) {
        textView.text = ""
    }

    private fun hideView(view: View?) {
        view?.visibility = View.GONE
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            Log.d(TAG, "onStateChanged: ON_CREATE")

        }

        if (event == Lifecycle.Event.ON_START) {
            Log.d(TAG, "onStateChanged: ON_START")

        }

        if (event == Lifecycle.Event.ON_RESUME) {
            Log.d(TAG, "onStateChanged: ON_RESUME")

        }

        if (event == Lifecycle.Event.ON_PAUSE) {
            Log.d(TAG, "onStateChanged: ON_PAUSE")

        }

        if (event == Lifecycle.Event.ON_STOP) {
            Log.d(TAG, "onStateChanged: ON_STOP")

        }

        if (event == Lifecycle.Event.ON_DESTROY) {
            Log.d(TAG, "onStateChanged: ON_DESTROY")
        }

        if (event == Lifecycle.Event.ON_ANY) {
            Log.d(TAG, "onStateChanged: ON_ANY")
        }
    }


}