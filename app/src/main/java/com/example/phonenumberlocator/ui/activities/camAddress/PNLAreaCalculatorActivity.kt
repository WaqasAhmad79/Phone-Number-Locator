package com.example.phonenumberlocator.ui.activities.camAddress

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnlareaCalculatorBinding
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlHelper.CURRENT_UNIT
import com.example.phonenumberlocator.pnlHelper.UNIT_ACRE
import com.example.phonenumberlocator.pnlHelper.UNIT_BIGHA
import com.example.phonenumberlocator.pnlHelper.UNIT_CM
import com.example.phonenumberlocator.pnlHelper.UNIT_FT
import com.example.phonenumberlocator.pnlHelper.UNIT_KANAL
import com.example.phonenumberlocator.pnlHelper.UNIT_KILLA
import com.example.phonenumberlocator.pnlHelper.UNIT_KM
import com.example.phonenumberlocator.pnlHelper.UNIT_M
import com.example.phonenumberlocator.pnlHelper.UNIT_MARLA
import com.example.phonenumberlocator.pnlHelper.UNIT_MI
import com.example.phonenumberlocator.pnlHelper.UNIT_MURABBA
import com.example.phonenumberlocator.pnlInterfaces.MyLocationCallback
import com.example.phonenumberlocator.pnlUtil.PNLDataStoreDb
import com.example.phonenumberlocator.pnlUtil.PNLSphericalUnitsUtil
import com.example.phonenumberlocator.ui.pnlDialog.PNLSelectUnitDialog
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale
import java.util.Stack
import javax.inject.Inject


