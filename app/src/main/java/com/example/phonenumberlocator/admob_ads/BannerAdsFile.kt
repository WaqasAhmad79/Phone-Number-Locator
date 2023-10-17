package com.example.phonenumberlocator.admob_ads


import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


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
