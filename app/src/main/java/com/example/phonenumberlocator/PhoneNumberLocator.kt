package com.example.phonenumberlocator

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleObserver
import com.example.phonenumberlocator.pnlExtensionFun.countryIso
import com.example.tracklocation.tlModel.PNLCountryData
import com.example.tracklocation.tlModel.PNLCountryModel
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import java.io.BufferedReader
import java.util.ArrayList


@HiltAndroidApp
class PhoneNumberLocator: Application(), LifecycleObserver {
    var countries = mutableListOf<PNLCountryModel>()

    var countryDataList: List<PNLCountryData> = ArrayList()
//    val nativeAdLang: MutableLiveData<NativeAd> = MutableLiveData()
//    val nativeAdMain: MutableLiveData<NativeAd> = MutableLiveData()
//    val nativeAdOther: MutableLiveData<NativeAd> = MutableLiveData()

    fun getDetailLanguageCategory(): List<PNLCountryData> {
        val inputStream = assets.open("country_codes.json")
        val text = inputStream.bufferedReader().use(BufferedReader::readText)
        return Gson().fromJson(text, Array<PNLCountryData>::class.java).toList()
    }

    private var currentActivity: Activity? = null

    var intent: Intent? = null


    override fun onCreate() {
        super.onCreate()
        //bubble level
//        PreferenceHelper.initPrefs(this)
        instance = this

//        startKoin {
//            androidLogger()
//            androidContext(this@PhoneNumberLocator)
////            modules(appModules)
//            countries = "isdcodes.json".readToObjectList(context, PNLCountryModel::class.java)
//            CoroutineScope(Dispatchers.IO).launch {
//                countryDataList = getDetailLanguageCategory()
//            }
//        }
        app_class = this@PhoneNumberLocator
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//        OpenApp(this)
//        // Log the Mobile Ads SDK version.
//        Log.d(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())
//        MobileAds.initialize(this) { }
//        loadHighSplashAdmobInterstitial()
//        loadLowSplashAdmobInterstitial()
//        loadHighMainAdmobInterstitial()
//        loadLowMainAdmobInterstitial()
//        loadAdmobInterstitial()

        applicationInstance = this@PhoneNumberLocator
        instance = this@PhoneNumberLocator

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())

//        app_class = this@PhoneNumberLocator
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
        var isAlready = false

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


}