@AndroidEntryPoint
class PNLAreaCalculatorActivity : PNLBaseClass<ActivityPnlareaCalculatorBinding>(),
    OnMapReadyCallback {

    override fun getViewBinding(): ActivityPnlareaCalculatorBinding {
        return ActivityPnlareaCalculatorBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var dataStoreDb: PNLDataStoreDb


    internal enum class MeasureType {
        AREA
    }

    private val TAG1 = "TESTING1"

    var unit: String? = null

    private var area: Double = 0.0
    var perimeter: Double = 0.0
    private var myLocation: LatLng? = null
    var map: GoogleMap? = null
        private set
    private val trace = Stack<LatLng>()
    private val lines = Stack<Polyline>()
    private val points = Stack<Marker?>()
    private var areaOverlay: Polygon? = null
    private var distance = 0f
    private var zoom = 16
    private var type: MeasureType? = null
    private var client: FusedLocationProviderClient? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var lastMyLocationCallback: MyLocationCallback? = null
    private var is3DViewEnabled = false
    private val redoStack = Stack<Marker?>()
    private lateinit var file: File
    private var isScreenshotTaken = false
//    private var dialog: PNLResumeLoadingDialog?=null

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        try {
            dataStoreDb.getString(CURRENT_UNIT) {
                unit = it ?: UNIT_M
            }

            val tmp = savedInstanceState.getSerializable("trace") as List<*>?
            val it = tmp!!.iterator()
            while (it.hasNext()) {
                addMarkerPoint(it.next() as LatLng)
            }
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        savedInstanceState.getDouble("position-lat"),
                        savedInstanceState.getDouble("position-lon")
                    ),
                    savedInstanceState.getFloat("position-zoom")
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable("trace", trace)
        map?.let {
            outState.putDouble("position-lon", it.cameraPosition.target.longitude)
            outState.putDouble("position-lat", it.cameraPosition.target.latitude)
            outState.putFloat("position-zoom", it.cameraPosition.zoom)
        }
        super.onSaveInstanceState(outState)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       /* dialog = ResumeLoadingDialog(this)
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

        showBannerAdmob(binding.flBanner, this, getString(R.string.ad_mob_banner_id), null)
*/
        initViews()
        clickListeners()
    }

    @SuppressLint("StringFormatInvalid")
    private fun initViews() {
        formatter_no_dec.maximumFractionDigits = 0
        formatter_two_dec.maximumFractionDigits = 2
        client = LocationServices.getFusedLocationProviderClient(this)

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
            location?.latitude?.let { lat ->
                location.longitude.let { long ->
                    myLocation = LatLng(lat, long)
                    // Add a marker at the user's location
                    map?.addMarker(
                        MarkerOptions().position(myLocation!!)
                            .title(resources.getString(R.string.current_location))
                    )
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation!!, 15f))
                    myLocation?.let { moveMapCamera(it) }
                }
            }

        }

        dataStoreDb.getString(CURRENT_UNIT) {
            unit = it ?: UNIT_M
        }
        (supportFragmentManager.findFragmentById(R.id.areaMap) as SupportMapFragment)
            .getMapAsync(this)

        updateValueText()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("StringFormatInvalid")
    private fun clickListeners() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.cardUndo.setOnClickListener { removeLastPoint() }

        binding.cardRedo.setOnClickListener {
            redoPoint()
           /* val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.deletestr, trace.size))
            builder.setPositiveButton(
                R.string.yes
            ) { dialog, _ ->
                clearAllPoint()
                dialog.dismiss()
            }
            builder.setNegativeButton(
                R.string.no
            ) { dialog, _ -> dialog.dismiss() }
            builder.create().show()*/
        }

        binding.cardPlus.setOnClickListener {
            if (zoom < 19) zoom++
            myLocation?.let { it1 -> moveMapCamera(it1, zoom) }
        }

        binding.cardMinus.setOnClickListener {
            if (zoom > 3) zoom--
            myLocation?.let { it1 -> moveMapCamera(it1, zoom) }
        }

        /*
                binding.cardSave.setOnClickListener {
                    saveButtonAction()
                }*/

        binding.cardUnit.setOnClickListener {
            unit?.let { it1 ->
                PNLSelectUnitDialog(this, dataStoreDb, it1) {
                    dataStoreDb.getString(CURRENT_UNIT) {
                        Log.d(TAG1, "clickListeners: $it")
                        unit = it ?: UNIT_M
                        CoroutineScope(Dispatchers.IO).launch {
                            unit?.let { it1 -> calculateArea(it1) }
                        }.invokeOnCompletion {
                            runOnUiThread { updateValueText() }
                        }
                    }

                }
            }
        }

        binding.cardMyLocation.setOnClickListener {
            setCurrentLocation()
        }

        binding.cardMapSimple.setOnClickListener {
            onSwitch3DButtonClicked(it)
//            it.beGone()
//            binding.cardMapSatellite.beVisible()
//            map?.mapType = GoogleMap.MAP_TYPE_HYBRID
        }
