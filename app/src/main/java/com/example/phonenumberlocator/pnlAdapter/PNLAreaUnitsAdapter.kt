package com.example.phonenumberlocator.pnlAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.phonenumberlocator.databinding.ItemViewUnitsBinding
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlHelper.CURRENT_UNIT
import com.example.phonenumberlocator.pnlUtil.PNLDataStoreDb
import javax.inject.Inject


class PNLAreaUnitsAdapter @Inject constructor(
    private val context: Context,
    private val dataStoreDb: PNLDataStoreDb
) :
    RecyclerView.Adapter<PNLAreaUnitsAdapter.ViewHolder>() {
    var onItemCListener: ((String) -> Unit)? = null
    private var list: ArrayList<String> = ArrayList(7)
    var appliedUnitName = ""


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewUnitsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(position)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    val TAG = "TESTING"

    inner class ViewHolder(private val binding: ItemViewUnitsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bindItems(position: Int) {
            with(binding) {
                binding.tvName.text=list[position]
                Log.d(TAG, "bindItems: ${list[position]}")
                if (appliedUnitName == list[position]) {
                    binding.ivChecked.beVisible()
                } else {
                    binding.ivChecked.beGone()
                }
//                ivBackground.setImageDrawable(context.getImage(list[position]))
                root.setOnClickListener {
                    dataStoreDb.saveString(CURRENT_UNIT, list[position])
                    appliedUnitName=list[position]
                    notifyDataSetChanged()
                    onItemCListener?.invoke(list[position])
                }
            }
        }
    }

    fun setOnItemClickListeners(listener: ((String) -> Unit)) {
        onItemCListener = listener
    }

    fun setData(unit: String,list2:ArrayList<String>){
        appliedUnitName=unit
        list=list2
        notifyDataSetChanged()
    }

}