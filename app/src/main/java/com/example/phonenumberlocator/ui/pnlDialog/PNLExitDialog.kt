package com.example.phonenumberlocator.ui.pnlDialog

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.native_ad.NativeAdConfig
import com.example.phonenumberlocator.admob_ads.native_ad.NativeAdHelper
import com.example.phonenumberlocator.databinding.ExitCustomDialogBinding
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable

class PNLExitDialog(
    val activity: Activity,
    val callback: () -> Unit
) {
    private var dialog = AlertDialog.Builder(activity)
    private val binding =
        ExitCustomDialogBinding.inflate(LayoutInflater.from(activity), null, false)

    init {
        dialog.setView(binding.root)
        val alertDialog: AlertDialog = dialog.create()

        //-------native ad starts
        if (RemoteConfigClass.native_exit_ad && PhoneNumberLocator.canRequestAd && activity.isNetworkAvailable()) {
            binding.flNativeAdExit.visibility = View.VISIBLE

            val config = NativeAdConfig(
                activity.resources.getString(R.string.admob_native_small),
                canShowAds = true,
                canReloadAds = true,
                layoutId = R.layout.native_ad_06
            )

            // Pass the LifecycleOwner (activity) to the NativeAdHelper
            val nativeAdHelper =
                NativeAdHelper(activity, activity as LifecycleOwner, config).apply {
                    TAG = "PNLExitDialog"
                    shimmerLayoutView = binding.includeShimmer.shimmerContainerNative
                    nativeContentView = binding.flNativeAdExit
                }
            nativeAdHelper.loadAndShowNativeAd()
        }
        //-------native ad ends

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
