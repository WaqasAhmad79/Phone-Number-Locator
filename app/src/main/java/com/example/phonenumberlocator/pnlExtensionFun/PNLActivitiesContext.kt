package com.example.phonenumberlocator.pnlExtensionFun

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.example.phonenumberlocator.R
import com.example.tracklocation.tlHelper.PNLMyContactsHelper
import com.example.phonenumberlocator.pnlHelper.ensureBackgroundThread
import com.example.phonenumberlocator.pnlModel.PNLMyContact
import com.google.android.gms.maps.model.LatLng
import com.permissionx.guolindev.PermissionX


fun Activity.launchSendSMSIntent(recipient: String) {
    Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.fromParts("smsto", recipient, null)
        launchActivityIntent(this)
    }
}


fun Activity.launchEditContactIntent(contact: PNLMyContact) {
    ensureBackgroundThread {
        val lookupKey = PNLMyContactsHelper(this).getContactLookupKey((contact).rawId.toString())
        val publicUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey)
        runOnUiThread {
            Intent(Intent.ACTION_EDIT).apply {
                setDataAndType(publicUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE)
                startActivity(this)
            }
        }
    }

}

fun Activity.setupDialogStuff(view: View, dialog: AlertDialog, titleId: Int = 0, titleText: String = "", cancelOnTouchOutside: Boolean = true, callback: (() -> Unit)? = null) {
    if (isDestroyed || isFinishing) {
        return
    }
    val adjustedPrimaryColor = getAdjustedPrimaryColor()


    var title: TextView? = null
    if (titleId != 0 || titleText.isNotEmpty()) {
        title = layoutInflater.inflate(R.layout.dialog_title, null) as TextView
    }

    dialog.apply {
        setView(view)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCustomTitle(title)
        setCanceledOnTouchOutside(cancelOnTouchOutside)
        show()
        getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(adjustedPrimaryColor)
        getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(adjustedPrimaryColor)
        getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(adjustedPrimaryColor)

       /* val bgDrawable = resources.getColoredDrawableWithColor(R.drawable.bg_call_default)
        window?.setBackgroundDrawable(bgDrawable)*/
    }
    callback?.invoke()
}

fun Activity.shareCurrentLocation(location: LatLng?, name: String?) {
    val uri =
        "https://www.google.com/maps/?location=${location!!.latitude} ${location.longitude} $name"
    val sharingSub = "Here is my location"
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, sharingSub)
    sharingIntent.putExtra(Intent.EXTRA_TEXT, uri)
    startActivity(Intent.createChooser(sharingIntent, "Share via"))
}

fun Resources.getColoredDrawableWithColor(drawableId: Int, alpha: Int = 255): Drawable {
    val drawable = getDrawable(drawableId)
    drawable.mutate().alpha = alpha
    return drawable
}


fun FragmentActivity.checkAndRequestPermissions(onPermissionStatus: ((Boolean) -> Unit)? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        PermissionX.init(this)
            .permissions(
                ACCESS_FINE_LOCATION
            )
            .onExplainRequestReason { scope, deniedList ->
            }
            .onForwardToSettings { scope, deniedList ->
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    onPermissionStatus?.invoke(true)
                } else {
                    onPermissionStatus?.invoke(false)
                }
            }

    } else {
        PermissionX.init(this)
            .permissions(
                ACCESS_FINE_LOCATION
            )
            .onExplainRequestReason { scope, deniedList ->
            }
            .onForwardToSettings { scope, deniedList ->
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    onPermissionStatus?.invoke(true)
                } else {
                    onPermissionStatus?.invoke(false)
                }
            }
    }

}
