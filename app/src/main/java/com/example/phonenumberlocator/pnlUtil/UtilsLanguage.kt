package com.example.phonenumberlocator.pnlUtil

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.util.Log
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.util.*
import kotlin.math.roundToInt

class UtilsLang {

    companion object {
        @JvmStatic
        fun convertDpToPixel(i: Int): Any {
            val metrics = Resources.getSystem().displayMetrics
            val px = i * (metrics.densityDpi / 160f)
            return px.roundToInt().toFloat()
        }
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE

@SuppressLint("Range")
infix fun Cursor.get(columnName: String): String? {
    return getString(getColumnIndex(columnName))
}

suspend fun launchMain(block: suspend CoroutineScope.() -> Unit) =
    withContext(Dispatchers.Main, block)

fun Context.setLocale() {
//    val preferences: SharedPreferences = getSharedPreferences(
//        "Settings",
//        AppCompatActivity.MODE_PRIVATE
//    )
//    val language = preferences.getString("app_lang", "")
//    val locale = Locale(language)
//    Locale.setDefault(locale)
//
//    val configuration = Configuration()
//    configuration.locale = locale
//    this.resources.updateConfiguration(
//        configuration,
//        this.resources.displayMetrics
//    )

}

//change language
fun Activity.changeLanguage(languageCode: String) {
    // Update the app's configuration with the new language code
    val newLocale = Locale(languageCode)
    val configuration = resources.configuration
    configuration.setLocale(newLocale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun Activity.setCurrentLocale(langauge: String) {
    // Update the app's configuration with the new language code
    if (langauge.equals("Empty", true)) {
        return
    }
    val newLocale = Locale(langauge)
    val configuration = resources.configuration
    configuration.setLocale(newLocale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun refreshLanguageStrings() {
    Log.d("refreshLanguageStrings", "refreshLangaugeStrings called")
    EventBus.getDefault().post(PNLEvents.RefreshLanguageStrings())
}