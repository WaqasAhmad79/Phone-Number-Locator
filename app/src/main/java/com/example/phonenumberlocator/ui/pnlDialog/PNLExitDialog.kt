package com.example.phonenumberlocator.ui.pnlDialog

import android.app.Activity
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.phonenumberlocator.databinding.ExitCustomDialogBinding


class PNLExitDialog(val activity: Activity, val callback: () -> Unit) {
    private var dialog= AlertDialog.Builder(activity)
    private val binding= ExitCustomDialogBinding.inflate(LayoutInflater.from(activity),null,false)
    init {
        dialog.setView(binding.root)
        val alertDialog: AlertDialog = dialog.create()
      /*if (nativeAd!=null){
          binding.adsNative.visibility= View.VISIBLE
          showLoadedNativeAdWithoutMedia(activity,binding.flNativeAdExit, R.layout.layout_native_without_media,nativeAd)
      }*/
        alertDialog.show()

        binding.tvYes.setOnClickListener{
            callback.invoke()
            alertDialog.dismiss()
        }
        binding.tvNo.setOnClickListener{
            alertDialog.dismiss()
        }
    }
}
