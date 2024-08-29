package com.example.phonenumberlocator.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdConfig
import com.example.phonenumberlocator.admob_ads.banner_ad.BannerAdHelper
import com.example.phonenumberlocator.admob_ads.isAppOpenEnable
import com.example.phonenumberlocator.databinding.ActivityCamAdresBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.ui.activities.camAddress.PNLAreaCalculatorActivity
import com.example.phonenumberlocator.ui.activities.camAddress.PNLDistanceFinderActivity
import com.example.phonenumberlocator.ui.activities.camAddress.PNLGpsAddressActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CamAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCamAdresBinding

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamAdresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleClicks()
        hideNavBar()
        handleBannerAd()

    }

    private fun handleBannerAd() {
        if (RemoteConfigClass.banner_cam_address_activity) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()
                val config = BannerAdConfig(
                    getString(R.string.ad_mob_banner_id),
                    canShowAds = true,
                    canReloadAds = true,
                    isCollapsibleAd = false
                )

                val bannerAdHelperClass = BannerAdHelper(
                    activity = this,
                    lifecycleOwner = this,
                    config = config
                )

                bannerAdHelperClass.myView = binding.ads
                bannerAdHelperClass.shimmer = binding.bannerView.customBannerShimmer
                bannerAdHelperClass.showBannerAdmob()
            }

        } else {
            binding.ads.beGone()
        }
    }

    private fun handleClicks() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.gpsAddress.setOnClickListener {
            isAppOpenEnable = true
            dispatchTakePictureIntent()
        }
        binding.areaCalculator.setOnClickListener {

            startActivity(Intent(this, PNLAreaCalculatorActivity::class.java))

        }
        binding.distanceFinder.setOnClickListener {

            startActivity(Intent(this, PNLDistanceFinderActivity::class.java))


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
                    "mobile.number.trackerapp.fileProvider",
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
            ".jpg",  /*suffix*/
            storageDir  /*directory*/
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
            val intent = Intent(this, PNLGpsAddressActivity::class.java)
            intent.putExtra("imagePath", currentPhotoPath)
            startActivity(intent)
        }
    }

    override fun onResume() {
        isAppOpenEnable = false
        super.onResume()
    }

}