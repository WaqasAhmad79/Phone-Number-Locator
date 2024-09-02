package com.example.phonenumberlocator.pnlExtensionFun

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.BlockedNumberContract
import android.provider.Settings
import android.telecom.TelecomManager
import android.telephony.PhoneNumberUtils
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.phonenumberlocator.BaseConfigPNL
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.pnlHelper.DARK_GREY
import com.example.phonenumberlocator.pnlHelper.PERMISSION_ACCESS_FINE_LOCATION
import com.example.phonenumberlocator.pnlHelper.PERMISSION_CALL_PHONE
import com.example.phonenumberlocator.pnlHelper.PERMISSION_GET_ACCOUNTS
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_CALENDAR
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_CALL_LOG
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_CONTACTS
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_PHONE_STATE
import com.example.phonenumberlocator.pnlHelper.PERMISSION_READ_STORAGE
import com.example.phonenumberlocator.pnlHelper.PERMISSION_WRITE_CALENDAR
import com.example.phonenumberlocator.pnlHelper.PERMISSION_WRITE_CALL_LOG
import com.example.phonenumberlocator.pnlHelper.PERMISSION_WRITE_CONTACTS
import com.example.phonenumberlocator.pnlHelper.PERMISSION_WRITE_STORAGE
import com.example.phonenumberlocator.pnlHelper.PREFS_KEY
import com.example.phonenumberlocator.pnlHelper.TIME_FORMAT_12
import com.example.phonenumberlocator.pnlHelper.TIME_FORMAT_24
import com.example.phonenumberlocator.pnlHelper.UNIT_ACRE
import com.example.phonenumberlocator.pnlHelper.UNIT_BIGHA
import com.example.phonenumberlocator.pnlHelper.UNIT_CM
import com.example.phonenumberlocator.pnlHelper.UNIT_FT
import com.example.phonenumberlocator.pnlHelper.UNIT_KANAL
import com.example.phonenumberlocator.pnlHelper.UNIT_KILLA
import com.example.phonenumberlocator.pnlHelper.UNIT_KM
import com.example.phonenumberlocator.pnlHelper.UNIT_M
import com.example.phonenumberlocator.pnlHelper.UNIT_MARLA
import com.example.phonenumberlocator.pnlHelper.UNIT_MI
import com.example.phonenumberlocator.pnlHelper.UNIT_MURABBA
import com.google.android.gms.maps.model.LatLng
import java.util.*
import java.util.regex.Pattern


//SharePref
fun Context.getSharedPrefs(): SharedPreferences =
    getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

//
val Context.telecomManager: TelecomManager get() = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
val Context.baseConfig: BaseConfigPNL get() = BaseConfigPNL.newInstance(this)

/*fun Context.getClocksDatabase() = ClocksDatabase.getInstance(this)
val Context.clocksDB: UserAddedClocksDao get() = getClocksDatabase().UserAddedClocksDao()*/


fun Context.isBlackAndWhiteTheme() =
    baseConfig.textColor == Color.WHITE && baseConfig.primaryColor == Color.BLACK && baseConfig.backgroundColor == Color.BLACK

fun Context.isWhiteTheme() =
    baseConfig.textColor == DARK_GREY && baseConfig.primaryColor == Color.WHITE && baseConfig.backgroundColor == Color.WHITE

fun Context.launchActivityIntent(intent: Intent) {
    try {
        Log.d("TAG", "launchActivityIntent: $intent")
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(R.string.no_app_found)
    } catch (e: Exception) {
        showErrorToast(e)
    }
}

fun Context.toast(str: Int) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun Context.toast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun Context.getAdjustedPrimaryColor() = when {
    isWhiteTheme() || isBlackAndWhiteTheme() -> baseConfig.accentColor
    else -> baseConfig.primaryColor
}

val Context.config: ConfigPNL get() = ConfigPNL.newInstance(applicationContext)


fun Context.queryCursor(
    uri: Uri,
    projection: Array<String>,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null,
    showErrors: Boolean = false,
    callback: (cursor: Cursor) -> Unit
) {
    try {
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    callback(cursor)
                } while (cursor.moveToNext())
            }
        }
    } catch (e: Exception) {
        if (showErrors) {
            showErrorToast(e)
        }
    }
}

