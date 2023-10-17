package com.example.phonenumberlocator.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.canShowAppOpen
import com.example.phonenumberlocator.admob_ads.interstitialAdPriority
import com.example.phonenumberlocator.admob_ads.isShowAD
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.loadSimpleAdmobInterstitial
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.admob_ads.showPriorityAdmobInterstitial
import com.example.phonenumberlocator.databinding.ActivityMainBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlUtil.PNLAppsUtils
import com.example.phonenumberlocator.pnlUtil.PNLEvents
import com.example.phonenumberlocator.ui.activities.CallLocActivity
import com.example.phonenumberlocator.ui.activities.CamAddressActivity
import com.example.phonenumberlocator.ui.activities.GpsTrackActivity
import com.example.phonenumberlocator.ui.activities.PNLLanguageActivity
import com.example.phonenumberlocator.ui.pnlDialog.PNLExitDialog
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        Log.d("isComingFromSplash", "onCreate: $isShowAD")
        if (!isShowAD) {
            requestPermission()
        } else {
            Log.d(TAG, "onCreate: ")
            showPriorityAdmobInterstitial(true, getString(R.string.admob_interistitial_search_high),
                getString(R.string.admob_interistitial_others_one), {
                    interstitialAdPriority = it
                    requestPermission()
                })
        }
        loadSimpleAdmobInterstitial()
//        loadCamAdmobInterstitial()
        loadAndReturnAd(
            this@MainActivity,
            resources.getString(R.string.admob_native_small)
        ) { it2 ->
            if (it2 != null) {
                PhoneNumberLocator.instance.nativeAdSmall.value = it2
            }
        }
        loadAndReturnAd(
            this@MainActivity,
            resources.getString(R.string.admob_native_large)
        ) { it2 ->
            if (it2 != null) {
                PhoneNumberLocator.instance.nativeAdLarge.value = it2
            }
        }

//        loadAd()

        EventBus.getDefault().register(this)

        initViews()
        handleClicks()

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
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
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
        canShowAppOpen = false
    }

    private fun initViews() {
        binding.content.drawerIcon.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        navDrawerView = findViewById(R.id.nav_view)
        navDrawerView.itemIconTintList = null
        navDrawerView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_privacy -> {
                    canShowAppOpen = true
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(resources.getString(R.string.privacy_policy_link))
                    )
                    startActivity(browserIntent)
                    true
                }

                R.id.nav_share -> {
                    canShowAppOpen = true
                    PNLAppsUtils.shareApp(this)
                    true
                }

                R.id.nav_rate -> {
                    canShowAppOpen = true
                    PNLAppsUtils.rateUs(this)
                    true
                }

                R.id.nav_language -> {
                    startActivity(
                        Intent(
                            this@MainActivity, PNLLanguageActivity::class.java
                        ).putExtra("setting", true)
                    )
                    true
                }

                else -> false
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun handleClicks() {
        binding.content.callLocator.setOnClickListener {
            startActivity(Intent(this, CallLocActivity::class.java))

        }
        binding.content.gpsTracker.setOnClickListener {

            startActivity(Intent(this, GpsTrackActivity::class.java))


        }
        binding.content.camAddress.setOnClickListener {

            startActivity(Intent(this, CamAddressActivity::class.java))

        }


    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            PNLExitDialog(this) {
                finishAffinity()
//            super.onBackPressed()
            }
        }
    }


 /*   private fun loadAd() {
        if (isNetworkAvailable()) {
            binding.content.ads.beVisible()
            PhoneNumberLocator.instance.nativeAdMain.observe(this) {
                showLoadedNativeAd(this, binding.content.ads, R.layout.layout_admob_native_ad_withou_tmedia, it)
            }
        } else {
            binding.content.ads.beGone()
        }
    }*/

    override fun onPause() {
        super.onPause()
        isShowAD = false
    }


}