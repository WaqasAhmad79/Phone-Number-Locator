package com.example.phonenumberlocator.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityMainBinding
import com.example.phonenumberlocator.pnlHelper.PERMISSION_ACCESS_FINE_LOCATION
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_CONTACTS
import com.example.phonenumberlocator.pnlUtil.PNLAppsUtils
import com.example.phonenumberlocator.pnlUtil.PNLEvents
import com.example.phonenumberlocator.ui.activities.PNLAreaCalculatorActivity
import com.example.phonenumberlocator.ui.activities.PNLCallLocatorActivity
import com.example.phonenumberlocator.ui.activities.PNLCamAddressActivity
import com.example.phonenumberlocator.ui.activities.PNLDeviceInfoActivity
import com.example.phonenumberlocator.ui.activities.PNLIsdStdActivity
import com.example.phonenumberlocator.ui.activities.PNLLanguageActivity
import com.example.phonenumberlocator.ui.activities.PNLLiveTrafficActivity
import com.example.phonenumberlocator.ui.activities.PNLLiveWeatherActivity
import com.example.phonenumberlocator.ui.activities.PNLNearbyPlacesActivity
import com.example.phonenumberlocator.ui.activities.PNLPhoneContactsActivity
import com.example.phonenumberlocator.ui.activities.PNLShareLocationActivity
import com.example.phonenumberlocator.ui.pnlDialog.PNLExitDialog
import com.google.android.material.navigation.NavigationView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity  : PNLBaseClass<ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var navDrawerView: NavigationView

    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private lateinit var currentPhotoPath: String
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

      /*  // Assuming you have a LottieAnimationView in your layout with id 'lottieAnimationView'
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.mainJson)

        // Set the images folder before loading the animation
        lottieAnimationView.imageAssetsFolder = "images"

        // Now, load the Lottie animation
        lottieAnimationView.setAnimation("main_screen.json") // Replace 'your_animation.json' with your animation file name
        lottieAnimationView.playAnimation()*/

    }

    /** this code also worked for get permissions **/
    private fun permissionLocation() {
        handlePermission(PERMISSION_ACCESS_FINE_LOCATION) { locationGranted ->
            if (locationGranted) {
                handlePermission(PERMISSION_READ_CONTACTS) { contactsGranted ->
                    if (contactsGranted) {
                        // All permissions granted, proceed with showing ads
//                        binding.content.ads.beVisible()
//                        loadAd()
                    } else {
                        Toast.makeText(
                            this@MainActivity, R.string.permission_required,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this@MainActivity, R.string.permission_required,
                    Toast.LENGTH_SHORT
                ).show()
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
                startActivity(Intent(this, PNLCallLocatorActivity::class.java))
//            }

        }
        binding.content.contactList.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLPhoneContactsActivity::class.java))
//            }
//            else{
                startActivity(Intent(this, PNLPhoneContactsActivity::class.java))
//           }

        }
        binding.content.liveWeather.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLLiveWeatherActivity::class.java))
//            }
//            else{
                startActivity(Intent(this, PNLLiveWeatherActivity::class.java))
//            }

        }
        binding.content.liveTraffic.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLLiveTrafficActivity::class.java))
//            }
//            else{
                startActivity(Intent(this, PNLLiveTrafficActivity::class.java))
//            }

        }
        binding.content.nearbyPlaces.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLNearbyPlacesActivity::class.java))
//            }
//            else{
                startActivity(Intent(this, PNLNearbyPlacesActivity::class.java))
//            }

        }
        binding.content.areaCalculator.setOnClickListener {
            val intent = Intent(this@MainActivity, PNLAreaCalculatorActivity::class.java)
            startActivity(intent)
        }
        binding.content.deviceInfo.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLDeviceInfoActivity::class.java))
//            }
//            else{
                startActivity(Intent(this, PNLDeviceInfoActivity::class.java))
//            }

        }
        binding.content.shareLocation.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLShareLocationActivity::class.java))
//            }
//            else{
                startActivity(Intent(this, PNLShareLocationActivity::class.java))
//            }

        }
        binding.content.camAddress.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLCamAddressActivity::class.java))
//            }
//            else{
            dispatchTakePictureIntent()
//            }

        }
        binding.content.isdStd.setOnClickListener {
//            if (!delayAdShown){
//                interstitialCounter++
//                startActivity(Intent(this, PNLIsdStdActivity::class.java))
//            }
//            else{
                startActivity(Intent(this, PNLIsdStdActivity::class.java))
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

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this,
                    "com.example.locationtracker.fileProvider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // The image was captured successfully
            // Pass the image path to the next screen or process it as needed
            val intent = Intent(this, PNLCamAddressActivity::class.java)
            intent.putExtra("imagePath", currentPhotoPath)
            startActivity(intent)
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