package com.example.phonenumberlocator.pnlAdapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.pnlExtensionFun.highlightTextFromNumbers
import com.example.phonenumberlocator.pnlExtensionFun.highlightTextPart
import com.example.phonenumberlocator.pnlModel.PNLAreaCodeModel


class PNLIsdStdAdapter(var context: Context): RecyclerView.Adapter<PNLIsdStdAdapter.CodeViewModel>() {
    var code_list:ArrayList<PNLAreaCodeModel> = arrayListOf()

    var textToHighlight = ""

    fun textChanged(text: String) {
        val texts = text.removePrefix("+")
        textToHighlight = texts.removePrefix("*")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CodeViewModel {
        return CodeViewModel(LayoutInflater.from(context).inflate(R.layout.item_std_codes,parent,false))
    }
    fun setData(activity: Activity, itemList: ArrayList<PNLAreaCodeModel>) {
        this.context = activity
        code_list = itemList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CodeViewModel, position: Int) {
        val citiesList = code_list[position]
        holder.countryName.text = citiesList.city
        holder.countryName.apply {
            text = if (citiesList.city?.contains(textToHighlight, true) == true) { citiesList.city!!.highlightTextPart(textToHighlight, context.resources.getColor(R.color.blue))
            } else {
                citiesList.city?.highlightTextFromNumbers(textToHighlight, context.resources.getColor(R.color.blue))
            }
        }
        holder.code.text = citiesList.areaCode

    }

    override fun getItemCount(): Int {
      return code_list.size
    }
    fun updateData( code_list2:ArrayList<PNLAreaCodeModel>){
        code_list=code_list2
        notifyDataSetChanged()

    }

    inner class CodeViewModel(itemView: View):RecyclerView.ViewHolder(itemView){
        var countryName: TextView
        var code: TextView


        init {
            countryName = itemView.findViewById(R.id.country_name)
            code = itemView.findViewById(R.id.code)


        }
    }
}