package com.example.phonenumberlocator.ui.pnlDialog

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.databinding.ExitCustomDialogBinding
import com.google.android.gms.ads.nativead.NativeAd

class PNLExitDialog(
    val activity: Activity,
    val callback: () -> Unit,
    private val nativeAd: NativeAd?
) {
    private var dialog = AlertDialog.Builder(activity)
    private val binding =
        ExitCustomDialogBinding.inflate(LayoutInflater.from(activity), null, false)

    init {
        dialog.setView(binding.root)
        val alertDialog: AlertDialog = dialog.create()
        if (nativeAd != null && RemoteConfigClass.native_exit_ad) {
            binding.flNativeAdExit.visibility = View.VISIBLE
            showLoadedNativeAd(
                activity,
                binding.flNativeAdExit,
                binding.includeShimmer.shimmerContainerNative,
                R.layout.layout_admob_native_ad_withou_tmedia,
                nativeAd,
            )
        }
        alertDialog.show()

        binding.tvYes.setOnClickListener {
            callback.invoke()
            alertDialog.dismiss()
        }
        binding.tvNo.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}
