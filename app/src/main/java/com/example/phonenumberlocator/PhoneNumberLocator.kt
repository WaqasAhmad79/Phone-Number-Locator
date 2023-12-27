package com.example.phonenumberlocator

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.admob_ads.OpenApp
import com.example.phonenumberlocator.admob_ads.interstitialAdPriority
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.loadPriorityAdmobInterstitial
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.countryIso
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlModel.PNLCountryData
import com.example.phonenumberlocator.pnlModel.PNLCountryModel
import com.example.tracklocation.tlUtil.readToObjectList
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.nativead.NativeAd
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.io.BufferedReader
import java.util.ArrayList


@HiltAndroidApp
class PhoneNumberLocator: Application(), LifecycleObserver {
    var countries = mutableListOf<PNLCountryModel>()

    var countryDataList: List<PNLCountryData> = ArrayList()


    fun getDetailLanguageCategory(): List<PNLCountryData> {
        val inputStream = assets.open("country_codes.json")
        val text = inputStream.bufferedReader().use(BufferedReader::readText)
        return Gson().fromJson(text, Array<PNLCountryData>::class.java).toList()
    }

    var intent: Intent? = null

    fun setAppOpenAd(){
        OpenApp(this)
        Log.d(TAG, "TESTINGOpenApp setAppOpenAd: ")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@PhoneNumberLocator)
//            modules(appModules)
            countries = "isdcodes.json".readToObjectList(context, PNLCountryModel::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                countryDataList = getDetailLanguageCategory()
            }
        }
        app_class = this@PhoneNumberLocator
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        // Log the Mobile Ads SDK version.
        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())


        applicationInstance = this@PhoneNumberLocator
        instance = this@PhoneNumberLocator

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())

        app_class = this@PhoneNumberLocator
    }

    //used to get default country code like=PK
    fun getDefaultCountry(Callback: (PNLCountryModel) -> Unit) {
        countries.forEach {
            if (it.code.equals(countryIso, ignoreCase = true)) {
                Callback(it)
                return
            }
        }
    }

    //used to get default country code like=PK
    @RequiresApi(Build.VERSION_CODES.N)
    fun getCountryISD(countryCode: String, Callback: (String) -> Unit) {
        countryDataList.forEach {
            if (it.CODE1.equals(countryCode, true) || it.CODE2.equals(countryCode, true)) {
                Callback.invoke(it.ISD.toString())
                return@forEach
            }
        }
    }


    //used to get country name from dial code like pakistan from +92
    fun findCountryByDialCode(dialCode: String, Callback: (PNLCountryModel) -> Unit) {
        countries.forEach {
            if (it.dialCode.replace(" ", "").equals(dialCode, ignoreCase = true)) {
                Callback(it)
                return
            }
        }

    }


    companion object {
        lateinit var instance: PhoneNumberLocator
        val nativeAdLang: MutableLiveData<NativeAd> = MutableLiveData()
        val nativeAdSmall: MutableLiveData<NativeAd> = MutableLiveData()
        val nativeAdLarge: MutableLiveData<NativeAd> = MutableLiveData()
        val nativeAdBoarding: MutableLiveData<NativeAd> = MutableLiveData()
        val onBoardNative1: MutableLiveData<NativeAd?> = MutableLiveData()
        val onBoardNative2: MutableLiveData<NativeAd?> = MutableLiveData()
        val onBoardNative3: MutableLiveData<NativeAd?> = MutableLiveData()
        val onBoardNative4: MutableLiveData<NativeAd?> = MutableLiveData()

        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        var app_class: PhoneNumberLocator? = null
            private set
        private const val TAG = "TextOnPhotos"
        private const val LOG_TAG = "AppOpenAdManager"


        const val PRO_VERSION_PRODUCT_ID = "pro_version"
        val context: Context get() = (instance as PhoneNumberLocator).applicationContext
        var applicationInstance: PhoneNumberLocator? = null

    }


 /*   private fun handleAds() {
        if (isNetworkAvailable()) {
            if (!baseConfig.appStarted) {
                loadAndReturnAd(
                    this@PhoneNumberLocator,
                    resources.getString(R.string.admob_native_lang_high)
                ) {
                    Log.d("NL", "loadHighOrLowNativeAd: ad:$it ")
                    if (it != null) {
                        nativeAdLang.value = it
                    } else {
                        loadAndReturnAd(
                            this@PhoneNumberLocator,
                            resources.getString(R.string.admob_native_lang_low)
                        ) { it2 ->
                            if (it2 != null) {
                                nativeAdLang.value = it2
                            }
                        }
                    }
                }
            }

            else if (!baseConfig.isAppIntroComplete) {
                loadAndReturnAd(
                    this@PhoneNumberLocator,
                    resources.getString(R.string.admob_native_boarding_high)
                ) {
                    if (it != null) {
                        nativeAdBoarding.value = it
                    } else {
                        loadAndReturnAd(
                            this@PhoneNumberLocator,
                            resources.getString(R.string.admob_native_boarding_low)
                        ) { it2 ->
                            if (it2 != null) {
                                nativeAdBoarding.value = it2
                            }
                        }
                    }
                }
            }
        }
        loadPriorityAdmobInterstitial(
            getString(R.string.admob_splash_interistitial_high),
            getString(R.string.admob_splash_interistitial_low)
        ) {
            Log.d("NL", "loadAdmobInterstitialPriority successful")
            interstitialAdPriority = it
        }
    }*/

}
