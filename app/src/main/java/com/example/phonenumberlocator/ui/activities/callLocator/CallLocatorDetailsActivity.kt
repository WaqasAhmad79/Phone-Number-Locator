package com.example.phonenumberlocator.ui.activities.callLocator

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.telecom.TelecomManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canLoadAndShowAd
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.canShowAppOpen
import com.example.phonenumberlocator.admob_ads.interstitialAdPriority
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.admob_ads.showPriorityInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.databinding.ActivityCallLocatorDetailsBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.findUserLocation
import com.example.phonenumberlocator.pnlExtensionFun.hideKeyboard
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlExtensionFun.launchSendSMSIntent
import com.example.phonenumberlocator.pnlExtensionFun.toast
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.example.phonenumberlocator.pnlUtil.PNLDataStoreDb
import com.example.tracklocation.tlHelper.PNLMyContactsHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class CallLocatorDetailsActivity  : PNLBaseClass<ActivityCallLocatorDetailsBinding>() {

    private var markerOptions: MarkerOptions? = null
    private var smf: SupportMapFragment? = null
    private var client: FusedLocationProviderClient? = null
    private var contactLocation: LatLng? = null
    private var countryName: String? = null
    private var phoneNumber: String? = null
    var selectedCountryCodeInt: Int? = null
    private var counter = 1
    private val CALL_PERMISSION_REQUEST_CODE = 1

    @Inject
    lateinit var dataStoreDb: PNLDataStoreDb

    @Inject
    lateinit var myContactsHelper: PNLMyContactsHelper

    @Inject
    lateinit var checkInternetConnection: PNLCheckInternetConnection



    override fun getViewBinding(): ActivityCallLocatorDetailsBinding {
        return ActivityCallLocatorDetailsBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleAds()
        showAd()

        initViews()
        clickListeners()
    }
    private fun handleAds(){
        if (isNetworkAvailable()){
            showPriorityInterstitialAdWithTimeAndCounter(
                true,
                getString(R.string.admob_interistitial_search_high),
                getString(R.string.admob_interistitial_others_one), {
                    interstitialAdPriority = it
                })
        }
    }

    private fun initViews() {
        phoneNumber = intent.getStringExtra("Number")
        selectedCountryCodeInt = intent.getIntExtra("countryCode", 0)

        if (phoneNumber != null && selectedCountryCodeInt != 0) {
            binding.ccp.setCountryForPhoneCode(selectedCountryCodeInt!!)
            binding.etPhoneNumber.setText(phoneNumber)

            Looper.myLooper()?.let {
                Handler(it).postDelayed({
                    if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                        searchResult()
                    }
                    binding.progressBar.visibility = View.GONE
                }, 2000)
            }

        } else {
            finish()
        }

        Log.d(TAG, "initViews: $phoneNumber")
        binding.etPhoneNumber.setText(phoneNumber)
        smf = supportFragmentManager.findFragmentById(R.id.numberLocatorMap) as SupportMapFragment?
        client = LocationServices.getFusedLocationProviderClient(this)
        getMyLocation()
        binding.ccp.setDialogBackgroundColor(resources.getColor(R.color.app_color))
        binding.ccp.setDialogTextColor(Color.WHITE)
        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        Log.d(TAG, "initViews valid:${binding.ccp.isValidFullNumber} ")

        binding.ccp.setPhoneNumberValidityChangeListener {
            Log.d(TAG, "initViews valid 2:${binding.ccp.isValidFullNumber} ")
        }

        binding.etPhoneNumber.setOnEditorActionListener { v, actionId, event ->
            actionId == EditorInfo.IME_ACTION_SEARCH
        }

    }

    private fun clickListeners() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.makeCall.setOnClickListener {
            canShowAppOpen = true
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PERMISSION_REQUEST_CODE
                )
            } else {
                handleCLicks("Call")
            }
        }
        binding.sendMessage.setOnClickListener {
           canShowAppOpen = true
            handleCLicks("Message")
        }
        binding.editContact.setOnClickListener {
           canShowAppOpen = true
            handleCLicks("AddNumber")
        }
        binding.blockContact.setOnClickListener {
            canShowAppOpen = true
            val telecomManager = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            startActivity(telecomManager.createManageBlockedNumbersIntent(), null);
        }
    }
    private fun isWhatsAppInstalled(): Boolean {
        val packageManager = packageManager
        return try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    private fun sendWhatsAppMessage(phoneNumber: String) {
        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    private fun searchResult() {
        counter++
        binding.etPhoneNumber.hideKeyboard(this)
        if (binding.ccp.isValidFullNumber) {
//            binding.clear.beVisible()
//            binding.ivSearch.beGone()
            binding.viewSearchResult.beVisible()
            countryName = binding.ccp.selectedCountryName
            Log.d(TAG, "searchResult: $countryName")
            binding.numberDetail.text = binding.ccp.fullNumberWithPlus
            binding.txtAddressDetail.text = countryName
            if (isNetworkAvailable()) {
                Log.d(TAG, "searchResult: $countryName")
                if (countryName == "India") {

                    getIndianLocation() {
                        contactLocation = it
                        setOnMapResult()
                    }
                    Log.d(TAG, "searchResult:India $contactLocation ")
                } else {
                    findUserLocation(countryName) {
                        contactLocation = it
                        setOnMapResult()
                    }
                }

            } else {
                binding.viewSearchResult.beGone()
                toast(R.string.check_internet_connection)
            }

            Log.d(TAG, "initViews valid Number ")
        } else {
            toast(R.string.invalid_number)
        }
    }

    private fun setOnMapResult() {
        if (contactLocation != null) {
            try {
                smf?.getMapAsync { googleMap ->
                    markerOptions?.let {
                        googleMap.clear()
                    }
                }
                showResult()
            } catch (ee: Exception) {
            }
        }
    }

    private fun showResult() {
        binding.viewSearchResult.beVisible()
        showOnMap(5.5f)
    }

    private fun handleCLicks(action: String) {
        if (binding.ccp.isValidFullNumber) {
            val phoneNumber = binding.ccp.fullNumberWithPlus
            when (action) {
                "Call" -> {
//                    initCall(phoneNumber)
                    dialNumber(phoneNumber)
                }
                "Message" -> {
//                    launchSendSMSIntent(phoneNumber)
                    if (isWhatsAppInstalled()) {
                        sendWhatsAppMessage(phoneNumber)
                    } else {
                        toast(R.string.whatsapp_not_installed)
                    }
                }
                "AddNumber" -> {
                    sendToPhoneBook(phoneNumber)
                }
            }
        } else {
//            binding.ntJson.beVisible()
            toast(R.string.invalid_number)
        }
    }

    private fun initCall(number: String) {
        if (number.isNotEmpty()) {
            startCallIntent(number)
        }
    }

    private fun sendToPhoneBook(phoneNumber: String) {
        val data = ArrayList<ContentValues>()
        val row = ContentValues()
        row.put(
            ContactsContract.Contacts.Data.MIMETYPE,
            ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
        )
        data.add(row)
        val intent = Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI)
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, data)
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, phoneNumber)
        startActivity(intent)
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
        task?.addOnSuccessListener {
            it?.let { location ->
                contactLocation = LatLng(location.latitude, location.longitude)
                markerOptions =
                    contactLocation?.let {
                        MarkerOptions().position(it)
                            .title(resources.getString(R.string.current_location))
                    }
                showOnMap(15f)
            }
        }
    }

    private fun showOnMap(fl: Float) {
        smf?.getMapAsync { googleMap ->
            markerOptions =
                contactLocation?.let {
                    MarkerOptions().position(it)
                        .title(resources.getString(R.string.current_location))
                }
            markerOptions?.let {
                googleMap.addMarker(markerOptions!!)
            }
            contactLocation?.let {
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        it,
                        fl
                    )
                )
            }
        }
    }


    private fun getIndianLocation(callBack: (LatLng) -> Unit) {

        dataStoreDb.getInt("City") {
            when (it) {
                0 -> {
                    callBack.invoke(LatLng(26.8601722, 80.9221328))
                    dataStoreDb.saveInt("City", 1)
                }
                1 -> {
                    callBack.invoke(LatLng(25.593480, 85.127085))
                    dataStoreDb.saveInt("City", 2)
                }
                2 -> {
                    callBack.invoke(LatLng(23.229088, 77.410962))
                    dataStoreDb.saveInt("City", 3)
                }
                3 -> {
                    callBack.invoke(LatLng(17.391182, 78.466818))
                    dataStoreDb.saveInt("City", 4)
                }
                4 -> {
                    callBack.invoke(LatLng(23.260935, 77.412619))
                    dataStoreDb.saveInt("City", 5)
                }
                5 -> {
                    callBack.invoke(LatLng(26.449116, 80.334187))
                    dataStoreDb.saveInt("City", 0)
                }
            }
        }
    }

    private fun showAd() {
        if (isNetworkAvailable()) {
            binding.ads.beVisible()
            PhoneNumberLocator.instance.nativeAdLarge.observe(this) {
                showLoadedNativeAd(
                    this,
                    binding.ads,
                    R.layout.layout_admob_native_ad,
                    it
                )
            }
        } else {
            binding.ads.beGone()
        }
    }

    override fun onResume() {
        super.onResume()
        canShowAppOpen =false
    }

}