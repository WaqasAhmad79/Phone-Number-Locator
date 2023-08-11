package com.example.phonenumberlocator.ui.pnlDialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.phonenumberlocator.databinding.DialogLoadingBinding
import com.example.phonenumberlocator.pnlExtensionFun.getWindowWidth


class PNLLoadingDialog(val activity: Context) {
    private var dialog: AlertDialog? = null
    private val binding =
        DialogLoadingBinding.inflate(LayoutInflater.from(activity), null, false)
    init {
        dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setCancelable(true)
            .create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            activity.getWindowWidth(0.50f),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    fun showDialog(){
        dialog?.show()
    }
    fun hideDialog(){
        dialog?.dismiss()
    }

}
