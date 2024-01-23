package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


open class InterstitialAdClass {

    var mInterstitialAd: InterstitialAd? = null
    var mInterstitialAdSp: InterstitialAd? = null
    var mInterstitialAdSearch: InterstitialAd? = null

    private val logTag = "interstitialAdFlow"

    private var someOpPerformed = false

    private val runnable = Runnable {
        showInterstitialAdLog("checking safe operation ... ")
        if (!someOpPerformed) {
            showInterstitialAdLog("safe operation needed , performing")
            mInterstitialAd = null
            failListener?.invoke()
        } else showInterstitialAdLog("safe operation not needed")
    }
    private lateinit var handler: Handler
    private var failListener: (() -> Unit)? = null

    companion object {
        var failCounter = 0
        var failSearchCounter = 0
        var failSplashCounter = 0

        @Volatile
        private var instance: InterstitialAdClass? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InterstitialAdClass().also { instance = it }
        }
    }

    /***************************************************************************
     * Normal Ad Flow
     * ***************************************************************************/
    fun loadSimpleInterstitialAd(context: Context) {
        handler = Handler(Looper.myLooper()!!)
        showInterstitialAdLog("Loading Ad ...")
        mInterstitialAd?.let {
            return
        } ?: run {
            showInterstitialAdLog("Loading Ad 1...")
            context.let {
                InterstitialAd.load(it,
                    it.getString(R.string.admob_interistitial_others_one),
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {

                        override fun onAdFailedToLoad(ad: LoadAdError) {
                            mInterstitialAd = null
                            if (failCounter < 2) {
                                failCounter++
                                loadSimpleInterstitialAd(context)
                            } else failCounter = 0
                            showInterstitialAdLog("Ad failed to load because $ad")
                        }

                        override fun onAdLoaded(ad: InterstitialAd) {
                            mInterstitialAd = ad
                            showInterstitialAdLog("Ad successfully loaded")
                        }
                    })
            }

        }

    }
    fun showSimpleInterstitialAdNew(
        activity: Activity,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("Showing Ad ...")
        if (activity.isNetworkAvailable()) {
            mInterstitialAd?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("Ad closed by user")
                        loadSimpleInterstitialAd(activity)
                        mInterstitialAd = null
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog("Ad failed to show")
                        loadSimpleInterstitialAd(activity)
                        someOpPerformed = true
                        mInterstitialAd = null
                        failListener?.invoke()
                        dismissLoadingDialog()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        showInterstitialAdLog("Ad successfully showed")
                        isInterstitialAdOnScreen = true
                        someOpPerformed = true
                        showListener?.invoke()
                        Handler().postDelayed({
                            dismissLoadingDialog()
                        }, 1000)
                    }
                }

                someOpPerformed = false
                //show interstitialAd
                Handler().postDelayed({
                    if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        it.show(activity)
                    } else {
                        dismissLoadingDialog()
                    }

                }, 1000)


            } ?: run {
                showInterstitialAdLog("Ad is null , not showing ...")
                failListener?.invoke()
            }
        }

    }

    private fun showInterstitialAdLog(message: String) {
        Log.d(logTag, message)
    }
 /***************************************************************************
     * splash Ad Flow
     * ***************************************************************************/
    fun loadSlashInterstitialAd(context: Context) {
        handler = Handler(Looper.myLooper()!!)
        showInterstitialAdLog("Loading Ad ...")
        mInterstitialAdSp?.let {
            return
        } ?: run {
            showInterstitialAdLog("Loading Ad 1...")
            context.let {
                InterstitialAd.load(it,
                    it.getString(R.string.admob_splash_interistitial_low),
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {

                        override fun onAdFailedToLoad(ad: LoadAdError) {
                            mInterstitialAdSp = null
                            if (failSplashCounter < 1) {
                                failSplashCounter++
                                loadSimpleInterstitialAd(context)
                            } else failSplashCounter = 0
                            showInterstitialAdLog("Ad failed to load because $ad")
                        }

                        override fun onAdLoaded(ad: InterstitialAd) {
                            mInterstitialAdSp = ad
                            showInterstitialAdLog("Ad successfully loaded")
                        }
                    })
            }

        }

    }