//        binding.cardMapSatellite.setOnClickListener {
//            it.beGone()
//            binding.cardMapSimple.beVisible()
//            map?.mapType = GoogleMap.MAP_TYPE_NORMAL
//        }

    }
    private fun onSwitch3DButtonClicked(view: View) {
        // Check if the map is ready
        map?.let {
            // Toggle 3D view on/off
            is3DViewEnabled = !is3DViewEnabled

            // Get the current camera position
            val cameraPosition = it.cameraPosition

            // Create a new camera position with the updated tilt (for 3D effect)
            val newCameraPosition = CameraPosition.Builder(cameraPosition)
                .tilt(if (is3DViewEnabled) 60.0f else 0.0f)
                .build()

            // Animate the camera to the new position
            it.animateCamera(CameraUpdateFactory.newCameraPosition(newCameraPosition))
        }
    }

    /*
        private fun saveButtonAction() {
            captureMapScreenshot { file ->
                if (file != null) {
                    isScreenshotTaken = true
                    startSaveAreaInfoActivity(file)
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

            map?.snapshot(snapshotReadyCallback)
        }

        private fun startSaveAreaInfoActivity(file: File) {
            val intent = Intent(this, SaveAreaInfoActivity::class.java)
                .putExtra("area", binding.tvArea.text.toString())
                .putExtra("perimeter", binding.tvPerimeter.text.toString())
                .putExtra("screenshot_path", file.absolutePath)

            val markerList = ArrayList<LatLng>()
            val polylineList = ArrayList<List<LatLng>>()

            for (marker in points) {
                marker?.position?.let { position ->
                    markerList.add(position)
                }
            }

            for (line in lines) {
                val polyline = line.points.map { LatLng(it.latitude, it.longitude) }
                polylineList.add(polyline)
            }

            intent.putExtra("markerList", markerList)
            intent.putExtra("polylineList", polylineList)
            startActivity(intent)
        }
    */

    private fun removeLastPoint() {


        if (trace.isEmpty()) return
        val removedMarker = points.pop()
        val removedPoint = trace.pop()

        if (!trace.isEmpty()) {
            distance -= PNLSphericalUnitsUtil.computeDistanceBetween(removedPoint, trace.peek())
                .toFloat()
        }

        if (!lines.isEmpty()) lines.pop()?.remove()

        // Push the removed marker onto the redo stack
        redoStack.push(removedMarker)

        updateValueText()
    }

    // Create a function to redo a point
    private fun redoPoint() {
        if (redoStack.isNotEmpty()) {
            val redoMarker = redoStack.pop()

            if (redoMarker != null) {
                val redoPoint = redoMarker.position
                trace.push(redoPoint)
                points.push(redoMarker)
                updateValueText()

                // Recreate the line to the new point
                updatePolylinesWithMarkers()
                updatePolygon()
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission", "StringFormatInvalid", "ResourceAsColor")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        marker = BitmapDescriptorFactory.fromResource(R.drawable.adp)
        map?.mapType = GoogleMap.MAP_TYPE_NORMAL
        map?.setOnMarkerClickListener { click ->
            addMarkerPoint(click.position)
            true
        }
        map?.setOnMapClickListener { center -> addMarkerPoint(center)
        }
        if (Intent.ACTION_VIEW == intent.action) {
            try {
            } catch (e: IOException) {
                Toast.makeText(
                    this, getString(
                        R.string.error,
                        e.javaClass.simpleName + "\n" + e.message
                    ), Toast.LENGTH_LONG
                )
                    .show()
                e.printStackTrace()
            }
        } else {
            getCurrentLocation(object : MyLocationCallback {
                override fun gotAllLocation(location: Location?) {
                    if (location != null && map!!.cameraPosition.zoom <= 2) {
                        moveMapCamera(LatLng(location.latitude, location.longitude))
                    }
                }
            })
        }
        setCurrentLocation()
        map?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                // Handle marker drag start event
            }

            override fun onMarkerDrag(marker: Marker) {
                // Called repeatedly as the marker is being dragged
                // Update the polyline or perform any necessary operations based on the dragged marker's position
                updatePolylinesWithMarkers()
                updateValueText()
                updatePolygon()

            }

            override fun onMarkerDragEnd(marker: Marker) {
                // Handle marker drag end event
                updatePolylinesWithMarkers()
                updatePolygon()
            }
        })

    }

    fun updatePolylinesWithMarkers() {
        // Remove previous lines
        while (!lines.empty()) {
            lines.pop().remove()
        }
        while (!trace.empty()) {
            trace.clear()
        }

        // Add new lines based on marker positions
        if (points.size >= 2) {
            for (i in 0 until points.size - 1) {
                val startPoint = points[i]?.position
                trace.push(startPoint)
                val endPoint = points[i + 1]?.position
                trace.push(endPoint)
                val polylineOptions = PolylineOptions()
                    .add(startPoint, endPoint)
                    .color(COLOR_OF_LINE)
                    .width(MAP_LINE_WIDTH)
                val line = map?.addPolyline(polylineOptions)
                lines.push(line)
            }
        }



    }
    fun updatePolygon() {
        val polygonOptions = PolygonOptions()
            .fillColor(COLOR_OF_POINT)
            .strokeWidth(0f)
        val latLngList = points.map { it?.position }
        areaOverlay?.remove()
        areaOverlay = map?.addPolygon(polygonOptions.addAll(latLngList))
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun setCurrentLocation() {
        getCurrentLocation(object : MyLocationCallback {
            override fun gotAllLocation(location: Location?) {
                if (location != null) {
                    myLocation = LatLng(location.latitude, location.longitude)
                    // Add a marker at the user's location
                    map?.addMarker(MarkerOptions().position(myLocation!!).title("Current Location"))
                    map?.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation!!, 15f))
                    val distance: Double = PNLSphericalUnitsUtil.computeDistanceBetween(
                        myLocation!!,
                        map!!.cameraPosition.target
                    )
                    if (distance < 0.5) {
                        Toast.makeText(
                            this@PNLAreaCalculatorActivity, R.string.current_location,
                            Toast.LENGTH_SHORT
                        ).show()
                        //addPoint(myLocation!!)
                    } else {
                        moveMapCamera(myLocation!!)
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getCurrentLocation(callback: MyLocationCallback?) {
        if (hasLocationPermission()) {
            if (callback == null) return
            mGoogleApiClient = GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {
                        @SuppressLint("MissingPermission") val l = LocationServices.FusedLocationApi
                            .getLastLocation(mGoogleApiClient!!)
                        mGoogleApiClient?.disconnect()
                        callback.gotAllLocation(l)
                    }

                    override fun onConnectionSuspended(cause: Int) {
                    }
                }).build()
            mGoogleApiClient!!.connect()
        } else {
            lastMyLocationCallback = callback
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), REQUEST_LOCATION_PERMISSION
            )
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun addMarkerPoint(p: LatLng) {
        binding.cardAction.beVisible()
        if (!trace.isEmpty()) {
            lines.push(
                map!!.addPolyline(
                    PolylineOptions().color(COLOR_OF_LINE)
                        .width(MAP_LINE_WIDTH).add(trace.peek())
                        .add(p)
                )
            )
            distance += PNLSphericalUnitsUtil.computeDistanceBetween(p, trace.peek()).toFloat()
        }
        points.push(drawMapMarker(p))
        trace.push(p)
        updateValueText()
        //points and marker saved on add marker on the map
        /* val markerEntity = MarkerEntity(id=0,latitude = p.latitude, longitude = p.longitude )
             adpViewModel.insertMarker(markerEntity)

 //       timestamp =  System.currentTimeMillis())

         // Save polyline coordinates in the database
         val polylineEntity = PolylineEntity(id=0 , points = listOf(trace.peek(), p))
             adpViewModel.insertPolyline(polylineEntity)*/

    }

    private fun clearAllPoint() {
        map?.clear()
        trace.clear()
        lines.clear()
        points.clear()
        distance = 0f
        updateValueText()
    }

  /*  private fun removeLastPoint() {
        if (trace.isEmpty()) return
        points.pop()?.remove()
        val remove = trace.pop()
        if (!trace.isEmpty()) distance -= PNLSphericalUnitsUtil.computeDistanceBetween(
            remove,
            trace.peek()
        )
            .toFloat()
        if (!lines.isEmpty()) lines.pop().remove()
        updateValueText()
    }
*/
    fun moveMapCamera(pos: LatLng) {
        moveMapCamera(pos, zoom)
    }

    private fun moveMapCamera(pos: LatLng, zoom: Int) {
        Log.d(TAG, "moveCamera zoom: $zoom")
        Log.d(TAG, "moveCamera zoom.toFloat: ${zoom.toFloat()}")
        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                pos,
                zoom.toFloat()
            )
        )
    }

    private fun changeMapView(newView: Int) {

        /* getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putInt("mapView", newView)
             .apply()*/
    }

    fun drawMapMarker(center: LatLng): Marker? {
        return map?.addMarker(
            MarkerOptions().position(center).flat(true).anchor(0.5f, 0.5f)
                .icon(marker).draggable(true)
        )
    }

    private fun calculatePerimeter(): Double {
        var perimeter = 0.0
        if (trace.size >= 3) {
            for (i in 1 until trace.size) {
                perimeter += PNLSphericalUnitsUtil.computeDistanceBetween(trace[i - 1], trace[i])
            }
            perimeter += PNLSphericalUnitsUtil.computeDistanceBetween(trace[trace.size - 1], trace[0])
        }
        return perimeter
    }


    /* private fun calculatePerimeter(): Double {
         for (i in 0 until lines.size - 1) {
             val startPoint = lines[i].points[0]
             val endPoint = lines[i + 1].points[0]
             perimeter += SphericalUtil.computeDistanceBetween(startPoint, endPoint)
         }
         return perimeter
     }*/

    // Update the updateValueText() method
    fun updateValueText() {
        if (formattedString?.isNotEmpty() == true) {
            val areaString = "$formattedString"
            val perimeterString = "${formatter_two_dec.format(calculatePerimeter())}"
//            binding.tvArea.text = "$areaString\n$perimeterString"
            binding.tvArea.text = "$areaString "
//            binding.tvPerimeter.text = "$perimeterString m"

        } else {
            binding.tvArea.text = ""
//            binding.tvPerimeter.text = ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (map != null) {
            val lastPosition: CameraPosition = map!!.cameraPosition
            getSharedPreferences("settings", Context.MODE_PRIVATE).edit()
                .putString(
                    "lastLocation",
                    lastPosition.target.latitude.toString() + "#" + lastPosition.target.longitude +
                            "#" + lastPosition.zoom
                ).apply()
        }

    }

    private fun hasLocationPermission(): Boolean {
        return PermissionChecker
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED && PermissionChecker
            .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PermissionChecker.PERMISSION_GRANTED
    }

    private val formattedString: String?
        get() {
            type = MeasureType.AREA
            return if (type == MeasureType.AREA) {
                if (areaOverlay != null) areaOverlay!!.remove()
                if (trace.size >= 3) {
                    area = PNLSphericalUnitsUtil.computeArea(trace)
                    areaOverlay = map?.addPolygon(
                        PolygonOptions().addAll(trace).strokeWidth(0f)
                            .fillColor(COLOR_OF_POINT)
                    )
                } else {
                    area = 0.0
                }
                unit?.let { calculateArea(it) }
            } else {
                "not yet supported"
            }
        }


    private fun calculateArea(unit: String): String {
        Log.d(TAG1, "calculateArea: Area=$area")
        val areaWithUnit: String = when (unit) {

            UNIT_CM -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 0.000645)
                ) + " cm²"
            }

            UNIT_FT -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 0.092903)
                ) + " ft²"
            }

            UNIT_M -> {
                formatter_no_dec.format(
                    0.0.coerceAtLeast(area)
                ) + " m²"
            }

            UNIT_KM -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 1000000.0)
                ) + " km²"
            }

            UNIT_MI -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 2590002.5)
                ) + "mi²"
            }
            UNIT_MARLA -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 25.292900)
                ) + " marla"
            }
            UNIT_KANAL -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 505.857)
                ) + " kanal"
            }

            UNIT_BIGHA -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 2508.382)
                ) + " bigha"
            }

            UNIT_KILLA -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 4046.8564)
                ) + " killa"
            }

            UNIT_ACRE -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 4046.86)
                ) + " acre"
            }

            UNIT_MURABBA -> {
                formatter_two_dec.format(
                    0.0.coerceAtLeast(area / 101171.414000)
                ) + " murabba"
            }

            else -> {
                formatter_no_dec.format(
                    0.0.coerceAtLeast(area)
                ) + " m²"
            }
        }
        Log.d(TAG1, "calculateArea:$unit= $areaWithUnit")
        runOnUiThread { binding.tvArea.text = areaWithUnit }
        return areaWithUnit
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


    override fun onBackPressed() {
        super.onBackPressed()
    }

}