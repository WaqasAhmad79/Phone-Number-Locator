package com.example.phonenumberlocator.admob_ads

import android.app.Activity
import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform


class GDPR(val activity: Activity) {
    private val TAG="TAGGDPR"
    private  var consentInformation: ConsentInformation?=null
    fun setGDPRConsent(callback: (Boolean)->Unit){
        // Set tag for under age of consent. false means users are not under age
        // of consent.
        // Set tag for under age of consent. false means users are not under age
        // of consent.


        /**
         * Un comment this code if you want to test consent form
         *
         * */
       /* val debugSettings = ConsentDebugSettings.Builder(activity)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId("E6AD84E326FD1B88782262A8A4AE5774") // you can get hash from logcat after running app set TAG in logcat "ConsentDebugSetting"
            .build()*/


        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
//            .setConsentDebugSettings(debugSettings)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        consentInformation?.requestConsentInfoUpdate(
            activity,
            params,
            OnConsentInfoUpdateSuccessListener {
                Log.d(TAG, "setGDPRConsent: OnConsentInfoUpdateSuccessListener")
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) { loadAndShowError ->
                    // Consent gathering failed.
                    Log.w(
                        TAG, String.format(
                            "%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message
                        )
                    )

                    // Consent has been gathered.
                }

            },
            OnConsentInfoUpdateFailureListener { requestConsentError: FormError ->
                Log.d(TAG, "setGDPRConsent OnConsentInfoUpdateFailureListener: ")
                // Consent gathering failed.
                Log.w(
                    TAG, String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
            })


        if(consentInformation?.canRequestAds() == true){
            Log.d(TAG, "setGDPRConsent: we can show ads now")
            callback.invoke(true)
        }else{
            Log.d(TAG, "setGDPRConsent: can not show ads right now")
            callback.invoke(false)
        }
    }
}