fun Context.showErrorToast(exception: Exception, length: Int = Toast.LENGTH_LONG) {
    showErrorToast(exception, length)
}

fun Context.hasPermission(permId: Int) = ContextCompat.checkSelfPermission(
    this, getPermissionString(permId)
) == PackageManager.PERMISSION_GRANTED

fun getPermissionString(id: Int) = when (id) {
   /* PERMISSION_READ_STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE*/
/*    PERMISSION_WRITE_STORAGE -> Manifest.permission.WRITE_EXTERNAL_STORAGE*/
    PERMISSION_READ_CONTACTS -> Manifest.permission.READ_CONTACTS
    PERMISSION_WRITE_CONTACTS -> Manifest.permission.WRITE_CONTACTS
    PERMISSION_READ_CALENDAR -> Manifest.permission.READ_CALENDAR
    PERMISSION_WRITE_CALENDAR -> Manifest.permission.WRITE_CALENDAR
    PERMISSION_CALL_PHONE -> Manifest.permission.CALL_PHONE
    PERMISSION_READ_CALL_LOG -> Manifest.permission.READ_CALL_LOG
    PERMISSION_WRITE_CALL_LOG -> Manifest.permission.WRITE_CALL_LOG
    PERMISSION_GET_ACCOUNTS -> Manifest.permission.GET_ACCOUNTS
    PERMISSION_READ_PHONE_STATE -> Manifest.permission.READ_PHONE_STATE
    PERMISSION_ACCESS_FINE_LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
    else -> ""
}





@TargetApi(Build.VERSION_CODES.N)
fun Context.addBlockedNumber(number: String) {
    ContentValues().apply {
        put(BlockedNumberContract.BlockedNumbers.COLUMN_ORIGINAL_NUMBER, number)
        put(
            BlockedNumberContract.BlockedNumbers.COLUMN_E164_NUMBER,
            PhoneNumberUtils.normalizeNumber(number)
        )
        try {
            contentResolver.insert(BlockedNumberContract.BlockedNumbers.CONTENT_URI, this)
            toast(R.string.number_blocked)
        } catch (e: Exception) {
            showErrorToast(e)
        }
    }
}

fun Context.findUserLocation(strAddress: String?, callBack: ((LatLng) -> Unit)?) {

    val coder = Geocoder(this)
    val address: List<Address>?
    var p1: LatLng? = null
    try {
        address = strAddress?.let { coder.getFromLocationName(it, 5) }
        if (address == null) {

        }
        val location = address?.get(0)
        p1 = location?.latitude?.let { LatLng(it, location.longitude) }
    } catch (e: Exception) {
        Log.e("TAG", "findLocation: ${e.message}")
    }
    if (p1 != null) {
        callBack?.invoke(p1)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun Context.getImage(name: String?): Drawable? {
    return this.resources.getDrawable(
        this.resources.getIdentifier(
            name, "drawable", this.packageName
        )
    )
}

fun Context.copyText(str: String) {
    val clipboard: ClipboardManager? = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("label", str)
    clipboard?.setPrimaryClip(clip)
}

fun Context.navigation(latLng: LatLng, add: LatLng) {
    val uri =
        "http://maps.google.com/maps?saddr=" + "${latLng.latitude},${latLng.longitude}" + "&daddr=" + "${add.latitude},${add.longitude}"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
    this.startActivity(intent)
}


fun Context.getWindowWidth(percent: Float = 1.0f): Int {
    val displayMetrics = resources.displayMetrics
    return (displayMetrics.widthPixels * percent).toInt()
}

fun getUnits(): ArrayList<String> {
    val list: ArrayList<String> = ArrayList()
    list.add(UNIT_CM)
    list.add(UNIT_FT)
    list.add(UNIT_M)
    list.add(UNIT_KM)
    list.add(UNIT_MI)
    list.add(UNIT_MARLA)
    list.add(UNIT_KANAL)
    list.add(UNIT_BIGHA)
    list.add(UNIT_KILLA)
    list.add(UNIT_ACRE)
    list.add(UNIT_MURABBA)
    return list
}

/*fun Context.getAddressFromLatLong(latitude: Double, longitude: Double, callBack: (() -> Unit)? = null): String? {
    var addresses: List<Address>?
    val geocoder = Geocoder(this, Locale.getDefault())
    try {
        addresses = geocoder.getFromLocation(
            latitude, longitude, 1
        )

    } catch (e: Exception) {
        callBack?.invoke()
        return "Please Check internet Connection"
    }


    val address: String = if (addresses?.isNotEmpty() == true) {
        addresses[0].getAddressLine(0)
    } else {
        getString(R.string.no_address_found)
    }
    callBack?.invoke()
    return address
}*/

fun Context.getAddressFromLatLong(latitude: Double, longitude: Double, callBack: (() -> Unit)? = null): String? {
    var addresses: List<Address>?
    val geocoder = Geocoder(this, Locale.getDefault())
    try {
        addresses = geocoder.getFromLocation(
            latitude, longitude, 1
        )

    } catch (e: Exception) {
        callBack?.invoke()
        return "Please Check internet Connection"
    }


    val address: String = if (addresses?.isNotEmpty() == true) {
        addresses[0].getAddressLine(0)
    } else {
        getString(R.string.no_address_found)
    }
    callBack?.invoke()
    return address
}


fun View.hideKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}



fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


/*var languageList: List<CountryData> = ArrayList()
fun Activity.getDetailLanguageCategory(): List<CountryData> {
    val inputStream = assets.open("country_codes.json")
    val text = inputStream.bufferedReader().use(BufferedReader::readText)
    return Gson().fromJson(text, Array<CountryData>::class.java).toList()
}

fun getLanguageName(list: String): String {
    var nameCountry = ""
    if (languageList.isNotEmpty()) {
        loop@ for (i in languageList) {
            if (i.CODE2.equals(list, ignoreCase = true)) {
                nameCountry = i.NAME.toString()
                break@loop
            }
        }
    }
    return nameCountry
}*/

fun Activity.gpsStatusCheck(callBack: ((Boolean) -> Unit)?) {
    var status = true
    val manager = getSystemService(LOCATION_SERVICE) as LocationManager?
    if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        buildAlertMessageNoGps()
        status = false
    }
    callBack?.invoke(status)
}

