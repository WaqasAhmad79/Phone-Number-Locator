package com.example.phonenumberlocator.admob_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatButton
import com.example.phonenumberlocator.R
import com.facebook.shimmer.BuildConfig
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

const val nativeAdFlow = "nativeAdFlow"

enum class NativeType {
    HIGH, LOW, FAILED
}

@SuppressLint("InflateParams")
fun loadHighOrLowNativeAd(
    context: Context,
    nativeIdHigh: String,
    nativeIdLow: String,
    adResult: ((NativeAd?, NativeType) -> Unit)
) {
    val builder = AdLoader.Builder(context, nativeIdHigh)
    builder.forNativeAd { nativeAd ->
        showNativeLog("NativeHigh Ad loaded...")
        adResult.invoke(nativeAd, NativeType.HIGH)
    }
    val videoOptions = VideoOptions.Builder()
        .setStartMuted(true)
        .build()

    val adOptions = NativeAdOptions.Builder()
        .setVideoOptions(videoOptions)
        .build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error =
                "${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
            showNativeLog("failed to load NativeHigh Ad error:$error")
            loadAndReturnAd(context, nativeIdLow) { lowNativeAd ->
                if (lowNativeAd == null) {
                    adResult.invoke(null, NativeType.FAILED)
                } else {
                    adResult.invoke(lowNativeAd, NativeType.LOW)
                }
            }

        }
    }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}


@SuppressLint("InflateParams")
fun loadAndReturnAd(
    context: Context, nativeId: String, adResult: ((NativeAd?) -> Unit)
) {
    val builder = AdLoader.Builder(context, nativeId)
    builder.forNativeAd { nativeAd ->
        showNativeLog("loadAndReturnAd: NativeLow Ad loaded...")
        adResult.invoke(nativeAd)
    }
    val videoOptions = VideoOptions.Builder()
        .setStartMuted(true)
        .build()

    val adOptions = NativeAdOptions.Builder()
        .setVideoOptions(videoOptions)
        .build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error =
                "${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
            showNativeLog("loadAndReturnAd: failed to load NativeLow Ad error:$error")
            adResult.invoke(null)
        }
    }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}


@SuppressLint("InflateParams")
fun loadAndReturnNoMediaAd(
    context: Context, nativeId: String, adResult: ((NativeAd?) -> Unit)
) {
    val builder = AdLoader.Builder(context, nativeId)
    builder.forNativeAd { nativeAd ->
        showNativeLog("loadAndReturnAd: NativeLow Ad loaded...")
        adResult.invoke(nativeAd)
    }

    val adOptions = NativeAdOptions.Builder()
        .build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error =
                "${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
            showNativeLog("loadAndReturnAd: failed to load NativeLow Ad error:$error")
            adResult.invoke(null)
        }
    }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}


fun showNativeLog(msg: String) {
    Log.d(nativeAdFlow, msg)
}

fun Activity.loadAndShowNativeAd(
    adFrame: FrameLayout, containerShimmerLoading: ShimmerFrameLayout, @LayoutRes layoutRes: Int,
    nativeId: String,
    scaleType: ImageView.ScaleType? = null
) {
    val builder = AdLoader.Builder(this, nativeId)
    builder.forNativeAd { nativeAd ->
        val activityDestroyed: Boolean = isDestroyed
        if (activityDestroyed || isFinishing || isChangingConfigurations) {
            nativeAd.destroy()
            return@forNativeAd
        }

        showNativeLog("loadAndShowNativeAd: native ad loaded successfully 1")
        showNativeLog("loadAndShowNativeAd: stop loading shimmer...")
        containerShimmerLoading.stopShimmer()
        containerShimmerLoading.visibility = View.GONE
        showNativeLog("loadAndShowNativeAd: populateUnifiedNativeAdView()...")
        val adView = layoutInflater.inflate(layoutRes, null, false) as NativeAdView
        populateUnifiedNativeAdView(nativeAd, adView, scaleType)
        adFrame.removeAllViews()
        adFrame.addView(adView)
    }

    val videoOptions = VideoOptions.Builder()
        .setStartMuted(true)
        .build()

    val adOptions = NativeAdOptions.Builder()
        .setVideoOptions(videoOptions)
        .build()

    builder.withNativeAdOptions(adOptions)

    val adLoader = builder.withAdListener(object : AdListener() {
        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            val error =
                "loadAndShowNativeAd onAdFailedToLoad: ${loadAdError.domain}, code: ${loadAdError.code}, message: ${loadAdError.message}"
            showNativeLog("loadAndShowNativeAd: failed to load native ad 2 $error")
            showNativeLog("loadAndShowNativeAd: stop loading shimmer...")
            containerShimmerLoading.stopShimmer()
            containerShimmerLoading.visibility = View.GONE
        }
    }).build()
    adLoader.loadAd(AdRequest.Builder().build())
}

