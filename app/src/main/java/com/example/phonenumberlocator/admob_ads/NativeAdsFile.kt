package com.example.phonenumberlocator.admob_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import com.example.phonenumberlocator.R
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

const val nativeAdFlow = "nativeAdFlow"


@SuppressLint("InflateParams")
fun loadAndReturnAd(
    context: Context, nativeId: String, adResult: ((NativeAd?) -> Unit)
) {
    val builder = AdLoader.Builder(context, nativeId)
    builder.forNativeAd { nativeAd ->
        adResult.invoke(nativeAd)
    }
    val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

    val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error =
                "${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
            showNativeLog("failed to load native ad 1 $error")
            adResult.invoke(null)
        }
    }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}

fun showNativeLog(msg: String) {
    Log.d("showNativeLog", "showNativeLog: $msg")
//    showLogMessage(nativeAdFlow, msg)
}

fun Activity.loadAndShowNativeAd(
    adFrame: FrameLayout,
    @LayoutRes layoutRes: Int,
    nativeId: String,
    scaleType: ImageView.ScaleType? = null
) {
    val builder = AdLoader.Builder(this, nativeId)
    builder.forNativeAd { nativeAd ->
        CoroutineScope(Dispatchers.Main).launch {
            val activityDestroyed: Boolean = isDestroyed
            if (activityDestroyed || isFinishing || isChangingConfigurations) {
                nativeAd.destroy()
                return@launch
            }
            showNativeLog("native ad loaded successfully 1")

            val adView = layoutInflater.inflate(layoutRes, null, false) as NativeAdView
            populateUnifiedNativeAdView(nativeAd, adView, scaleType)
            adFrame.removeAllViews()
            adFrame.addView(adView)
        }
    }

    val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

    val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error =
                "${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
            showNativeLog("failed to load native ad 2 $error")
        }
    }).build()
    CoroutineScope(Dispatchers.IO).launch {
        adLoader.loadAd(AdRequest.Builder().build())
    }
}

fun showLoadedNativeAd(
    context: Context,
    nativeAdHolder: FrameLayout,
    adLayout: Int,
    nativeAd: NativeAd,
    scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
) {
    val adView = (context as Activity).layoutInflater.inflate(adLayout, null, false) as NativeAdView
    populateUnifiedNativeAdView(nativeAd, adView, scaleType)
    nativeAdHolder.removeAllViews()
    nativeAdHolder.addView(adView)
}


fun populateUnifiedNativeAdView(
    nativeAd: NativeAd, adView: NativeAdView, scaleType: ImageView.ScaleType?
) {
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
    nativeAd.starRating?.let {
        when {
            it > 0 -> starRatingView.rating = it.toFloat()
            else -> hideView(starRatingView)
        }
    } ?: myAddRating(starRatingView)


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

fun myAddRating(starRatingView: RatingBar) {
//    when {
//        BuildConfig.DEBUG ->
//        starRatingView.rating = 3.5F
//        else ->
            hideView(starRatingView)
//    }
}

fun changeTextToEmpty(textView: TextView) {
    textView.text = ""
}

fun hideView(view: View?) {
    view?.visibility = View.GONE
}



