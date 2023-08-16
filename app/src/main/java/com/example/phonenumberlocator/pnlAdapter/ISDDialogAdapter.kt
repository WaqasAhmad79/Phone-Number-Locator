package com.example.phonenumberlocator.pnlAdapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.pnlExtensionFun.highlightTextFromNumbers
import com.example.phonenumberlocator.pnlExtensionFun.highlightTextPart
import com.example.phonenumberlocator.pnlModel.PNLAreaCountriesModel


class ISDDialogAdapter (val context: Context) : RecyclerView.Adapter<ISDDialogAdapter.CountriesViewHolder>() {

    lateinit var activity: Activity
    private var countries: ArrayList<PNLAreaCountriesModel> = arrayListOf()
    private var itemClick: ((PNLAreaCountriesModel, Int) -> Unit)? = null

    var textToHighlight = ""

    fun textChanged(text: String) {
        val texts = text.removePrefix("+")
        textToHighlight = texts.removePrefix("*")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_countries_dialog, parent, false)
        return CountriesViewHolder(view)
    }

    fun setData(activity: Activity, itemList: ArrayList<PNLAreaCountriesModel>) {
        this.activity = activity
        countries = itemList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(callBackNew: ((PNLAreaCountriesModel, Int) -> Unit)) {
        itemClick = callBackNew
    }


    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val country = countries[position]
        Log.d("searchtest", "onViewCreated: onBindViewHolder:${countries.size}")
        holder.countryCode.text=country.phonecode
        holder.countryName.text=country.name
        holder.countryName.apply {
            text = if (country.name?.contains(textToHighlight, true) == true) { country.name!!.highlightTextPart(textToHighlight, context.resources.getColor(R.color.blue))
            } else {
                country.name?.highlightTextFromNumbers(textToHighlight, context.resources.getColor(R.color.blue))
            }
        }
        holder.itemView.setOnClickListener {
            itemClick?.invoke(country, position)
        }
    }

    override fun getItemCount(): Int = countries.size


    class CountriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var item: LinearLayout = itemView.findViewById(R.id.item_country_frame)
//        var searchBar: EditText = itemView.findViewById(R.id.search_bar)
        var countryName: TextView = itemView.findViewById(R.id.country_name)
        var countryCode: TextView = itemView.findViewById(R.id.code)

    }


}