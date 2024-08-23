package com.example.phonenumberlocator.admob_ads

import android.annotation.SuppressLint
import android.app.Activity
import com.example.phonenumberlocator.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

object RemoteConfigClass {

    // App Open Ad
    var App_Open_Ad: Boolean = true

    // Interstitial Ads
    var interSplash: Boolean = true
    var interOtherHome: Boolean = true
    var inter_pnl_isd_std_activity: Boolean = true
    var inter_pnl_contacts_detailed_activity: Boolean = true
    var inter_pnl_phone_contacts_activity: Boolean = true
    var inter_pnl_area_calculator_activity: Boolean = true
    var inter_pnl_distance_finder_activity: Boolean = true
    var inter_gps_location_activity: Boolean = true
    var inter_pnl_find_address_activity: Boolean = true
    var inter_pnl_share_location_activity: Boolean = true
    var inter_pnl_saved_image_confirmation_activity: Boolean = true
    var inter_pnl_gps_address_activity: Boolean = true
    var inter_pnl_language_activity: Boolean = true
    var inter_pnl_intro_slider_activity: Boolean = true
    var inter_call_locator_details_activity: Boolean = true
    var inter_pnl_call_locator_activity: Boolean = true
    var inter_exit_app_activity: Boolean = true

    // Banner Ads
    var banner_splash: Boolean = true
    var banner_main_activity: Boolean = true
    var banner_call_loc_activity: Boolean = true
    var banner_gps_track_activity: Boolean = true
    var banner_pnl_share_location_activity: Boolean = true
    var banner_cam_address_activity: Boolean = true
    var banner_pnl_area_calculator_activity: Boolean = true
    var banner_pnl_distance_finder_activity: Boolean = true
    var banner_pnl_find_address_activity: Boolean = true

    // Native Ads
    var native_language: Boolean = true
    var native_welcome_one: Boolean = true
    var native_welcome_three: Boolean = true
    var native_welcome_four: Boolean = true
    var native_pnl_call_locator_activity: Boolean = true
    var native_call_locator_details_activity: Boolean = true
    var native_gps_location_activity: Boolean = true
    var native_pnl_contacts_detailed_activity: Boolean = true
    var native_dup_language_activity: Boolean = true
    var native_language_other_activity: Boolean = true
    var native_language_other_dup_activity: Boolean = true
    var native_welcome_screen_activity: Boolean = true
    var native_welcome_screen_dup_activity: Boolean = true
    var native_exit_ad: Boolean = true


    //configurations
    @SuppressLint("StaticFieldLeak")
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private var interval: Long = 0
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = interval
    }


    private fun getRemoteConfig(): FirebaseRemoteConfig {
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_values)
        return remoteConfig
    }

    fun fetchRecord(application: Activity, callBack: (Boolean) -> Unit) {
        val remoteConfig = getRemoteConfig()
        remoteConfig.fetchAndActivate().addOnCompleteListener(application) { task ->
            App_Open_Ad = remoteConfig["App_Open_Ad"].asBoolean()
            interSplash = remoteConfig["inter_splash_src"].asBoolean()
            interOtherHome = remoteConfig["inter_other_home_scr"].asBoolean()
            banner_splash = remoteConfig["banner_splash"].asBoolean()
            native_language = remoteConfig["native_language"].asBoolean()
            native_welcome_one = remoteConfig["native_welcome_one"].asBoolean()
            native_welcome_three = remoteConfig["native_welcome_three"].asBoolean()
            native_welcome_four = remoteConfig["native_welcome_four"].asBoolean()
            banner_main_activity = remoteConfig["banner_main_activity"].asBoolean()
            banner_call_loc_activity = remoteConfig["banner_call_loc_activity"].asBoolean()
            native_pnl_call_locator_activity =
                remoteConfig["native_pnl_call_locator_activity"].asBoolean()
            native_call_locator_details_activity =
                remoteConfig["native_call_locator_details_activity"].asBoolean()
            native_pnl_contacts_detailed_activity =
                remoteConfig["native_pnl_contacts_detailed_activity"].asBoolean()
            banner_gps_track_activity =
                remoteConfig["banner_gps_track_activity"].asBoolean()
            native_gps_location_activity =
                remoteConfig["native_gps_location_activity"].asBoolean()
            banner_pnl_share_location_activity =
                remoteConfig["banner_pnl_share_location_activity"].asBoolean()
            banner_pnl_find_address_activity =
                remoteConfig["banner_pnl_find_address_activity"].asBoolean()
            banner_cam_address_activity =
                remoteConfig["banner_cam_address_activity"].asBoolean()
            banner_pnl_area_calculator_activity =
                remoteConfig["banner_pnl_area_calculator_activity"].asBoolean()
            banner_pnl_distance_finder_activity =
                remoteConfig["banner_pnl_distance_finder_activity"].asBoolean()
            inter_pnl_isd_std_activity =
                remoteConfig["inter_pnl_isd_std_activity"].asBoolean()
            inter_pnl_contacts_detailed_activity =
                remoteConfig["inter_pnl_contacts_detailed_activity"].asBoolean()
            inter_pnl_phone_contacts_activity =
                remoteConfig["inter_pnl_phone_contacts_activity"].asBoolean()
            inter_pnl_area_calculator_activity =
                remoteConfig["inter_pnl_area_calculator_activity"].asBoolean()
            inter_pnl_distance_finder_activity =
                remoteConfig["inter_pnl_distance_finder_activity"].asBoolean()
            inter_gps_location_activity =
                remoteConfig["inter_gps_location_activity"].asBoolean()
            inter_pnl_find_address_activity =
                remoteConfig["inter_pnl_find_address_activity"].asBoolean()
            inter_pnl_share_location_activity =
                remoteConfig["inter_pnl_share_location_activity"].asBoolean()
            inter_pnl_saved_image_confirmation_activity =
                remoteConfig["inter_pnl_saved_image_confirmation_activity"].asBoolean()
            inter_pnl_gps_address_activity =
                remoteConfig["inter_pnl_gps_address_activity"].asBoolean()
            inter_pnl_language_activity =
                remoteConfig["inter_pnl_language_activity"].asBoolean()
            inter_pnl_intro_slider_activity =
                remoteConfig["inter_pnl_intro_slider_activity"].asBoolean()
            inter_call_locator_details_activity =
                remoteConfig["inter_call_locator_details_activity"].asBoolean()
            inter_pnl_call_locator_activity =
                remoteConfig["inter_pnl_call_locator_activity"].asBoolean()
            native_dup_language_activity =
                remoteConfig["native_dup_language_activity"].asBoolean()
            native_language_other_activity =
                remoteConfig["native_language_other_activity"].asBoolean()
            native_language_other_dup_activity =
                remoteConfig["native_language_other_dup_activity"].asBoolean()
            native_welcome_screen_activity =
                remoteConfig["native_welcome_screen_activity"].asBoolean()
            native_welcome_screen_dup_activity =
                remoteConfig["native_welcome_screen_dup_activity"].asBoolean()
            inter_exit_app_activity =
                remoteConfig["inter_exit_app_activity"].asBoolean()
            native_exit_ad =
                remoteConfig["native_exit_ad"].asBoolean()


            callBack.invoke(task.isSuccessful)
        }
    }
}