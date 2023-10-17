package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.phonenumberlocator.R
import com.google.android.gms.ads.interstitial.InterstitialAd


var interstitialAdPriority: InterstitialAd? = null
var timeWhenPreviousIntrShowed: Long? = null
var isFirst20SecondAdControlled = false
var isInterstitialAdOnScreen = false
var interstitialAdCounter = 0
var canShowAppOpen = false
var isFirstTimeAd = true
var isFirstClick = true
var isShowAD: Boolean=false

fun timeAndCounterCheck(): Boolean {
    return if (isFirstClick) {
        isFirstClick = false
        false
    } else {
        if (isFirstTimeAd) {
            isFirstTimeAd = false
            timeWhenPreviousIntrShowed = System.currentTimeMillis()
            true
        } else {
            if (!isFirst20SecondAdControlled) {
                val currentTime = System.currentTimeMillis()
                val timeDifference = timeWhenPreviousIntrShowed?.let { currentTime.minus(it) }
                Log.d("TAG", "isTimeDifferenceGreaterThan20Seconds: $timeDifference")
                if (timeDifference != null) {
                    isFirst20SecondAdControlled = timeDifference > 20000
                    timeDifference > 20000
                } else {
                    return true
                }
            } else {
                return counterCheck()
            }
        }
    }
}

fun counterCheck(): Boolean {
    interstitialAdCounter++
    return interstitialAdCounter % 2 == 0
}


fun Activity.showPriorityInterstitialAdWithTimeAndCounter(
    loadAgain: Boolean = false,
    adIDHigh: String? = null,
    adIDLow: String? = null,
    loadAd: ((InterstitialAd) -> Unit)? = null,
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    if (timeAndCounterCheck()) {
        showPriorityAdmobInterstitial(
            loadAgain,
            adIDHigh,
            adIDLow,
            loadAd,
            closeListener,
            failListener,
            showListener,
        )
    }
}


fun Activity.showPriorityInterstitialAdWithCounter(
    interstitialAd: InterstitialAd,
    loadAgain: Boolean = false,
    adIDHigh: String? = null,
    adIDLow: String? = null,
    loadAd: ((InterstitialAd) -> Unit)? = null,
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    if (counterCheck()) {
        showPriorityAdmobInterstitial(
            loadAgain,
            adIDHigh,
            adIDLow,
            loadAd,
            closeListener,
            failListener,
            showListener,
        )
    }
}


fun Activity.showSimpleInterstitialAdWithTimeAndCounter(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    if (timeAndCounterCheck()) {
        timeWhenPreviousIntrShowed = System.currentTimeMillis()
        showSimpleInterstitial(closeListener, failListener, showListener)
    }
}


fun Activity.showSimpleInterstitialAdWithCounter(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    if (counterCheck()) {
        showSimpleInterstitial(closeListener, failListener, showListener)
    }
}


fun Activity.showSimpleInterstitial(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance()
        .showSimpleInterstitialAdNew(this, closeListener, failListener, showListener)
}
fun Context.loadSimpleAdmobInterstitial() {
    InterstitialAdClass.getInstance().loadSimpleInterstitialAd(this,this.getString(R.string.admob_interistitial_others_one))
}

/**cam interstitial**/
/*
fun Activity.showCamInterstitial(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance()
        .showCamInterstitialAdNew(this, closeListener, failListener, showListener)
}
fun Context.loadCamAdmobInterstitial() {
    InterstitialAdClass.getInstance().loadCamInterstitialAd(this,this.getString(R.string.admob_interistitial_others_one))
}*/



fun Activity.showPriorityAdmobInterstitial(
    loadAgain: Boolean = false,
    adIDHigh: String? = null,
    adIDLow: String? = null,
    loadAd: ((InterstitialAd) -> Unit)? = null,
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance().showPriorityInterstitialAdNew(
        this,
        loadAgain,
        adIDHigh,
        adIDLow,
        loadAd,
        closeListener,
        failListener,
        showListener
    )
}

fun Context.loadPriorityAdmobInterstitial(
    adIDHigh: String, adIDLow: String, adLoaded: (InterstitialAd) -> Unit
) {
    InterstitialAdClass.getInstance().loadPriorityInterstitialAds(this, adIDHigh, adIDLow, adLoaded)
}