fun showLoadedNativeAd(
    context: Context,
    nativeAdHolder: FrameLayout,
    containerShimmerLoading: ShimmerFrameLayout,
    adLayout: Int,
    nativeAd: NativeAd,
    scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
) {
    showNativeLog("showLoadedNativeAd: stop loading shimmer...")
    containerShimmerLoading.stopShimmer()
    containerShimmerLoading.visibility = View.GONE
    nativeAdHolder.removeAllViews()
    showNativeLog("showLoadedNativeAd: populateUnifiedNativeAdView()...")
    val adView = (context as Activity).layoutInflater.inflate(adLayout, null, false) as NativeAdView
    populateUnifiedNativeAdView(nativeAd, adView, scaleType)
    nativeAdHolder.addView(adView)
}

fun showLoadedNoMediaNativeAd(
    context: Context,
    nativeAdHolder: FrameLayout,
    containerShimmerLoading: ShimmerFrameLayout,
    adLayout: Int,
    nativeAd: NativeAd,
    scaleType: ImageView.ScaleType? = ImageView.ScaleType.FIT_CENTER
) {
    showNativeLog("showLoadedNativeAd: stop loading shimmer...")
    containerShimmerLoading.stopShimmer()
    containerShimmerLoading.visibility = View.GONE
    showNativeLog("showLoadedNativeAd: populateUnifiedNativeAdView()...")
    val adView = (context as Activity).layoutInflater.inflate(adLayout, null, false) as NativeAdView

    //populate native Ad start
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    //adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)

    nativeAd.headline?.let {
        (adView.headlineView as TextView).text = it
    } ?: changeTextToEmpty(adView.headlineView as TextView)

//    nativeAd.advertiser?.let {
//        (adView.advertiserView as TextView).text = it
//    } ?: changeTextToEmpty(adView.advertiserView as TextView)

    nativeAd.icon?.let {
        (adView.iconView as ImageView?)?.apply {
            setImageDrawable(it.drawable)
        }
    } ?: hideView(adView.iconView)

    nativeAd.callToAction?.let {
        (adView.callToActionView as AppCompatButton).text = it
    } ?: hideView(adView.callToActionView)

    nativeAd.body?.let {
        (adView.bodyView as TextView).text = it
    }

    adView.setNativeAd(nativeAd)

    //populate native Ad end
    nativeAdHolder.removeAllViews()
    nativeAdHolder.addView(adView)
}

fun populateUnifiedNativeAdView(
    nativeAd: NativeAd,
    adView: NativeAdView,
    scaleType: ImageView.ScaleType?
) {
    adView.mediaView = adView.findViewById(R.id.ad_media)
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    //adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)

    nativeAd.mediaContent?.let {
        adView.mediaView?.apply {
            setMediaContent(it)
            scaleType?.let { sType ->
                setImageScaleType(sType)
            }
        }
    }

    nativeAd.headline?.let {
        (adView.headlineView as TextView).text = it
    } ?: changeTextToEmpty(adView.headlineView as TextView)

//    nativeAd.advertiser?.let {
//        (adView.advertiserView as TextView).text = it
//    } ?: changeTextToEmpty(adView.advertiserView as TextView)

    nativeAd.icon?.let {
        (adView.iconView as ImageView?)?.apply {
            setImageDrawable(it.drawable)
        }
    } ?: hideView(adView.iconView)

    nativeAd.callToAction?.let {
        (adView.callToActionView as TextView).text = it
    } ?: hideView(adView.callToActionView)

    val starRatingView = adView.findViewById<RatingBar>(R.id.ratingBar)
    nativeAd.body?.let {
        (adView.bodyView as TextView).text = it
    }
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
    when {
        BuildConfig.DEBUG -> starRatingView.rating = 3.5F
        else -> hideView(starRatingView)
    }
}

fun changeTextToEmpty(textView: TextView) {
    textView.text = ""
}

fun hideView(view: View?) {
    view?.visibility = View.GONE
}



