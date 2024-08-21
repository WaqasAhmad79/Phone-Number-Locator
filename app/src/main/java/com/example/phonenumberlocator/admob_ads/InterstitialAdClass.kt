package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canRequestAd
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

@Suppress("DEPRECATION")
open class InterstitialAdClass {
    var mInterstitialAd: InterstitialAd? = null
//    var mSearchInterstitialAd: InterstitialAd? = null
    private val logTag = "interstitialAdFlow"
    private var someOpPerformed = false

    companion object {
        var failCounter = 0
        var failSearchCounter = 0
        var failBackCounter = 0

        @Volatile
        private var instance: InterstitialAdClass? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InterstitialAdClass().also { instance = it }
        }
    }

    /***************************************************************************
     * Normal Ad Flow
     * ***************************************************************************/

    fun loadNormalInterstitialAd(context: Context, adId: String) {
        showInterstitialAdLog("Loading Ad ...")
        if (canRequestAd) {
            mInterstitialAd?.let {
                return
            } ?: run {
                showInterstitialAdLog("loadNormalInterstitialAd Loading Ad 1...")
                context.let {
                    InterstitialAd.load(it,
                        adId,
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {

                            override fun onAdFailedToLoad(ad: LoadAdError) {
                                mInterstitialAd = null
                                if (failCounter < 2) {
                                    failCounter++
                                    loadNormalInterstitialAd(context, adId)
                                } else failCounter = 0
                                showInterstitialAdLog("loadNormalInterstitialAd Ad failed to load because $ad")
                            }

                            override fun onAdLoaded(ad: InterstitialAd) {
                                mInterstitialAd = ad
                                showInterstitialAdLog("loadNormalInterstitialAd Ad successfully loaded")
                            }
                        })
                }

            }
        }

    }

    fun showNormalInterstitialAdNew(
        activity: Activity,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("showNormalInterstitialAdNew Showing Ad ...")
        if (activity.isNetworkAvailable() && canRequestAd) {
            mInterstitialAd?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("showNormalInterstitialAdNew Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("showNormalInterstitialAdNew Ad closed by user")
                        mInterstitialAd = null
                        isInterstitialAdOnScreen = false
                        loadNormalInterstitialAd(
                            activity,
                            activity.getString(R.string.admob_interistitial_others_one)
                        )
                        closeListener?.invoke()
                        Handler(Looper.myLooper()!!).postDelayed({
                            isInterstitialRecentClosed = false
                        }, 1000)
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog(" showNormalInterstitialAdNewAd failed to show")
                        someOpPerformed = true
                        mInterstitialAd = null
                        failListener?.invoke()
                        loadNormalInterstitialAd(
                            activity,
                            activity.getString(R.string.admob_interistitial_others_one)
                        )
                        dismissLoadingDialog()
                        Handler(Looper.myLooper()!!).postDelayed({
                            isInterstitialRecentClosed = false
                        }, 1000)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        showInterstitialAdLog("showNormalInterstitialAdNew Ad successfully showed")
                        isInterstitialAdOnScreen = true
                        someOpPerformed = true
                        showListener?.invoke()
                        Handler().postDelayed({
                            if (!activity.isDestroyed) {
                                dismissLoadingDialog()
                            }
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
                showInterstitialAdLog("showNormalInterstitialAdNew Ad is null , not showing ...")
                failListener?.invoke()
            }
        }

    }

    /***************************************************************************
     * Search Ad Flow
     * ***************************************************************************/

   /* fun loadSearchInterstitialAd(context: Context,id:String) {
        showInterstitialAdLog(" load_search_ad Loading Ad ...")
        if (canRequestAd) {
            mSearchInterstitialAd?.let {
                return
            } ?: run {
                showInterstitialAdLog(" load_search_ad Loading Ad 1...")
                context.let {
                    InterstitialAd.load(it,
                        it.getString(R.string.admob_interistitial_others_one),
                        AdRequest.Builder().build(),
                        object : InterstitialAdLoadCallback() {

                            override fun onAdFailedToLoad(ad: LoadAdError) {
                                mSearchInterstitialAd = null
                                if (failSearchCounter < 2) {
                                    failSearchCounter++
                                    loadSearchInterstitialAd(context,id)
                                } else failSearchCounter = 0
                                showInterstitialAdLog(" load_search_ad Ad failed to load because $ad")
                            }

                            override fun onAdLoaded(ad: InterstitialAd) {
                                mSearchInterstitialAd = ad
                                showInterstitialAdLog(" load_search_ad Ad successfully loaded")
                            }
                        })
                }

            }
        }

    }

    fun showInterSearchInterstitialAdNew(
        activity: Activity,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("showSearchAd  Showing Ad ...")
        if (activity.isNetworkAvailable() && canRequestAd) {
            mSearchInterstitialAd?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("showSearchAd Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("showSearchAd Ad closed by user")
                        mSearchInterstitialAd = null
                        loadSearchInterstitialAd(activity)
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                        Handler(Looper.myLooper()!!).postDelayed({
                            isInterstitialRecentClosed = false
                        }, 1000)
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog("showSearchAd Ad failed to show")
                        someOpPerformed = true
                        mSearchInterstitialAd = null
                        loadSearchInterstitialAd(activity)
                        failListener?.invoke()
                        dismissLoadingDialog()
                        Handler(Looper.myLooper()!!).postDelayed({
                            isInterstitialRecentClosed = false
                        }, 1000)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        showInterstitialAdLog("showSearchAd Ad successfully showed")
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
                showInterstitialAdLog("showSearchAd Ad is null , not showing ...")
                failListener?.invoke()
            }
        }

    }
*/





    /**
     *
     *Exit app ad flow
     * */


    fun showExitInterstitialAdNew(
        activity: Activity,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("showNormalInterstitialAdNew Showing Ad ...")
        if (activity.isNetworkAvailable() && canRequestAd) {
            mInterstitialAd?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("showNormalInterstitialAdNew Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("showNormalInterstitialAdNew Ad closed by user")
                        mInterstitialAd = null
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                        Handler(Looper.myLooper()!!).postDelayed({
                            isInterstitialRecentClosed = false
                        }, 1000)
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog(" showNormalInterstitialAdNewAd failed to show")
                        someOpPerformed = true
                        mInterstitialAd = null
                        failListener?.invoke()
                        dismissLoadingDialog()
                        Handler(Looper.myLooper()!!).postDelayed({
                            isInterstitialRecentClosed = false
                        }, 1000)
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        showInterstitialAdLog("showNormalInterstitialAdNew Ad successfully showed")
                        isInterstitialAdOnScreen = true
                        someOpPerformed = true
                        showListener?.invoke()
                        Handler().postDelayed({
                            if (!activity.isDestroyed) {
                                dismissLoadingDialog()
                            }
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
                showInterstitialAdLog("showNormalInterstitialAdNew Ad is null , not showing ...")
                failListener?.invoke()
            }
        }

    }

    private fun showInterstitialAdLog(message: String) {
        Log.d(logTag, message)
    }

    private var loadingDialog: Dialog? = null
    private fun showLoadingDialog(activity: Activity) {
        isInterstitialRecentClosed = true
        loadingDialog = Dialog(activity, R.style.AppTheme2)
        loadingDialog?.setContentView(R.layout.dialog_resume_loading)
        loadingDialog?.setCancelable(false)
        if (loadingDialog?.isShowing == false && !activity.isDestroyed) {
            loadingDialog?.show()
        }
    }

    private fun dismissLoadingDialog() {
        if (loadingDialog?.isShowing == true && loadingDialog != null) {
            loadingDialog?.dismiss()

        }
    }


}