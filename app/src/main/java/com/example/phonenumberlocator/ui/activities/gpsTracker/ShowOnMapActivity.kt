package com.example.phonenumberlocator.ui.activities.gpsTracker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnlshareLocationBinding
import com.example.phonenumberlocator.databinding.ActivityShowOnMapBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.copyText
import com.example.phonenumberlocator.pnlExtensionFun.gpsStatusCheck
import com.example.phonenumberlocator.pnlExtensionFun.shareCurrentLocation
import com.example.phonenumberlocator.pnlExtensionFun.toast
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class ShowOnMapActivity : PNLBaseClass<ActivityShowOnMapBinding>() {

    override fun getViewBinding(): ActivityShowOnMapBinding {
        return ActivityShowOnMapBinding.inflate(layoutInflater)
    }
    @Inject
    lateinit var checkInternetConnection: PNLCheckInternetConnection

    private var zoom = 16
    private var smf: SupportMapFragment? = null
    private var client: FusedLocationProviderClient? = null
    private var latLng: LatLng? = null
    private var latLngCurrent: LatLng? = null

    //    private var dialog: ResumeLoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*  dialog = ResumeLoadingDialog(this)

          if (isTimeDifferenceGreaterThan20Seconds()) {
              if (isNetworkAvailable()) {
                  dialog?.show()
                  showAdmobInterstitial({
                  }, { dialog?.dismiss() }, {
                      if(delayAdShown)
                      {
                          interstitialCounter =0
                      }

                      Handler().postDelayed({
                          dialog?.dismiss()
                      }, 1000)
                  })
              }
          }*/

        gpsStatusCheck() {
            if (it) {
                initView()
//                showBannerAdmob(binding.flBanner,this,getString(R.string.ad_mob_banner_id))
            }
        }

        clickListeners()
    }

    private fun initView() {
        smf = supportFragmentManager.findFragmentById(R.id.locationMap) as SupportMapFragment?
        client = LocationServices.getFusedLocationProviderClient(this)
        Dexter.withContext(applicationContext)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : com.karumi.dexter.listener.single.PermissionListener {
                override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                    getMyLocation()
                }
                override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).check()
    }


    private fun clickListeners() {
        binding.backArrow.setOnClickListener { onBackPressed() }
    }
    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val task = client?.lastLocation
        task?.addOnSuccessListener { location ->
            latLng = location?.latitude?.let { LatLng(it, location.longitude) }
            latLngCurrent = latLng
            showOnMap()
        }
    }

    private fun showOnMap() {
        //click on map it gave that place location to shift marker
        smf?.getMapAsync { googleMap ->
            googleMap.clear()
            val markerOptions =
                latLng?.let { MarkerOptions().position(it).title(resources.getString(R.string.current_location)) }
            if (markerOptions != null) {
                googleMap.addMarker(markerOptions)
            }
            latLng?.let { CameraUpdateFactory.newLatLngZoom(it, 15f) }
                ?.let { googleMap.animateCamera(it) }
            googleMap.setOnMapClickListener {
                latLng = it
                //                binding.etLatitude.setText(latLng?.latitude.toString())
                //                binding.etLongitude.setText(latLng?.longitude.toString())
                showOnMap()
            }
        }
    }
    fun moveMapCamera(pos: LatLng) {
        moveMapCamera(pos, zoom)
    }

    private fun moveMapCamera(pos: LatLng, zoom: Int) {
        Log.d(TAG, "moveCamera zoom: $zoom")
        Log.d(TAG, "moveCamera zoom.toFloat: ${zoom.toFloat()}")
        smf?.getMapAsync {
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pos, zoom.toFloat()
                )
            )
        }
        //map?.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, zoom.toFloat()))
    }

    override fun onResume() {
        super.onResume()
//        isAppOpenEnable=false

    }

}