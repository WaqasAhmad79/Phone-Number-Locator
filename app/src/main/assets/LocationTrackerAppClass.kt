package com.example.locationtracker

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
import com.example.locationtracker.admob_ads.OpenApp
import com.example.locationtracker.admob_ads.loadAdmobInterstitial
import com.example.locationtracker.admob_ads.loadHighMainAdmobInterstitial
import com.example.locationtracker.admob_ads.loadHighSplashAdmobInterstitial
import com.example.locationtracker.admob_ads.loadLowMainAdmobInterstitial
import com.example.locationtracker.admob_ads.loadLowSplashAdmobInterstitial
import com.example.locationtracker.ltExtensionFun.countryIso
import com.example.locationtracker.ltModel.LTCountryData
import com.example.locationtracker.ltModel.LTCountryModel
import com.example.locationtracker.ltUtil.readToObjectList
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
import java.util.*

@HiltAndroidApp
class LocationTrackerAppClass : Application(), LifecycleObserver {
    var countries = mutableListOf<LTCountryModel>()

    var countryDataList: List<LTCountryData> = ArrayList()
    val nativeAdLang: MutableLiveData<NativeAd> = MutableLiveData()
    val nativeAdMain: MutableLiveData<NativeAd> = MutableLiveData()
    val nativeAdOther: MutableLiveData<NativeAd> = MutableLiveData()

    fun getDetailLanguageCategory(): List<LTCountryData> {
        val inputStream = assets.open("country_codes.json")
        val text = inputStream.bufferedReader().use(BufferedReader::readText)
        return Gson().fromJson(text, Array<LTCountryData>::class.java).toList()
    }

    private var currentActivity: Activity? = null

    var intent: Intent? = null


    override fun onCreate() {
        super.onCreate()
        //bubble level
//        PreferenceHelper.initPrefs(this)
        instance = this

        startKoin {
            androidLogger()
            androidContext(this@LocationTrackerAppClass)
//            modules(appModules)
            countries = "isdcodes.json".readToObjectList(context, LTCountryModel::class.java)
            CoroutineScope(Dispatchers.IO).launch {
                countryDataList = getDetailLanguageCategory()
            }
        }
        app_class = this@LocationTrackerAppClass
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        OpenApp(this)
        // Log the Mobile Ads SDK version.
        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())
        MobileAds.initialize(this) { }
        loadHighSplashAdmobInterstitial()
        loadLowSplashAdmobInterstitial()
        loadHighMainAdmobInterstitial()
        loadLowMainAdmobInterstitial()
        loadAdmobInterstitial()

        applicationInstance = this@LocationTrackerAppClass
        instance = this@LocationTrackerAppClass

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())

        app_class = this@LocationTrackerAppClass
    }

    //used to get default country code like=PK
    fun getDefaultCountry(Callback: (LTCountryModel) -> Unit) {
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
    fun findCountryByDialCode(dialCode: String, Callback: (LTCountryModel) -> Unit) {
        countries.forEach {
            if (it.dialCode.replace(" ", "").equals(dialCode, ignoreCase = true)) {
                Callback(it)
                return
            }
        }

    }


    companion object {
        lateinit var instance: LocationTrackerAppClass
        var isAlready = false

        @SuppressLint("StaticFieldLeak")
        @get:Synchronized
        var app_class: LocationTrackerAppClass? = null
            private set
        private const val TAG = "TextOnPhotos"
        private const val LOG_TAG = "AppOpenAdManager"


        const val PRO_VERSION_PRODUCT_ID = "pro_version"
        val context: Context get() = (instance as LocationTrackerAppClass).applicationContext
        var applicationInstance: LocationTrackerAppClass? = null

    }


}
