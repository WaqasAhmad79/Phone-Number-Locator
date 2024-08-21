package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.example.phonenumberlocator.PhoneNumberLocator

class AdmobManagerClass(private val context: Context) : Application.ActivityLifecycleCallbacks {

    private val openApp: OpenApp = OpenApp

    init {
        OpenApp.initialize(context as PhoneNumberLocator)
        (context as Application).registerActivityLifecycleCallbacks(this)
    }

    // Implement other necessary AdManager functionalities based on your requirements.

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Handle activity created event
    }

    override fun onActivityStarted(activity: Activity) {
        // Handle activity started event
        OpenApp.onActivityStarted(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        // Handle activity resumed event
        OpenApp.onActivityResumed(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        // Handle activity paused event
    }

    override fun onActivityStopped(activity: Activity) {
        // Handle activity stopped event
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Handle activity save instance state event
    }

    override fun onActivityDestroyed(activity: Activity) {
        // Handle activity destroyed event
    }
}
