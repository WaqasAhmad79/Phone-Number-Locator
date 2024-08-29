package com.example.phonenumberlocator.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdExit
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLarge
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdSmall
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.OpenApp.isShowingAd
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdConfig
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdHelper
import com.example.phonenumberlocator.admob_ads.isAppOpenEnable
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.showExitAdmobInterstitial
import com.example.phonenumberlocator.admob_ads.showNormalAdmobInterstitial
import com.example.phonenumberlocator.databinding.ActivityMainBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlUtil.PNLEvents
import com.example.phonenumberlocator.ui.activities.CallLocActivity
import com.example.phonenumberlocator.ui.activities.CamAddressActivity
import com.example.phonenumberlocator.ui.activities.DrawerActivity
import com.example.phonenumberlocator.ui.activities.GpsTrackActivity
import com.example.phonenumberlocator.ui.activities.ThankyouScreenActivity
import com.example.phonenumberlocator.ui.pnlDialog.PNLExitDialog
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.system.exitProcess

class MainActivity : PNLBaseClass<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var navDrawerView: NavigationView

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshLanguageStrings(event: PNLEvents.RefreshLanguageStrings) {
        Log.d("refreshLanguageStrings", "RefreshLanguageStrings implemented ")
        // Restart the activity to apply the new configuration
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleAds()
        EventBus.getDefault().register(this)
//        initViews()
        handleClicks()
        handleBannerAd()
        hideNavBar()

    }

    fun handleBannerAd() {
        if (RemoteConfigClass.banner_main_activity) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()
                val config = BannerAdConfig(
                    getString(R.string.ad_mob_banner_id), true, true, true
                )
                val bannerAdHelperClass = BannerAdHelper(this, this, config)
                bannerAdHelperClass.myView = binding.ads
                bannerAdHelperClass.shimmer = binding.bannerView.customBannerShimmer
                bannerAdHelperClass.showBannerAdmob()
            }
        } else {
            binding.ads.beGone()
        }
    }

    private fun handleAds() {
        if (isNetworkAvailable()) {

            Log.d("isComingFromSplash", "onCreate: $isShowingAd")
            if (!isShowingAd) {
                requestPermission()
            } else {
                Log.d(TAG, "onCreate: ")
                showNormalAdmobInterstitial { requestPermission() }
            }

            // native ad small loading
          /*  if (RemoteConfigClass.native_pnl_call_locator_activity && PhoneNumberLocator.canRequestAd) {
                loadAndReturnAd(
                    this@MainActivity,
                    resources.getString(R.string.admob_native_small)
                ) { it2 ->
                    if (it2 != null) {
                        nativeAdSmall.value = it2
                    }
                }
            } else {
                nativeAdSmall.postValue(null)
            }*/

            // native ad large loading
            if (
                (RemoteConfigClass.native_call_locator_details_activity
                        || RemoteConfigClass.native_gps_location_activity
                        || RemoteConfigClass.native_pnl_contacts_detailed_activity)
                && (PhoneNumberLocator.canRequestAd)
            ) {
                loadAndReturnAd(
                    this@MainActivity,
                    resources.getString(R.string.admob_native_large)
                ) { it2 ->
                    if (it2 != null) {
                        nativeAdLarge.value = it2
                    }
                }
            } else {
                nativeAdLarge.postValue(null)
            }

//            nativeAdExit loading
            if (RemoteConfigClass.native_exit_ad && PhoneNumberLocator.canRequestAd) {
                loadAndReturnAd(
                    this@MainActivity,
                    resources.getString(R.string.admob_native_small)
                ) { it2 ->
                    if (it2 != null) {
                        nativeAdExit.value = it2
                    }
                }
            } else {
                nativeAdExit.postValue(null)
            }

        }
    }

    private fun checkPermission(): Boolean {
        val permissionsToCheck = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS
        )

        return permissionsToCheck.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

    }

    private fun requestPermission() {
        val permissionsToRequest = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS
        )
        requestPermissions(permissionsToRequest, 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                requestPermission()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isAppOpenEnable = false

    }


    @SuppressLint("SuspiciousIndentation")
    private fun handleClicks() {

        binding.callLocator.setOnClickListener {
            startActivity(Intent(this, CallLocActivity::class.java))
        }
        binding.gpsTracker.setOnClickListener {
            startActivity(Intent(this, GpsTrackActivity::class.java))
        }
        binding.camAddress.setOnClickListener {
            startActivity(Intent(this, CamAddressActivity::class.java))
        }
        binding.drawerIcon.setOnClickListener {
            startActivity(Intent(this, DrawerActivity::class.java))
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {


        if (RemoteConfigClass.inter_exit_app_activity) {

            showExitAdmobInterstitial(
                {
                    // go to ThankyouScreenActivity if ad shown successfully
                    startActivity(Intent(this, ThankyouScreenActivity::class.java))
                    finish()
                }, {

                    // Show only exit dialog if the ad failed to show and exit app
                    nativeAdExit.observe(this) { ad ->
                        if (ad != null) {
                            PNLExitDialog(this, {
                                finish()
                                exitProcess(0)
                            }, ad)
                        }
                    }

                },
                {

                }
            )
        } else {
            // Show only exit dialog if the inter ad remote is off
            nativeAdExit.observe(this) { ad ->
                if (ad != null) {
                    PNLExitDialog(this, {
                        finish()
                        exitProcess(0)
                    }, ad)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isShowingAd = false
    }

}