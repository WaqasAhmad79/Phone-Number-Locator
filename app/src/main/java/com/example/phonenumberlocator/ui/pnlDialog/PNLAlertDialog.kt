package com.example.phonenumberlocator.ui.pnlDialog


import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.phonenumberlocator.databinding.DialogAlertCustomBinding
import com.example.phonenumberlocator.pnlExtensionFun.getWindowWidth


@SuppressLint("MissingPermission", "SetTextI18n")
class PNLAlertDialog(val activity: Activity, title:String, textNo:String, textYes:String, msg: String, val callback: () -> Unit) {
    private var dialog: AlertDialog? = null
    private val binding =
        DialogAlertCustomBinding.inflate(LayoutInflater.from(activity), null, false)


    init {
        dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setCancelable(true)
            .create()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            activity.getWindowWidth(0.90f),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.show()
//        binding.ivImg.setImageResource(img)
        binding.tvMessage.text = msg
        binding.tvTitle.text = title
        binding.tvYes.text = textYes
        binding.tvNo.text = textNo
        binding.cvNo.setOnClickListener { dialog?.dismiss() }
        binding.cvYes.setOnClickListener {
            dialog?.dismiss()
            callback.invoke()
        }
    }
}
