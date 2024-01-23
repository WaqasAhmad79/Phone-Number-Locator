package com.example.phonenumberlocator.admob_ads


import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.example.phonenumberlocator.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import java.util.UUID


fun showBannerAdmob(
    myView: FrameLayout,
    context: Context,
    bannerId: String,
    adCallBack: (() -> Unit)? = null
) {
    val admobView = AdView(context)
    admobView.adUnitId = bannerId
    getAdSize(context)?.let { admobView.setAdSize(it) }
    val adRequest = AdRequest.Builder().build()
    admobView.loadAd(adRequest)
    setAdListener(admobView, myView) {
        adCallBack?.invoke()
    }
}

private fun setAdListener(admobView: AdView, myView: FrameLayout, adCallBack: () -> Unit) {
    admobView.adListener = object : AdListener() {
        override fun onAdLoaded() {
            adCallBack.invoke()
            myView.removeAllViews()
            myView.addView(admobView)
        }
    }
}

private fun getAdSize(context: Context): AdSize? {
    val displayMetricsOut = findRelevantOutMetrics(context)
    val widthPixels = displayMetricsOut.widthPixels.toFloat()
    val density = displayMetricsOut.density
    val adWidth = (widthPixels / density).toInt()
    return AdSize.getInlineAdaptiveBannerAdSize( adWidth,50)
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

enum class BannerState { LOADED, FAILED }

public fun loadCollapsibleBanner(context: Activity,id:String,frameLayout: FrameLayout) {
   val adView = AdView(context)
    adView.adUnitId = context.getString(R.string.adaptive_mob_banner_id)
    val adSize: AdSize = getAdSize2(context,frameLayout)
    adView.setAdSize(adSize)
    val extras = Bundle()
    extras.putString("collapsible", "bottom")
    extras.putString("collapsible_request_id", UUID.randomUUID().toString())
    val adRequest: AdRequest = AdRequest.Builder()
        .addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()
    frameLayout.addView(adView)
    adView.loadAd(adRequest)
}

/*fun loadCollapsibleBanner(
    context: Activity,
    id: String,
    frameLayout: FrameLayout,
    callback: (BannerState) -> Unit
) {
    val shimmerLayout = ShimmerFrameLayout(context)
    val adView = AdView(context)
    adView.adUnitId = context.getString(R.string.adaptive_mob_banner_id)
    val adSize: AdSize = getAdSize2(context, frameLayout)
    adView.setAdSize(adSize)
    val extras = Bundle()
    extras.putString("collapsible", "bottom")
    extras.putString("collapsible_request_id", UUID.randomUUID().toString())
    val adRequest: AdRequest = AdRequest.Builder()
        .addNetworkExtrasBundle(AdMobAdapter::class.java, extras).build()

    // Wrap AdView with ShimmerFrameLayout
    shimmerLayout.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    shimmerLayout.addView(adView)
    shimmerLayout.startShimmer() // Start shimmer effect

    // Add ShimmerFrameLayout to FrameLayout
    frameLayout.addView(shimmerLayout)

    // Load ad and stop shimmer effect when ad is loaded
    adView.loadAd(adRequest)
    adView.adListener = object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            callback.invoke(BannerState.LOADED)
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            callback.invoke(BannerState.FAILED)
        }
    }}*/


private fun getAdSize2(context: Activity,frameLayout: FrameLayout): AdSize {
    val display =context.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    val density = outMetrics.density
    var adWidthPixels: Int =frameLayout.width
    if (adWidthPixels.toFloat() == 0f) {
        adWidthPixels = outMetrics.widthPixels.toFloat().toInt()
    }
    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
}