fun Activity.buildAlertMessageNoGps() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
        .setCancelable(false).setPositiveButton("Yes",
            DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
    val alert: AlertDialog = builder.create()
    alert.show()
}

val Context.hasNotificationListenerGranted: Boolean
    get() = run {
        val contentResolver = this.contentResolver
        val enabledNotificationListeners =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val packageName = this.packageName
        return !(enabledNotificationListeners == null || !enabledNotificationListeners.contains(
            packageName
        ))
    }



/*
@SuppressLint("UseCompatLoadingForDrawables")
fun Context.getProfileTextColor(image: Int) = when (image) {
    R.drawable.ic_contact_bg_1 -> resources.getColor(R.color.contact_bg_1)
    R.drawable.ic_contact_bg_2 -> resources.getColor(R.color.contact_bg_2)
    R.drawable.ic_contact_bg_3 -> resources.getColor(R.color.contact_bg_3)
    R.drawable.ic_contact_bg_4 -> resources.getColor(R.color.contact_bg_4)
    R.drawable.ic_contact_bg_5 -> resources.getColor(R.color.contact_bg_5)
    else -> resources.getColor(R.color.contact_bg_5)
}*/

val Context.countryIso: String
    get() = run {
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkCountryIso
    }

fun checkIsValidNumber(incomingNmbr: String): Boolean {
    if (incomingNmbr.startsWith("+")) return true
    if (!Pattern.matches("[a-zA-Z]+", incomingNmbr.trim())) {
        // your operations
        Log.d("checkIsValidNumber", "InComingNmbr: $incomingNmbr")
        return true
    }
    return false
}

fun Context.getTimeFormat() = if (baseConfig.use24HourFormat) TIME_FORMAT_24 else TIME_FORMAT_12

//val Context.sdCardPath: String get() = baseConfig.sdCardPath
//val Context.internalStoragePath: String get() = baseConfig.internalStoragePath
val Context.otgPath: String get() = baseConfig.OTGPath

fun Context.copyToClipboard(text: String) {
    val clip = ClipData.newPlainText(getString(R.string.simple_commons), text)
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
//    val toastText = getString(R.string.copied_to_clipboard)
    toast(R.string.copied_to_clipboard)

}