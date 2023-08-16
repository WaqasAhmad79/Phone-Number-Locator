package com.example.phonenumberlocator.ui.pnlDialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonenumberlocator.databinding.ActivityIsdDataDialogBinding
import com.example.phonenumberlocator.pnlAdapter.ISDDialogAdapter
import com.example.phonenumberlocator.pnlExtensionFun.normalizeString
import com.example.phonenumberlocator.pnlExtensionFun.onTextChangeListener
import com.example.phonenumberlocator.pnlModel.PNLAreaCountriesModel


class PNLCountriesDataDialog(
    val activity: Activity,
    val countriesAdapter: ISDDialogAdapter,
    val countries: ArrayList<PNLAreaCountriesModel>,
    val callback: (PNLAreaCountriesModel) -> Unit
) {
    private var dialog: AlertDialog? = null
    private var filtered = ArrayList<PNLAreaCountriesModel>()
    private val binding = ActivityIsdDataDialogBinding.inflate(LayoutInflater.from(activity), null, false)

    init {
        dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setCancelable(true)
            .create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.show()

        binding.countriesList.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = countriesAdapter
        }
        binding.searchBar.onTextChangeListener { text ->
            countriesAdapter.textChanged(text)
            if (text.isEmpty()) {
                countriesAdapter.setData(activity, countries)
            } else {
                searchForContact(text) {
                    countriesAdapter.setData(activity, filtered)
                }
            }
        }

        countriesAdapter.setData(activity, countries)

        countriesAdapter.setOnItemClickListener { countries, pos ->
            dialog?.dismiss()
            Log.d("TAG", "handleClicks: dismiss$callback")
            callback.invoke(countries)
        }

    }
    private fun searchForContact(text: String, callback: () -> Unit) {
        if (text.trim().isNotEmpty()) {
            filtered.clear()
            val searchQuery = text.trim().lowercase()
            Log.d("searchtest", "onViewCreated: query word:$searchQuery")
            Log.d("searchtest", "onViewCreated: all contacts:${countries.size}")
            if (countries.isNotEmpty()) {
                countries.forEach { country ->
                    val convertedName = (country.name!!.normalizeString())
                    if (country.name?.lowercase()?.contains(searchQuery) == true) {
                        filtered.add(country)
                    } else {
                        country.phonecode?.forEach { code ->
                            if (code.lowercase().contains(searchQuery) || convertedName.contains(text, true)
                            ) {
                                filtered.add(country)
                            }
                        }
                    }
                }
                callback.invoke()
            }
        }

    }

}
