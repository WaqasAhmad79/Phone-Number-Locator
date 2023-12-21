package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canLoadAndShowAd
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
    var camInterstitialAd: InterstitialAd? = null


    private val logTag = "interstitialAdFlow"

    private var someOpPerformed = false

    private val runnable = Runnable {
        showInterstitialAdLog("checking safe operation ... ")
        if (!someOpPerformed) {
            showInterstitialAdLog("safe operation needed , performing")
            mInterstitialAd = null
            camInterstitialAd = null
            failListener?.invoke()
        } else showInterstitialAdLog("safe operation not needed")
    }
    private lateinit var handler: Handler
    private var failListener: (() -> Unit)? = null

    companion object {
        var failCounter = 0

        @Volatile
        private var instance: InterstitialAdClass? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: InterstitialAdClass().also { instance = it }
        }
    }

    /**
     *pre load interstitial ad by priority and show later when needed for splash and other
     * */

    fun loadPriorityInterstitialAds(
        context: Context,
        adIDHigh: String,
        adIDLow: String,
        adLoadedCallback: (InterstitialAd) -> Unit
    ) {
        handler = Handler(Looper.myLooper()!!)
        showInterstitialAdLog("Loading High Ad 2...")
        loadInterstitialAdPriority(context, adIDHigh) { intAd ->
            intAd?.let { adLoadedCallback.invoke(intAd) } ?: run {
                loadInterstitialAdPriority(context, adIDLow) { intAdLow ->
                    intAdLow?.let { adLoadedCallback.invoke(intAdLow) }
                }
            }

        }
    }

    private fun loadInterstitialAdPriority(
        context: Context, adId: String, adLoadedCallback: (InterstitialAd?) -> Unit
    ) {
        context.let {
            if (!canLoadAndShowAd){
                return
            }
            InterstitialAd.load(
                it,
                adId,
                AdRequest.Builder().build(),
                object : InterstitialAdLoadCallback() {

                    override fun onAdFailedToLoad(ad: LoadAdError) {
                        adLoadedCallback.invoke(null)
                        showInterstitialAdLog("High Ad failed to load because $ad")
                    }

                    override fun onAdLoaded(ad: InterstitialAd) {
                        adLoadedCallback.invoke(ad)
                        showInterstitialAdLog("High Ad successfully loaded")
                    }
                })
        }
    }

    fun showPriorityInterstitialAdNew(
        activity: Activity,
        loadAgain: Boolean = false,
        adIDHigh: String? = null,
        adIDLow: String? = null,
        loadAd: ((InterstitialAd) -> Unit)? = null,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("Showing priority Ad ...")
        if (activity.isNetworkAvailable()) {
            interstitialAdPriority?.let {
                isInterstitialAdOnScreen = true
                showLoadingDialog(activity)
                showInterstitialAdLog("priority Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("priority Ad closed by user")
                        isInterstitialAdOnScreen = false
                        interstitialAdPriority = null
                        closeListener?.invoke()
                        if (loadAgain) {
                            loadAgainPriorityInterstitialAd(activity, adIDHigh, adIDLow, loadAd)
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        someOpPerformed = true
                        interstitialAdPriority = null
                        showInterstitialAdLog("priority Ad failed to show")
                        failListener?.invoke()
                        dismissLoadingDialog()
                        if (loadAgain) {
                            loadAgainPriorityInterstitialAd(activity, adIDHigh, adIDLow, loadAd)
                        }
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        isInterstitialAdOnScreen = true
                        showInterstitialAdLog("priority Ad successfully showed")
                        interstitialAdPriority = null
                        someOpPerformed = true
                        showListener?.invoke()
                        Handler().postDelayed({
                            dismissLoadingDialog()
                        }, 1000)
                    }
                }

                someOpPerformed = false

                Handler().postDelayed({
                    if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        it.show(activity)
                    } else {
                        isInterstitialAdOnScreen = false
                        dismissLoadingDialog()
                    }

                }, 1500)

            } ?: run {
                if (loadAgain) {
                    loadAgainPriorityInterstitialAd(activity, adIDHigh, adIDLow, loadAd)
                }
            }
        }
    }

    private fun loadAgainPriorityInterstitialAd(
        activity: Activity,
        adIDHigh: String?,
        adIDLow: String?,
        function: ((InterstitialAd) -> Unit)?
    ) {
        if (adIDHigh != null && adIDLow != null && function != null) {
            loadPriorityInterstitialAds(activity, adIDHigh, adIDLow, function)
        }
    }


    /***************************************************************************
     * Normal Ad Flow
     * ***************************************************************************/
    fun loadSimpleInterstitialAd(context: Context,adId:String) {
        handler = Handler(Looper.myLooper()!!)
        showInterstitialAdLog("Loading Ad ...")
        mInterstitialAd?.let {
            return
        } ?: run {
            showInterstitialAdLog("Loading Ad 1...")
            context.let {
                if (!canLoadAndShowAd){
                    return
                }
                InterstitialAd.load(it,
                    adId,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {

                        override fun onAdFailedToLoad(ad: LoadAdError) {
                            mInterstitialAd = null
                            if (failCounter < 2) {
                                failCounter++
                                loadSimpleInterstitialAd(context,it.getString(R.string.admob_interistitial_others_one))
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

    private fun showInterstitialAdLog(message: String) {
        Log.d(logTag, message)
    }

// to show Interstitial Ad Activity reference must be given

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
                        mInterstitialAd = null
                        loadSimpleInterstitialAd(activity, activity.getString(R.string.admob_interistitial_others_one))
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog("Ad failed to show")
                        someOpPerformed = true
                        mInterstitialAd = null
                        loadSimpleInterstitialAd(activity,activity.getString(R.string.admob_interistitial_others_one))
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
                showInterstitialAdLog( "showSimpleInterstitialAdNew1: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")

                Handler(Looper.myLooper()!!).postDelayed({
                    showInterstitialAdLog( "showSimpleInterstitialAdNew2: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")
                    if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        showInterstitialAdLog( "showSimpleInterstitialAdNew3: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")
                        it.show(activity)
                        showInterstitialAdLog( "showSimpleInterstitialAdNew4: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")
                    } else {
                        showInterstitialAdLog( "showSimpleInterstitialAdNew5: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")
                        dismissLoadingDialog()
                        showInterstitialAdLog( "showSimpleInterstitialAdNew6: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")
                    }
                    showInterstitialAdLog( "showSimpleInterstitialAdNew7: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")

                }, 1500)
                showInterstitialAdLog( "showSimpleInterstitialAdNew8: ${ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)}")


            } ?: run {
                showInterstitialAdLog("Ad is null , not showing ...")
                failListener?.invoke()
            }
        }

    }



    /***************************************************************************
     * cam Ad Flow
     * ***************************************************************************/

   /* fun loadCamInterstitialAd(context: Context,adId:String) {
        handler = Handler(Looper.myLooper()!!)
        showInterstitialAdLog("Loading Ad ...")
        mInterstitialAd?.let {
            return
        } ?: run {
            showInterstitialAdLog("Loading Ad 1...")
            context.let {
                InterstitialAd.load(it,
                    adId,
                    AdRequest.Builder().build(),
                    object : InterstitialAdLoadCallback() {

                        override fun onAdFailedToLoad(ad: LoadAdError) {
                            mInterstitialAd = null
                            if (failCounter < 2) {
                                failCounter++
                                loadSimpleInterstitialAd(context,it.getString(R.string.admob_interistitial_others_one))
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

// to show Interstitial Ad Activity reference must be given

    fun showCamInterstitialAdNew(
        activity: Activity,
        closeListener: (() -> Unit)? = null,
        failListener: (() -> Unit)? = null,
        showListener: (() -> Unit)? = null
    ) {
        showInterstitialAdLog("Showing Ad ...")
        if (activity.isNetworkAvailable()) {
            camInterstitialAd?.let {
                showLoadingDialog(activity)
                showInterstitialAdLog("Ad is not null , Calling show and setting listener ...")
                it.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        showInterstitialAdLog("Ad closed by user")
                        camInterstitialAd = null
                        loadSimpleInterstitialAd(activity, activity.getString(R.string.admob_interistitial_others_one))
                        isInterstitialAdOnScreen = false
                        closeListener?.invoke()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        showInterstitialAdLog("Ad failed to show")
                        someOpPerformed = true
                        camInterstitialAd = null
                        loadSimpleInterstitialAd(activity,activity.getString(R.string.admob_interistitial_others_one))
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
*/


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