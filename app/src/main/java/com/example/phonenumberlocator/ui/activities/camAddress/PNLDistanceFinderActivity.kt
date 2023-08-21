package com.example.phonenumberlocator.ui.activities.camAddress

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnldistanceFinderBinding
import com.example.phonenumberlocator.pnlExtensionFun.beInvisible
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlHelper.UNIT_CM
import com.example.phonenumberlocator.pnlHelper.UNIT_KILOMETERS
import com.example.phonenumberlocator.pnlHelper.UNIT_METERS
import com.example.phonenumberlocator.pnlHelper.UNIT_MILES
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.io.File
import java.text.NumberFormat
import java.util.Locale
import java.util.Stack

class PNLDistanceFinderActivity : PNLBaseClass<ActivityPnldistanceFinderBinding>(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    override fun getViewBinding(): ActivityPnldistanceFinderBinding =ActivityPnldistanceFinderBinding.inflate(layoutInflater)

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    private var markers: ArrayList<Marker> = ArrayList()
    private var distances: ArrayList<Float> = ArrayList()
    private var polyline: Polyline? = null
    private val redoStack: Stack<Marker> = Stack()
    private lateinit var customMarker: BitmapDescriptor
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var REQUEST_LOCATION_PERMISSION=1
    private var currentLatLng: LatLng? = null
    private var zoom = 16

    private lateinit var file: File
    private var isScreenshotTaken = false
    private var is3DViewEnabled = false
    private var selectedDistanceUnits = "m" // Default units


//    private var dialog: PNLResumeLoadingDialog?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     /*   dialog = PNLResumeLoadingDialog(this)
        if (isTimeDifferenceGreaterThan30Seconds()) {
            if (isNetworkAvailable()) {
                dialog?.show()
                showHighMainAdmobInterstitial({
                    previousAdClosedTime = System.currentTimeMillis()
                }, {
                    showLowMainAdmobInterstitial({
                        previousAdClosedTime = System.currentTimeMillis()
                    }, { dialog?.dismiss() }, {
                        interstitialCounter =0
                        delayAdShown = true
                        Handler().postDelayed({
                            dialog?.dismiss()
                        }, 1000)
                    })
                }, {
                    interstitialCounter =0
                    delayAdShown = true
                    Handler().postDelayed({
                        dialog?.dismiss()
                    }, 1000)
                })

            }
        }

        showBannerAdmob(binding.flBanner,this,getString(R.string.ad_mob_banner_id),null)*/

        mapView = findViewById(R.id.distanceMap)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        customMarker = BitmapDescriptorFactory.fromResource(R.drawable.adp)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        clickListeners()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMapClickListener(this)
        getCurrentLocation()
        map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                // Handle marker drag start event
            }

            override fun onMarkerDrag(marker: Marker) {
                // Called repeatedly as the marker is being dragged
                // Update the polyline or perform any necessary operations based on the dragged marker's position
                updateDistancesAndPolyline()


            }

            override fun onMarkerDragEnd(marker: Marker) {
                // Handle marker drag end event
                updateDistancesAndPolyline()

            }
        })

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, get the current location
            getCurrentLocation()
        } else {
            // Permission denied, show a message to the user
            Toast.makeText(
                this,
                "Location permission denied",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    // Create a LatLng object from the location coordinates
                    currentLatLng = LatLng(location.latitude, location.longitude)

                    // Add a marker to the map at the current location
                    map.addMarker(
                        MarkerOptions()
                            .position(currentLatLng!!)
                            .title(getString(R.string.current_location))

                    )
                    // animate the camera to the current location
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng!!, 15f))
                } else {
                    Toast.makeText(
                        this,
                        "Location not available",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    override fun onMapClick(point: LatLng) {
        binding.clDistanceResult.beVisible()
        binding.cardAction.beVisible()
        map.addMarker(MarkerOptions().position(point).icon(customMarker).draggable(true))?.let { markers.add(it) }
//        map.setOnMapLongClickListener { latLng ->
//            showCustomPopup(latLng.latitude,latLng.longitude)
//
//        }
        updateDistancesAndPolyline()
    }
/** update distance without units**/
   /* private fun updateDistancesAndPolyline() {
        // Calculate the distances between all pairs of consecutive markers
        distances.clear()
        var totalDistance = 0f
        for (i in 0 until markers.size - 1) {
            val start = markers[i].position
            val end = markers[i + 1].position
            val distance = calculateDistance(start, end)
            totalDistance += distance
            distances.add(distance)
        }

        // Update the UI with the distances
//        val distanceText = resources.getString(R.string.total_distance)+ " " + ":" +" "+ totalDistance.toInt() + " " + "m"
        val distanceText =""+ totalDistance.toInt() + " " + "m"
        binding.tvDistance.text = distanceText

        // Draw a polyline between all pairs of consecutive markers
        if (markers.size >= 2) {
            val polylineOptions = PolylineOptions()
            polylineOptions.color(COLOR_OF_LINE)
            polylineOptions.width(MAP_LINE_WIDTH)
            for (i in 0 until markers.size) {
                polylineOptions.add(markers[i].position)
            }
            if (polyline != null) {
                polyline!!.remove()
            }
            polyline = map.addPolyline(polylineOptions)
        } else {
            if (polyline != null) {
                polyline!!.remove()
                polyline = null
            }
        }
    }*/


    /** update distance with units**/
   private fun updateDistancesAndPolyline() {
       // Calculate the distances between all pairs of consecutive markers
       distances.clear()
       var totalDistance = 0f
       for (i in 0 until markers.size - 1) {
           val start = markers[i].position
           val end = markers[i + 1].position
           val distance = calculateDistance(start, end)
           totalDistance += distance
           distances.add(distance)
       }

        // Calculate the distances based on the selected units
        val distancesInSelectedUnits = distances.map { distance ->
            when (selectedDistanceUnits) {
                UNIT_METERS -> distance // Already in meters
                UNIT_KILOMETERS -> distance / 1000 // Convert to kilometers
//                UNIT_MILES -> distance / 1609.344 // Convert to miles
                else -> distance // Default to meters
            }
        }
        // Calculate the total distance in the selected units
        val totalDistanceInSelectedUnits = distancesInSelectedUnits.sum()

        // Update the UI with the distances in the selected units
        val totalDistanceText = "${formatter_two_dec.format(totalDistanceInSelectedUnits)} $selectedDistanceUnits"
        binding.tvDistance.text = totalDistanceText

       // Draw a polyline between all pairs of consecutive markers
       if (markers.size >= 2) {
           val polylineOptions = PolylineOptions()
           polylineOptions.color(COLOR_OF_LINE)
           polylineOptions.width(MAP_LINE_WIDTH)
           for (i in 0 until markers.size) {
               polylineOptions.add(markers[i].position)
           }
           if (polyline != null) {
               polyline!!.remove()
           }
           polyline = map.addPolyline(polylineOptions)
       } else {
           if (polyline != null) {
               polyline!!.remove()
               polyline = null
           }
       }
   }

    private fun deleteAll(){
        markers.forEach { it.remove() }
        markers.clear()
        polyline?.remove()
        polyline = null
        binding.tvDistance.text="0.0"
//        binding.clDistanceResult.beGone()
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            start.latitude,
            start.longitude,
            end.latitude,
            end.longitude,
            results
        )
        return results[0]
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    /*  private fun showCustomPopup(marker: Marker) {
          val popupView = layoutInflater.inflate(R.layout.lat_long_layout, null)
          val latitudeTextView = popupView.findViewById<TextView>(R.id.lat)
          val longitudeTextView = popupView.findViewById<TextView>(R.id.lang)

          // Set latitude and longitude values based on the marker
          val latitude = marker.position.latitude
          val longitude = marker.position.longitude
          latitudeTextView.text = "Latitude: $latitude"
          longitudeTextView.text = "Longitude: $longitude"

          val popupWindow = PopupWindow(
              popupView, WindowManager.LayoutParams.WRAP_CONTENT,
              WindowManager.LayoutParams.WRAP_CONTENT, true
          )
          // Set background drawable for the popup window
          popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
          // Set animation style for the popup window
          popupWindow.animationStyle = R.style.PopupAnimation

          // Calculate the gravity for the popup window based on the marker's position
          val offsetX = resources.getDimensionPixelOffset(R.dimen.popup_offset_x)
          val offsetY = resources.getDimensionPixelOffset(R.dimen.popup_offset_y)
          val markerPoint = marker.position
          val screenPosition = map.projection.toScreenLocation(markerPoint)
          val x = screenPosition.x - offsetX
          val y = screenPosition.y - offsetY

          // Show the popup window near the marker's position
          popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, x, y)
      }*/


    /*private fun showCustomPopup(latitude: Double, longitude: Double) {
        val popupView = layoutInflater.inflate(R.layout.lat_long_layout, null)
        val latitudeTextView = popupView.findViewById<TextView>(R.id.lat)
        val longitudeTextView = popupView.findViewById<TextView>(R.id.lang)
        latitudeTextView.text = "Latitude: $latitude"
        longitudeTextView.text = "Longitude: $longitude"

        val popupWindow = PopupWindow(
            popupView, ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, true
        )
        // Show the popup window at the click point
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 400)
    }*/
    private fun clickListeners() {
        binding.cardMap3d.setOnClickListener {
            it.beInvisible()
            binding.cardMap2d.beVisible()
            onSwitch3DButtonClicked(it)
        }
        binding.cardMap2d.setOnClickListener {
            it.beInvisible()
            binding.cardMap3d.beVisible()
            onSwitch3DButtonClicked(it)
        }
        binding.cardMyLocation.setOnClickListener {
            getCurrentLocation()
        }
        binding.backArrow.setOnClickListener { onBackPressed() }

        binding.cardPlus.setOnClickListener {
            if (zoom < 19) zoom++
            currentLatLng?.let { it1 -> moveMapCamera(it1, zoom) }
        }
        binding.cardMinus.setOnClickListener {
            if (zoom > 3) zoom--
            currentLatLng?.let { it1 -> moveMapCamera(it1, zoom) }
        }
//        binding.cardUndo.setOnClickListener {
//            if (markers.isNotEmpty()) {
//                markers.removeLast().remove() // Remove the last added marker from the map
//                updateDistancesAndPolyline() // Update the distances and polyline
//            }
//        }
        binding.cardUndo.setOnClickListener {
            if (markers.isNotEmpty()) {
                val removedMarker = markers.removeLast() // Remove the last added marker from the list
                removedMarker.remove() // Remove the marker from the map
                redoStack.push(removedMarker) // Push the removed marker to redoStack
                updateDistancesAndPolyline() // Update the distances and polyline
            }
        }
        binding.cardRedo.setOnClickListener {
            redoMarker()
        }
        binding.cardUnit.setOnClickListener {
            val units = arrayOf("Meters", "Kilometers")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Select Units")
            builder.setItems(units) { _, which ->
                selectedDistanceUnits = when (which) {
                    0 -> UNIT_METERS
                    1 -> UNIT_KILOMETERS
//                    2 -> UNIT_MILES
                    else -> UNIT_METERS // Default to meters
                }
                updateDistancesAndPolyline()
            }
            builder.show()
        }

       /* binding.cardDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.deletestr))
            builder.setPositiveButton(
                R.string.yes
            ) { dialog, _ ->
                deleteAll()
                dialog.dismiss()
            }
            builder.setNegativeButton(
                R.string.no
            ) { dialog, _ -> dialog.dismiss() }
            builder.create().show()

        }*/

//        binding.cardSave.setOnClickListener {
//            saveButtonAction()
//        }

    }
    private fun redoMarker() {
        if (redoStack.isNotEmpty()) {
            val redoMarker = redoStack.pop()
            redoMarker?.let {
                val addedMarker = map.addMarker(MarkerOptions().position(it.position).icon(customMarker).draggable(true))
                if (addedMarker != null) {
                    markers.add(addedMarker)
                } // Add the marker to the markers list
                updateDistancesAndPolyline()
            }
        }
    }
    private fun onSwitch3DButtonClicked(view: View) {
        // Check if the map is ready
        mapView.getMapAsync { googleMap ->
            // Toggle 3D view on/off
            is3DViewEnabled = !is3DViewEnabled

            // Get the current camera position
            val cameraPosition = googleMap.cameraPosition

            // Create a new camera position with the updated tilt (for 3D effect)
            val newCameraPosition = CameraPosition.Builder(cameraPosition)
                .tilt(if (is3DViewEnabled) 60.0f else 0.0f)
                .build()

            // Animate the camera to the new position
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))
        }
    }
    /*   private fun saveButtonAction() {
           captureMapScreenshot { file ->
               if (file != null) {
                   isScreenshotTaken = true
                   startSaveDistanceInfoActivity(file)
               } else {
                   // Handle the case when the file is null or not initialized yet
               }
           }
       }
       private fun captureMapScreenshot(callback: (file: File?) -> Unit) {
           val snapshotReadyCallback = GoogleMap.SnapshotReadyCallback { bitmap ->
               val file = File(getExternalFilesDir(null), "${System.currentTimeMillis()}_map_screenshot.png")
               Log.d(TAG, "captureMapScreenshot: $file")
               try {
                   val outputStream = FileOutputStream(file)
                   bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                   outputStream.flush()
                   outputStream.close()
                   callback(file)
               } catch (e: Exception) {
                   e.printStackTrace()
                   callback(null)
               }
           }

           map.snapshot(snapshotReadyCallback)
       }
       private fun startSaveDistanceInfoActivity(file: File) {
           val intent = Intent(this, SaveDistanceInfoActivity::class.java)
               .putExtra("distance", binding.distance.text.toString())
               .putExtra("screenshot_path", file.absolutePath)

           val markerList = ArrayList<LatLng>()
           val polylineList = ArrayList<List<LatLng>>()

           for (marker in markers) {
               marker.position?.let { position ->
                   markerList.add(position)
               }
           }
           polyline?.points?.let { linePoints ->
               val polylinePoints = linePoints.map { LatLng(it.latitude, it.longitude) }
               polylineList.add(polylinePoints)
           }

           intent.putExtra("markerList", markerList)
           intent.putExtra("polylineList", polylineList)
           startActivity(intent)
       }

   */
    private fun moveMapCamera(pos: LatLng, zoom: Int) {
        Log.d(TAG, "moveCamera zoom: $zoom")
        Log.d(TAG, "moveCamera zoom.toFloat: ${zoom.toFloat()}")
        mapView.getMapAsync {
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pos, zoom.toFloat()
                )
            )
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
    companion object {
        var marker: BitmapDescriptor? = null
        val COLOR_OF_LINE = Color.argb(128, 0, 255, 0)
        val COLOR_OF_POINT = Color.argb(128, 255, 0, 0)
        const val MAP_LINE_WIDTH = 5f

        @SuppressLint("ConstantLocale")
        val formatter_two_dec: NumberFormat = NumberFormat.getInstance(Locale.getDefault())

        @SuppressLint("ConstantLocale")
        private val formatter_no_dec = NumberFormat.getInstance(Locale.getDefault())
        private const val REQUEST_LOCATION_PERMISSION = 0
    }

}