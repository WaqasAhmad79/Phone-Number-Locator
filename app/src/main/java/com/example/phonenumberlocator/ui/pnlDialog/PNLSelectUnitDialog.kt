package com.example.phonenumberlocator.ui.pnlDialog


import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phonenumberlocator.databinding.DialogUnitsBinding
import com.example.phonenumberlocator.pnlAdapter.PNLAreaUnitsAdapter
import com.example.phonenumberlocator.pnlExtensionFun.getUnits
import com.example.phonenumberlocator.pnlExtensionFun.getWindowWidth
import com.example.phonenumberlocator.pnlExtensionFun.setupDialogStuff
import com.example.phonenumberlocator.pnlUtil.PNLDataStoreDb


@SuppressLint("MissingPermission", "SetTextI18n")
class PNLSelectUnitDialog(val activity: Activity, dataStoreDb: PNLDataStoreDb, unit:String,
                          val callback: (String) -> Unit) {
    var adapter: PNLAreaUnitsAdapter = PNLAreaUnitsAdapter(activity,dataStoreDb)
    private var dialog: AlertDialog? = null
    private val binding= DialogUnitsBinding.inflate(LayoutInflater.from(activity),null,false)

    init {
        binding.recyclerView.layoutManager=LinearLayoutManager(activity)
        binding.recyclerView.adapter=adapter
        dialog = AlertDialog.Builder(activity)
            .setOnCancelListener {}
            .create().apply {
                activity.setupDialogStuff(binding.root, this)
            }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(activity.getWindowWidth(0.93f), ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.show()
        adapter.setData(unit, getUnits())
        adapter.setOnItemClickListeners {
            callback.invoke(it)
            dialog?.dismiss()
        }
    }
}
