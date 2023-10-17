package com.example.phonenumberlocator.pnlSharedPreferencesLang

import android.content.Context

class PNLMySharePreferences(private val context: Context) {
    var appStarted: Boolean
        get() {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            return prefs.getBoolean("get_started", false)
        }
        set(value) {
            val prefs = context.getSharedPreferences(PREFS_NAME, 0)
            val editor = prefs.edit()
            editor.putBoolean("get_started", value)
            editor.apply()
            editor.commit()
        }


    companion object {
        const val PREFS_NAME = "get_started"
    }
}