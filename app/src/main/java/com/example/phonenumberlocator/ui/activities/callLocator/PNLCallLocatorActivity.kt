package com.example.phonenumberlocator.ui.activities.callLocator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdSmall
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.*
import com.example.phonenumberlocator.databinding.ActivityPnlcallLocatorBinding
import com.example.phonenumberlocator.pnlExtensionFun.*
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.example.phonenumberlocator.pnlUtil.PNLDataStoreDb
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PNLCallLocatorActivity : PNLBaseClass<ActivityPnlcallLocatorBinding>() {

    private var countryName: String? = null
    private var phoneNumber: String? = null
    private var counter = 1

    @Inject
    lateinit var dataStoreDb: PNLDataStoreDb

    @Inject
    lateinit var checkInternetConnection: PNLCheckInternetConnection

    override fun getViewBinding(): ActivityPnlcallLocatorBinding {
        return ActivityPnlcallLocatorBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        handleAds() // for interstitial
        initViews()
        clickListeners()
        hideNavBar()
        showAd() // for native ads

    }

    private fun handleAds() {

        if (RemoteConfigClass.inter_pnl_call_locator_activity
            && isNetworkAvailable()
            && PhoneNumberLocator.canRequestAd) {
            showSimpleInterstitialAdWithTimeAndCounter()
        }

    }

    private fun initViews() {
        phoneNumber = intent.getStringExtra("number")
        Log.d(TAG, "initViews: $phoneNumber")
        binding.etPhoneNumber.setText(phoneNumber)
        binding.ccp.setDialogBackgroundColor(resources.getColor(R.color.app_color));
        binding.ccp.setDialogTextColor(resources.getColor(R.color.text_color));
        binding.ccp.registerCarrierNumberEditText(binding.etPhoneNumber)
        Log.d(TAG, "initViews valid:${binding.ccp.isValidFullNumber} ")
        binding.ccp.setPhoneNumberValidityChangeListener {}
        binding.etPhoneNumber.setOnEditorActionListener { v, actionId, event ->
            actionId == EditorInfo.IME_ACTION_SEARCH
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SuspiciousIndentation")
    private fun clickListeners() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.etPhoneNumber.onTextChangeListener {
            if (it.trim().isEmpty()) {
//                binding.viewSearchResult.visibility = View.GONE

            }
        }
        binding.findLocation.setOnClickListener { view ->
            searchResult()
        }

        binding.etPhoneNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchResult()
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun searchResult() {
        counter++
        binding.etPhoneNumber.hideKeyboard(this)
        if (binding.ccp.isValidFullNumber) {
            countryName = binding.ccp.selectedCountryName
            if (isNetworkAvailable()) {
                binding.progressBar.visibility = View.VISIBLE
                binding.findLocation.visibility = View.INVISIBLE

                Looper.myLooper()?.let {
                    Handler(it).postDelayed({
                        if (ProcessLifecycleOwner.get().lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                            val intent = Intent(this, CallLocatorDetailsActivity::class.java)
                            intent.putExtra("Number", binding.etPhoneNumber.text.toString())
                            intent.putExtra("countryCode", binding.ccp.selectedCountryCodeAsInt)
                            startActivity(intent)
                        }
                        binding.progressBar.visibility = View.GONE
                        binding.findLocation.visibility = View.VISIBLE
                    }, 2000)
                }


            } else {
                toast(R.string.check_internet_connection)
            }

            Log.d(TAG, "initViews valid Number ")
        } else {
            toast(R.string.invalid_number)
        }
    }

    private fun showAd() { // for native ads
        if (RemoteConfigClass.native_pnl_call_locator_activity) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.beVisible()
                nativeAdSmall.observe(this) {
                    showLoadedNativeAd(
                        this,
                        binding.ads,
                        binding.includeShimmer.shimmerContainerNative,
                        R.layout.layout_admob_native_ad_withou_tmedia,
                        it
                    )
                }
            }
        } else {
            binding.ads.beGone()
        }
    }

    override fun onResume() {
        super.onResume()
        isAppOpenEnable = false
    }

}