package com.example.phonenumberlocator.ui.activities.callLocator

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telecom.TelecomManager
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canLoadAndShowAd
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.canShowAppOpen
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.databinding.ActivityPnlcontactsDetailedBinding
import com.example.phonenumberlocator.pnlAppCallModels.RecentCallsDetailModel
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.checkIsValidNumber
import com.example.phonenumberlocator.pnlExtensionFun.countryIso
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlExtensionFun.launchEditContactIntent
import com.example.phonenumberlocator.pnlExtensionFun.normalizePhoneNumber
import com.example.phonenumberlocator.pnlModel.PNLMyContact
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.example.phonenumberlocator.pnlUtil.PNLDataStoreDb
import com.example.tracklocation.tlHelper.PNLMyContactsHelper
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import javax.inject.Inject


@AndroidEntryPoint
class PNLContactsDetailedActivity : PNLBaseClass<ActivityPnlcontactsDetailedBinding>() {

    override fun getViewBinding(): ActivityPnlcontactsDetailedBinding =
        ActivityPnlcontactsDetailedBinding.inflate(layoutInflater)
    private var countryName: String? = null
    private val CALL_PERMISSION_REQUEST_CODE = 1

    @Inject
    lateinit var dataStoreDb: PNLDataStoreDb

    @Inject
    lateinit var checkInternetConnection: PNLCheckInternetConnection

    private var userContact: PNLMyContact? = null
    private var numberData: RecentCallsDetailModel? = null


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (isNetworkAvailable() && canLoadAndShowAd){
            showSimpleInterstitialAdWithTimeAndCounter()
        }



        showAd()

        initViews()
        val myContactsHelper = PNLMyContactsHelper(this)
        numberData?.let { recentDetailModel ->
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("fetchContact", "numberData:recentDetailModel= $recentDetailModel")
                var formatted = PhoneNumberUtils.formatNumber(recentDetailModel.phoneNumber)
                if (formatted == null) {
                    formatted = recentDetailModel.phoneNumber
                }
                myContactsHelper.getAvailableContacts(false) { contactsList ->
                    contactsList.forEach { contact ->
                        Log.d("fetchContact", "contact: $contact")
                        contact.phoneNumbers.forEach {
                            val cfn = PhoneNumberUtils.formatNumber(it.normalizedNumber)
                            Log.d(
                                "fetchContact",
                                "contactNo=${it.normalizedNumber}  RecentFormatted= $formatted"
                            )
                            if (it.normalizedNumber == formatted || it.normalizedNumber == recentDetailModel.phoneNumber.normalizePhoneNumber() || cfn == formatted) {
                                userContact = contact
                                Log.d(
                                    "fetchContact",
                                    "userContact Exist= ${recentDetailModel.name}"
                                )
                                return@getAvailableContacts
                            }
                        }
                    }
                }
            }.invokeOnCompletion {
                CoroutineScope(Dispatchers.Main).launch {
                    //check favorite or not
                    userContact?.let { contact ->
                        myContactsHelper.getAvailableContacts(true) { myContacts ->
                        }
                    }
                }
            }

        }
        if (checkIsValidNumber(userContact?.phoneNumbers?.first()!!.normalizedNumber)) {
            Log.d("testing", "setupView: ${userContact?.phoneNumbers!!.first().normalizedNumber}")
            val swissNumberProto: Phonenumber.PhoneNumber = PhoneNumberUtil.getInstance().parse(
                userContact?.phoneNumbers!!.first().normalizedNumber,
                PhoneNumberLocator.context.countryIso.uppercase()
            )
            Log.d("testing", "setupView: $swissNumberProto")
            PhoneNumberLocator.instance.findCountryByDialCode("+${swissNumberProto.countryCode}") { countryModel ->
                binding.txtAddressDetail.text = countryModel.name
                Log.d(TAG, "onCreate3: ${countryModel.name}")
                countryName=countryModel.name
                Log.d(TAG, "onCreate2: ${countryModel.name}")

            }
        } else {
            binding.txtAddressDetail.text = " "
        }

        handleClicks()
    }

    private fun initViews() {
        if (intent.hasExtra("ContactDetail")) {
            numberData = intent.getParcelableExtra("ContactDetail")
            binding.titleContact.text = numberData?.name
            binding.textNameDetail.text = numberData?.name
            binding.numberDetail.text = numberData?.phoneNumber
//            binding.txtAddressDetail.text = String.format(getString(R.string.location), numberData?.name)
            binding.txtNetworkDetail.text =
                String.format(getString(R.string.network_name), numberData?.name)
        } else {
            if (intent.hasExtra("ContactModel")) {
                userContact = intent.getParcelableExtra("ContactModel")
                binding.titleContact.text = userContact?.name
                binding.textNameDetail.text = userContact?.name
                binding.numberDetail.text = userContact?.phoneNumbers?.first()?.normalizedNumber
//                binding.txtAddressDetail.text = String.format(getString(R.string.location), userContact?.name)
                binding.txtNetworkDetail.text =
                    String.format(getString(R.string.network_name), userContact?.name)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleClicks() {
        binding.backArrow.setOnClickListener { finish() }

        binding.editContact.setOnClickListener {
            canShowAppOpen=true
            userContact?.let {
                launchEditContactIntent(it)
            }
        }

        binding.makeCall.setOnClickListener {
            canShowAppOpen=true
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
                userContact?.phoneNumbers?.first()?.normalizedNumber?.let { it1 -> dialNumber(it1) }
            }
        }

        binding.blockContact.setOnClickListener {
            canShowAppOpen=true
            val telecomManager= getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            startActivity(telecomManager.createManageBlockedNumbersIntent(), null);
        }

        binding.sendMessage.setOnClickListener {
            canShowAppOpen=true
            userContact?.let {
                val phoneNumber = it.phoneNumbers.first().normalizedNumber
                if (isWhatsAppInstalled()) {
                    sendWhatsAppMessage(phoneNumber)
                } else {
                    toast("WhatsApp is not installed on this device.")
                }
            }
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

    override fun onResume() {
        canShowAppOpen=false
        super.onResume()
    }

    private fun showAd() {
        if (isNetworkAvailable() && canLoadAndShowAd) {
            binding.ads.beVisible()
            PhoneNumberLocator.instance.nativeAdLarge.observe(this) {
                showLoadedNativeAd(this, binding.ads, R.layout.native_large_2, it)
            }
        } else {
            binding.ads.beGone()
        }
    }

}