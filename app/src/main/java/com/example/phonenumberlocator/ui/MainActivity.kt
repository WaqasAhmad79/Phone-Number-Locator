package com.example.phonenumberlocator.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityMainBinding
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlHelper.PERMISSION_ACCESS_FINE_LOCATION
import com.example.phonenumberlocator.pnlHelper.PERMISSION_CAMERA
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_CONTACTS
import com.example.phonenumberlocator.pnlUtil.PNLAppsUtils
import com.example.phonenumberlocator.pnlUtil.PNLEvents
import com.example.phonenumberlocator.ui.activities.CallLocActivity
import com.example.phonenumberlocator.ui.activities.CamAddressActivity
import com.example.phonenumberlocator.ui.activities.GpsTrackActivity
import com.example.phonenumberlocator.ui.activities.callLocator.PNLCallLocatorActivity
import com.example.phonenumberlocator.ui.activities.PNLLanguageActivity
import com.example.phonenumberlocator.ui.activities.callLocator.PNLPhoneContactsActivity
import com.example.phonenumberlocator.ui.pnlDialog.PNLExitDialog
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : PNLBaseClass<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var navDrawerView: NavigationView


    private val CAMERA_PERMISSION_REQUEST_CODE = 1001

//    private var dialog: PNLResumeLoadingDialog? = null

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
        initViews()
        handleClicks()
        runtimePer()
        /* val adControl = intent.getBooleanExtra("LanguageActivity", false)
         if (adControl) {
             permissionLocation()
         } else if (!adControl) {
             dialog = PNLResumeLoadingDialog(this)
             Log.d("testing", "onCreate: 1")
             if (isNetworkAvailable()) {
                 Log.d("testing", "onCreate: 2")
                 dialog?.show()
                 Log.d("testing", "onCreate: 3")
                 showHighSplashAdmobInterstitial({
                     permissionLocation()
                     Log.d("testing", "onCreate: 4")
                 }, {
                     Log.d("testing", "onCreate: 5")
                     showLowSplashAdmobInterstitial({
                         permissionLocation()
                         Log.d("Hamza", "onCreate: 6")
                     }, {
                         permissionLocation()
                         dialog?.dismiss()
                     }, {
                         Handler().postDelayed({
                             dialog?.dismiss()
                         }, 1000)
                     })
                 }, {
                     Handler().postDelayed({
                         dialog?.dismiss()
                     }, 1000)
                 })

             }
         }

         loadAndReturnAd(
             this@MainActivity,
             resources.getString(R.string.admob_native_others)
         ) { it2 ->
             if (it2 != null) {
                 TrackLocationAppClass.instance.nativeAdOther.value = it2
             }
         }
 */
        EventBus.getDefault().register(this)

    }

    private fun runtimePer() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_MEDIA_IMAGES

                    ), 1
                )
            } else {
                binding.content.ads.beVisible()
//                loadAd()
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED

            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 1
                )
            } else {
                binding.content.ads.beVisible()
//                loadAd()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    binding.content.ads.beVisible()
//                    loadAd()
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        isAppOpenEnable = false
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
//                    isAppOpenEnable = true
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(resources.getString(R.string.privacy_policy_link))
                    )
                    startActivity(browserIntent)
                    true
                }

                R.id.nav_share -> {
//                    isAppOpenEnable = true
                    PNLAppsUtils.shareApp(this)
                    true
                }

                R.id.nav_rate -> {
//                    isAppOpenEnable = true
                    PNLAppsUtils.rateUs(this)
                    true
                }

                R.id.nav_language -> {
                    startActivity(
                        Intent(
                            this@MainActivity, PNLLanguageActivity::class.java
                        ).putExtra("setting", false)
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
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLCallLocatorActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, CallLocActivity::class.java))
//            }

        }
        binding.content.gpsTracker.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLPhoneContactsActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, GpsTrackActivity::class.java))
//           }

        }
        binding.content.camAddress.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLCamAddressActivity::class.java))
//            }
//            else{
            startActivity(Intent(this, CamAddressActivity::class.java))
//            }

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


    /*  private fun loadAd() {
          if (isNetworkAvailable()) {
              binding.content.ads.beVisible()
              TrackLocationAppClass.instance.nativeAdMain.observe(this){
                  showLoadedNativeAd(this,binding.content.ads, R.layout.layout_admob_native_ad,it)
              }
          } else {
              binding.content.ads.beGone()
          }
      }*/


}