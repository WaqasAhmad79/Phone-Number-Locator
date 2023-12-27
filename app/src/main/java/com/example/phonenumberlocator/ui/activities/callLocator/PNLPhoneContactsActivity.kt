package com.example.phonenumberlocator.ui.activities.callLocator

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonenumberlocator.PNLBaseClass
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.databinding.ActivityPnlphoneContactsBinding
import com.example.phonenumberlocator.pnlAdapter.PNLPhoneContactsAdapter
import com.example.phonenumberlocator.pnlAppCallModels.RecentCallsDetailModel
import com.example.phonenumberlocator.pnlExtensionFun.beVisibleIf
import com.example.phonenumberlocator.pnlExtensionFun.countryIso
import com.example.phonenumberlocator.pnlExtensionFun.normalizeString
import com.example.phonenumberlocator.pnlExtensionFun.onTextChangeListener
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_CONTACTS
import com.example.phonenumberlocator.pnlModel.PNLMyContact
import com.example.phonenumberlocator.pnlOverloads.pnlViewModel.AppViewModel
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PNLPhoneContactsActivity : PNLBaseClass<ActivityPnlphoneContactsBinding>() {

    override fun getViewBinding(): ActivityPnlphoneContactsBinding =
        ActivityPnlphoneContactsBinding.inflate(layoutInflater)


    private var allContacts = ArrayList<PNLMyContact>()
    private var filtered = ArrayList<PNLMyContact>()
    private var advanceRecentList: ArrayList<RecentCallsDetailModel> = ArrayList()
    private val sharedViewModel: AppViewModel by viewModels()
    private var mDCC = ""


    @Inject
    lateinit var contactsAdapter: PNLPhoneContactsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

            showSimpleInterstitialAdWithTimeAndCounter()

        initViews()
        handleClicks()
    }

    override fun onResume() {
        super.onResume()
        handlePermission(PERMISSION_READ_CONTACTS) {
            sharedViewModel.getContacts()
        }

    }

    private fun initViews() {
        binding.rvContactList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvContactList.adapter = contactsAdapter
        sharedViewModel.advancedRecentList.observe(this) {
            advanceRecentList = it
            Log.d(TAG, "initViews: ${sharedViewModel.advancedRecentList}")
        }
        sharedViewModel.contactsList.observe(this) {
            allContacts = it
            Log.d("gssssss", "setData: ${it.size}")
            Log.d("gssssss", "setData: ${allContacts.size}")
            allContacts.sort()
            setupUI(allContacts)
        }
        /*TrackerAppClass.instance?.let {
            it.getDefaultCountry { country ->
                mDCC = country.code
            }
        }*/
    }

    private fun handleClicks() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.etSearch.onTextChangeListener { text ->
            contactsAdapter.textChanged(text)
            if (text.isEmpty()) {
                contactsAdapter.setData(this, allContacts)
            } else {
                searchForContact(text) {
                    contactsAdapter.setData(this, filtered)
                }
            }
        }
        contactsAdapter.setOnMoreClickListener { contact, position ->
            val myContact = contact as PNLMyContact
            var data: RecentCallsDetailModel? = null
            Log.d("contactFrag", "viewMore clicked contact: ${myContact.phoneNumbers}")
            CoroutineScope(Dispatchers.IO).launch {
                for (recentDetailModel in advanceRecentList) {
                    Log.d("contactFrag", "recent list recentDetailModel= $recentDetailModel")
                    val swissNumberProto: Phonenumber.PhoneNumber = PhoneNumberUtil.getInstance()
                        .parse(recentDetailModel.phoneNumber, countryIso?.toUpperCase())
                    val formatted = PhoneNumberUtil.getInstance()
                        .format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164)
                    myContact.phoneNumbers.forEach {
                        Log.d("contactFrag", "contact nmber=${it.normalizedNumber}  recentFormatted= $formatted")
                        val cfn = PhoneNumberUtils.formatNumber(it.normalizedNumber)
                        if (it.normalizedNumber == recentDetailModel.phoneNumber || it.normalizedNumber == formatted || cfn == formatted) {
                            data = recentDetailModel
                            Log.d("contactFrag", "number=number ${recentDetailModel.name}")
                            return@launch
                        }

                    }
                }
            }.invokeOnCompletion {
                if (data != null) {
                    startActivity(Intent(this, PNLContactsDetailedActivity::class.java).putExtra("ContactDetail", data))
                } else {
                    startActivity(Intent(this, PNLContactsDetailedActivity::class.java).putExtra("ContactModel", myContact))
                }
            }

        }

        contactsAdapter.setOnItemClickListener { contact, pos ->
            val myContact = contact as PNLMyContact
            dialNumber(myContact.phoneNumbers.first().normalizedNumber)
        }

    }

    private fun searchForContact(text: String, callback: () -> Unit) {
        if (text.trim().isNotEmpty()) {
            filtered.clear()
            val searchQuery = text.trim().lowercase()
            Log.d("searchtest", "onViewCreated: query word:$searchQuery")
            Log.d("searchtest", "onViewCreated: all contacts:${allContacts.size}")
            if (allContacts.isNotEmpty()) {
                allContacts.forEach { myContact ->
                    var convertedName =
                        PhoneNumberUtils.convertKeypadLettersToDigits(myContact.name.normalizeString())
                    if (myContact.name.lowercase().contains(searchQuery)) {
                        filtered.add(myContact)
                    } else {
                        myContact.phoneNumbers.forEach { phoneNumber ->
                            if (phoneNumber.normalizedNumber.lowercase()
                                    .contains(searchQuery) || convertedName.contains(text, true)
                            ) {
                                filtered.add(myContact)
                            }
                        }
                    }
                }
                callback.invoke()
            }
        }

    }

    private fun setupUI(contacts: ArrayList<PNLMyContact>) {
        val hasContacts = contacts.isNotEmpty()
        binding.rvContactList.beVisibleIf(hasContacts)

        /*        binding.noContactsPlaceholder.beVisibleIf(!hasContacts)
                binding.imvEmptyContacts.beVisibleIf(!hasContacts)
                binding.noContactsPlaceholder2.beVisibleIf(!hasContacts && !hasPermission(
                    PERMISSION_READ_CONTACTS)!!)
                if (!hasContacts) {
                    val placeholderText =
                        if (activity?.hasPermission(PERMISSION_READ_CONTACTS)!!) R.string.no_contacts_found else R.string.no_access_to_contacts
                    binding.noContactsPlaceholder.text = getString(placeholderText)
                }*/

        contactsAdapter.setData(this, contacts)

    }


}