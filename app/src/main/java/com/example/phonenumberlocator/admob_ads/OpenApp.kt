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




object OpenApp : Application.ActivityLifecycleCallbacks, LifecycleEventObserver {

    private var adVisible = false
    private val TAG = "TESTINGOpenApp"
    private var appOpenAd: AppOpenAd? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false

    private var isShowingDialog = false
    private var dialog: PNLResumeLoadingDialog? = null
    private var myApplication: PhoneNumberLocator? = null
    private var fullScreenContentCallback: FullScreenContentCallback? = null

    fun initialize(
        application: PhoneNumberLocator
    ) {
        if (myApplication == null) {
            myApplication = application
            myApplication?.registerActivityLifecycleCallbacks(this)
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }
    }

    fun fetchAd() {
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
                }
            }
        val request: AdRequest = getAdRequest()

        myApplication?.applicationContext?.apply {
            AppOpenAd.load(
                this,
                myApplication!!.getString(R.string.admob_app_open_id),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                loadCallback
            )
        }
    }

    private fun showAdIfAvailable() {
        if (!canShowAppOpen && !isInterstitialAdOnScreen && !isShowingAd && isAdAvailable()) {
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
                dialog = PNLResumeLoadingDialog(currentActivity)
            }
            dismissDialog()
            showDialog()
            Log.d(TAG, "showAdIfAvailable: show")
            appOpenAd?.show(currentActivity!!)
        } else {
            Log.d(TAG, "showAdIfAvailable:fetchAd ")
            fetchAd()
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
        if (dialog != null && dialog?.isShowing!!) {
            dialog?.dismiss()
        }
    }

    private fun showDialog() {
        Log.d(TAG, "showDialog: ")
        dialog?.show()
    }

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
}
