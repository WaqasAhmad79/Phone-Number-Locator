package com.example.phonenumberlocator.admob_ads


import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.ui.activities.PNLSplashActivity
import com.example.phonenumberlocator.ui.pnlDialog.PNLResumeLoadingDialog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import org.jetbrains.annotations.NotNull
import kotlin.math.log


class OpenApp(private val globalClass: PhoneNumberLocator) : Application.ActivityLifecycleCallbacks,
    LifecycleEventObserver {

    private var adVisible = false
    private val TAG = "TESTINGOpenApp"
    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var isShowingDialog = false
    private var dialog: PNLResumeLoadingDialog? = null
    private var myApplication: PhoneNumberLocator? = globalClass
    private var fullScreenContentCallback: FullScreenContentCallback? = null

    init {
        this.myApplication?.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        fetchAd()
        Log.d(TAG, "init called: ")
//        currentActivity?.let {
//            Log.d(TAG, "this: ")
//            dialog = ResumeLoadingDialog(currentActivity)
//        }

    }

    fun fetchAd() {
        Log.d(TAG, "fetchAd: called ${isAdAvailable()}")
        if (isAdAvailable()) {
            return
        }
        Log.d(TAG, "fetchAd: ")
            val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        Log.d(TAG, "onAdLoaded: ")
                    }

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        super.onAdFailedToLoad(p0)
                        Log.d(TAG, "onAdFailedToLoad: $p0")
                    }
                }
            val request: AdRequest = getAdRequest()

            myApplication?.applicationContext?.apply {
                AppOpenAd.load(
                    this,
                    globalClass.getString(R.string.admob_app_open_id),
                    request,
                    AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                    loadCallback
                )
            }

    }

    private fun showAdIfAvailable() {
        Log.d(TAG, "showAdIfAvailable: $canShowAppOpen")
        Log.d(TAG, "showAdIfAvailable: $canShowAppOpen")
        if (!isInterstitialAdOnScreen && !canShowAppOpen) {
            if (!isShowingAd && isAdAvailable()) {
                fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        adVisible = false
                        dismissDialog()
                        fetchAd()
                        Log.d(TAG, "onAdDismissedFullScreenContent: ")
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        dismissDialog()
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                        Log.d(TAG, "onAdShowedFullScreenContent: ")
                    }
                }

                adVisible = true
                appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
                currentActivity?.let {
                    Log.d(TAG, "this: ")
                    dialog = PNLResumeLoadingDialog(currentActivity)
                }
                dismissDialog()
                showDialog()
                appOpenAd!!.show(currentActivity!!)
            }
        }
    }


    override fun onStateChanged(p0: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            Log.d(TAG, "onStateChanged: ")
            currentActivity?.let {
                Log.d(TAG, "onStateChanged: $it")
                Log.d(TAG, "onStateChanged: ${it !is PNLSplashActivity}")
                if (it !is PNLSplashActivity) {
                    currentActivity?.let{it2->
                            showAdIfAvailable()
                    }
                } else {
                    fetchAd()
                }
            }
        }
    }

    private fun dismissDialog() {
        Log.d(TAG, "dismissDialog: ${dialog?.isShowing}")
        dialog?.dismiss()
    }

    private fun showDialog() {
        Log.d(TAG, "showDialog: ")
        dialog?.show()
    }

    @NotNull
    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }


    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
        /* currentActivity?.let {
             Log.d(TAG, "onActivityStarted: Dialog")
             dialog = ResumeLoadingDialog(currentActivity)
         }*/
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
        /* currentActivity?.let {
             Log.d(TAG, "onActivityResumed: Dialog")
             dialog = ResumeLoadingDialog(currentActivity)
         }*/
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}

/*
package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.ui.activities.PNLSplashActivity
import com.example.phonenumberlocator.ui.pnlDialog.PNLResumeLoadingDialog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import org.jetbrains.annotations.NotNull

var showAppOpenAds = false

class OpenApp(private val globalClass: PhoneNumberLocator) : Application.ActivityLifecycleCallbacks,
    LifecycleEventObserver {

    private var adVisible = false
    private val TAG = "TESTINGOpenApp"
    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private var isHighAdFailedToLoad = false
    private var isMediumAdFailedToLoad = false
    private var dialog: PNLResumeLoadingDialog? = null
    private var myApplication: PhoneNumberLocator? = globalClass
    private var fullScreenContentCallback: FullScreenContentCallback? = null


    init {
        this.myApplication?.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun fetchAd(adId: String) {
        if (isAdAvailable()) {
            return
        }
        val loadCallback: AppOpenAd.AppOpenAdLoadCallback =
            object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    Log.d(TAG, "onAdLoaded: ")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Log.d(TAG, "onAdFailedToLoad: $p0")
                    if (!isHighAdFailedToLoad) {
                        Log.d(TAG, "onAdFailedToLoad: high app open fail$p0")
                        isHighAdFailedToLoad = true
                        fetchAd(globalClass.getString(R.string.admob_app_open_id_sp))
                    } else if (!isMediumAdFailedToLoad) {
                        Log.d(TAG, "onAdFailedToLoad: medium app open fail $p0")
                        isMediumAdFailedToLoad = true
                        fetchAd(globalClass.getString(R.string.admob_app_open_id))
                    } else {
                        Log.d(TAG, "onAdFailedToLoad: low app open fail$p0")
                    }
                }
            }
        val request: AdRequest = getAdRequest()

        myApplication?.applicationContext?.apply {
            AppOpenAd.load(
                this,
                globalClass.getString(R.string.admob_app_open_id_high),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback
            )
        }
    }

    private fun showAdIfAvailable() {
        if (!isInterstitialAdOnScreen && !showAppOpenAds) {
            if (!isShowingAd && isAdAvailable()) {
                fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        adVisible = false
                        dismissDialog()
                        fetchAd(globalClass.getString(R.string.admob_app_open_id_high))
                        Log.d(TAG, "onAdDismissedFullScreenContent: ")
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        dismissDialog()
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: ")
                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                        Log.d(TAG, "onAdShowedFullScreenContent: ")
                    }
                }

                adVisible = true
                appOpenAd?.fullScreenContentCallback = fullScreenContentCallback
                currentActivity?.let {
                    Log.d(TAG, "this: ")
                    dialog = PNLResumeLoadingDialog(currentActivity)
                }
                dismissDialog()

                showDialog()
                appOpenAd!!.show(currentActivity!!)


            } else {
                fetchAd(globalClass.getString(R.string.admob_app_open_id_high))
            }
        }
    }


    override fun onStateChanged(p0: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            Log.d(TAG, "onStateChanged: ")

            currentActivity?.let {
                if (it !is PNLSplashActivity) {
                    showAdIfAvailable()
                }
            }
        }
    }

    private fun dismissDialog() {
        Log.d(TAG, "dismissDialog: ${dialog?.isShowing}")
        dialog?.dismiss()
    }

    private fun showDialog() {
        Log.d(TAG, "showDialog: ")
        dialog?.show()
    }

    @NotNull
    private fun getAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }


    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }


    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}*/
