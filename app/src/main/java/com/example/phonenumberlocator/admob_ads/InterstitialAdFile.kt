package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.content.Context
import android.util.Log
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
fun Activity.showSearchInterstitialAdWithTimeAndCounter(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    if (timeAndCounterCheck()) {
        timeWhenPreviousIntrShowed = System.currentTimeMillis()
        showSearchInterstitial(closeListener, failListener, showListener)
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
    InterstitialAdClass.getInstance().loadSimpleInterstitialAd(this)
}

fun Context.loadSplashAdmobInterstitial() {
    InterstitialAdClass.getInstance().loadSlashInterstitialAd(this)
}
fun Context.loadSearchAdmobInterstitial() {
    InterstitialAdClass.getInstance().loadSearchInterstitialAd(this)
}


fun Activity.showSplashInterstitial(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance()
        .showSplashInterstitialAdNew(this, closeListener, failListener, showListener)
}

fun Activity.showSearchInterstitial(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance()
        .showSearchInterstitialAdNew(this, closeListener, failListener, showListener)
}

