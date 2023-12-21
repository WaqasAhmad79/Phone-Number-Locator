package com.example.phonenumberlocator.ui.activities.callLocator

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.CorrectionInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canLoadAndShowAd
import com.example.phonenumberlocator.admob_ads.showSimpleInterstitialAdWithTimeAndCounter
import com.example.phonenumberlocator.pnlAdapter.ISDDialogAdapter
import com.example.phonenumberlocator.pnlAdapter.PNLIsdStdAdapter
import com.example.phonenumberlocator.databinding.ActivityPnlisdStdBinding
import com.example.phonenumberlocator.pnlDatabases.TinyDB
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlExtensionFun.normalizeString
import com.example.phonenumberlocator.pnlExtensionFun.onTextChangeListener
import com.example.phonenumberlocator.pnlHelper.SELECTED_COUNTRY
import com.example.phonenumberlocator.pnlModel.PNLAreaCodeModel
import com.example.phonenumberlocator.ui.pnlDialog.PNLCountriesDataDialog
import com.example.phonenumberlocator.pnlModel.PNLAreaCountriesModel
import com.example.tracklocation.tlUtil.readToObjectList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PNLIsdStdActivity  : AppCompatActivity() {

    private lateinit var binding: ActivityPnlisdStdBinding
    var code_list: ArrayList<PNLAreaCodeModel>? = null
    var country_list: ArrayList<PNLAreaCountriesModel>? = null
    private var filtered = ArrayList<PNLAreaCodeModel>()
    var areaCodesList: ArrayList<PNLAreaCodeModel> = arrayListOf()

    @Inject
    lateinit var adapter: PNLIsdStdAdapter
    lateinit var coountriesAdapter: ISDDialogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnlisdStdBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isNetworkAvailable() && canLoadAndShowAd){
            showSimpleInterstitialAdWithTimeAndCounter()
        }

    }

    private fun initViews() {
        binding.rvAreaCode.layoutManager = LinearLayoutManager(this)
        binding.rvAreaCode.adapter = adapter

        updateAdapter()

    }

    private fun handleClicks() {
        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        binding.selectCountry.setOnClickListener {
            coountriesAdapter = ISDDialogAdapter(this)
            Log.d("TAG", "showPopup: ${country_list?.size}")
            country_list?.let { it1 ->
                PNLCountriesDataDialog(this, coountriesAdapter, it1) {
//                    Log.d("TAG", "handleClicks1: dismiss$it")
                    TinyDB.getInstance(this).putInt(SELECTED_COUNTRY, it.id!!)
//                    Log.d("TAG", "init2: $id")
                    binding.iso2.text = it.iso2
                    binding.phoneCode.text = it.phonecode
                    updateCities(it.id!!)

                }
            }
        }
        binding.searchText.onTextChangeListener { text ->
            adapter.textChanged(text)
            if (text.isEmpty()) {
                adapter.setData(this, code_list!!)
            } else {
                searchForContact(text) {
                    adapter.setData(this, filtered)
                }
            }
        }

    }

    private fun updateAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            code_list = getCities(this@PNLIsdStdActivity)
            country_list = getCountries(this@PNLIsdStdActivity)
        }.invokeOnCompletion {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("TAG", "updateAdapter: ${country_list!!.size}")
                Log.d("TAG", "updateAdapter: ${code_list!!.size}")
                if (code_list?.isNotEmpty() == true) {
                    binding.progressBar.beGone()
                    adapter.updateData(code_list!!)
                }
            }
            val id = TinyDB.getInstance(this).getIntArea(SELECTED_COUNTRY)
            Log.d("TAG", "init: $id")
            for (i in country_list!!) {
                if (i.id == id) {
                    binding.iso2.text = i.iso2
                    binding.phoneCode.text = i.phonecode
                }
            }
            updateCities(id)
        }
    }

    private fun searchForContact(text: String, callback: () -> Unit) {
        if (text.trim().isNotEmpty()) {
            filtered.clear()
            val searchQuery = text.trim().lowercase()
            Log.d("searchtest", "onViewCreated: query word:$searchQuery")
            Log.d("searchtest", "onViewCreated: all contacts:${areaCodesList.size}")
            if (areaCodesList.isNotEmpty()) {
                areaCodesList.forEach { city ->
                    val convertedName = (city.city!!.normalizeString())
                    if (city.city?.lowercase()?.contains(searchQuery) == true) {
                        filtered.add(city)
                    } else {
                        city.areaCode?.forEach { code ->
                            if (code.lowercase().contains(searchQuery) || convertedName.contains(text, true)
                            ) {
                                filtered.add(city)
                            }
                        }
                    }
                }
                callback.invoke()
            }
        }

    }

    private fun getCountries(context: Context): java.util.ArrayList<PNLAreaCountriesModel> {
        return "countries.json".readToObjectList(
            context,
            PNLAreaCountriesModel::class.java
        ) as java.util.ArrayList<PNLAreaCountriesModel>
    }

    fun getCities(context: Context): java.util.ArrayList<PNLAreaCodeModel> {
        return "city_dial_code.json".readToObjectList(
            context,
            PNLAreaCodeModel::class.java
        ) as java.util.ArrayList<PNLAreaCodeModel>
    }

    private fun updateCities(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("arfa", "handleClicks1:${areaCodesList.size}")
            areaCodesList = arrayListOf()
            Log.d("arfa", "handleClicks2:${areaCodesList.size}")
            for (i in code_list!!) {
                if (i.countryId == id.toString()) {
                    areaCodesList.add(i)
                }
            }

        }.invokeOnCompletion {
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("arfa", "handleClicks3:${areaCodesList.size}")

                if (areaCodesList.isNotEmpty()) {
                    adapter.updateData(areaCodesList)
                    binding.rvAreaCode.beVisible()
                } else {
                    binding.rvAreaCode.beGone()
                }
            }
        }
    }

    override fun onResume() {
        initViews()
        handleClicks()
        super.onResume()

    }
}