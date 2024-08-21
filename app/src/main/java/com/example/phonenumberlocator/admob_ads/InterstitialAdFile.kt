package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.content.Context

var isInterstitialRecentClosed: Boolean = false
var isInterstitialAdOnScreen = false
var interstitialAdCounter = 0
var isFirstTimeAd = true
var isFirstClick = true

fun timeAndCounterCheck(): Boolean {
    return if (isFirstClick) {
        isFirstClick = false
        false
    } else {
        if (isFirstTimeAd) {
            isFirstTimeAd = false
            true
        } else {
            return counterCheck()
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
        showNormalAdmobInterstitial(closeListener, failListener, showListener)
    }
}

fun Activity.showNormalAdmobInterstitial(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance()
        .showNormalInterstitialAdNew(this, closeListener, failListener, showListener)
}

fun Context.loadNormalAdmobInterstitial(adId: String) {
    InterstitialAdClass.getInstance().loadNormalInterstitialAd(this, adId)
}


fun Activity.showExitAdmobInterstitial(
    closeListener: (() -> Unit)? = null,
    failListener: (() -> Unit)? = null,
    showListener: (() -> Unit)? = null
) {
    InterstitialAdClass.getInstance()
        .showExitInterstitialAdNew(this, closeListener, failListener, showListener)
}