// to show Interstitial Ad Activity reference must be given

    fun showSplashInterstitialAdNew(
        activity: Activity,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("Showing Ad ...")
        if (activity.isNetworkAvailable()) {
            mInterstitialAdSp?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("Ad closed by user")
                        mInterstitialAdSp = null
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog("Ad failed to show")
                        someOpPerformed = true
                        mInterstitialAdSp = null
                        failListener?.invoke()
                        dismissLoadingDialog()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        showInterstitialAdLog("Ad successfully showed")
                        isInterstitialAdOnScreen = true
                        someOpPerformed = true
                        showListener?.invoke()
                        Handler().postDelayed({
                            dismissLoadingDialog()
                        }, 1000)
                    }
                }

                someOpPerformed = false
                //show interstitialAd
                Handler().postDelayed({
                    if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        it.show(activity)
                    } else {
                        dismissLoadingDialog()
                    }

                }, 1000)


            } ?: run {
                showInterstitialAdLog("Ad is null , not showing ...")
                failListener?.invoke()
            }
        }

    }

    /***************************************************************************
     * search Ad Flow
     * ***************************************************************************/
    fun loadSearchInterstitialAd(context: Context) {
        handler = Handler(Looper.myLooper()!!)
        showInterstitialAdLog("Loading Ad ...")
        mInterstitialAdSearch?.let {
            return
        } ?: run {
            showInterstitialAdLog("Loading Ad 1...")
            context.let {
                InterstitialAd.load(it,
                    it.getString(R.string.admob_interistitial_search_high),
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {

                        override fun onAdFailedToLoad(ad: LoadAdError) {
                            mInterstitialAdSearch = null
                            if (failSearchCounter < 2) {
                                failSearchCounter++
                                loadSimpleInterstitialAd(context)
                            } else failSearchCounter = 0
                            showInterstitialAdLog("Ad failed to load because $ad")
                        }

                        override fun onAdLoaded(ad: InterstitialAd) {
                            mInterstitialAdSearch = ad
                            showInterstitialAdLog("Ad successfully loaded")
                        }
                    })
            }

        }

    }

// to show Interstitial Ad Activity reference must be given

    fun showSearchInterstitialAdNew(
        activity: Activity,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("Showing Ad ...")
        if (activity.isNetworkAvailable()) {
            mInterstitialAdSearch?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("Ad closed by user")
                        loadSimpleInterstitialAd(activity)
                        mInterstitialAdSearch = null
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog("Ad failed to show")
                        loadSimpleInterstitialAd(activity)
                        someOpPerformed = true
                        mInterstitialAdSearch = null
                        failListener?.invoke()
                        dismissLoadingDialog()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        showInterstitialAdLog("Ad successfully showed")
                        isInterstitialAdOnScreen = true
                        someOpPerformed = true
                        showListener?.invoke()
                        Handler().postDelayed({
                            dismissLoadingDialog()
                        }, 1000)
                    }
                }

                someOpPerformed = false
                //show interstitialAd
                Handler().postDelayed({
                    if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        it.show(activity)
                    } else {
                        dismissLoadingDialog()
                    }

                }, 1000)


            } ?: run {
                showInterstitialAdLog("Ad is null , not showing ...")
                failListener?.invoke()
            }
        }

    }

    private var loadingDialog: Dialog? = null
    private fun showLoadingDialog(activity: Activity) {
        loadingDialog = Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        loadingDialog?.setContentView(R.layout.dialog_resume_loading)
        loadingDialog?.setCancelable(false)
        if (loadingDialog?.isShowing == false) {
            loadingDialog?.show()
        }
    }

    private fun dismissLoadingDialog() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()
            loadingDialog = null
        }
    }


}