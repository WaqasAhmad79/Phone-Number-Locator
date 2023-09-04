package com.example.phonenumberlocator

import android.content.Context
import android.net.Uri
import android.text.format.DateFormat
import com.example.phonenumberlocator.pnlHelper.ACCENT_COLOR
import com.example.phonenumberlocator.pnlHelper.APP_ICON_COLOR
import com.example.phonenumberlocator.pnlHelper.APP_ID
import com.example.phonenumberlocator.pnlHelper.APP_PASSWORD_HASH
import com.example.phonenumberlocator.pnlHelper.APP_PASSWORD_PROTECTION
import com.example.phonenumberlocator.pnlHelper.APP_PROTECTION_TYPE
import com.example.phonenumberlocator.pnlHelper.APP_RUN_COUNT
import com.example.phonenumberlocator.pnlHelper.APP_SIDELOADING_STATUS
import com.example.phonenumberlocator.pnlHelper.BACKGROUND_COLOR
import com.example.phonenumberlocator.pnlHelper.BLOCKED_COUNTRY_CODES_LIST
import com.example.phonenumberlocator.pnlHelper.BLOCK_INTERNATIONAL_CALLS
import com.example.phonenumberlocator.pnlHelper.BLOCK_TOP_SPAMMERS
import com.example.phonenumberlocator.pnlHelper.BLOCK_UNKNOWN_NUMBERS
import com.example.phonenumberlocator.pnlHelper.CALLER_ID_SCREEN_SIZE_SETTING
import com.example.phonenumberlocator.pnlHelper.CONFLICT_SKIP
import com.example.phonenumberlocator.pnlHelper.CUSTOM_ACCENT_COLOR
import com.example.phonenumberlocator.pnlHelper.CUSTOM_APP_ICON_COLOR
import com.example.phonenumberlocator.pnlHelper.CUSTOM_BACKGROUND_COLOR
import com.example.phonenumberlocator.pnlHelper.CUSTOM_NAVIGATION_BAR_COLOR
import com.example.phonenumberlocator.pnlHelper.CUSTOM_PRIMARY_COLOR
import com.example.phonenumberlocator.pnlHelper.CUSTOM_TEXT_COLOR
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_EIGHT
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_FIVE
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_FOUR
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_ONE
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_SEVEN
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_SIX
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_THREE
import com.example.phonenumberlocator.pnlHelper.DATE_FORMAT_TWO
import com.example.phonenumberlocator.pnlHelper.DEFAULT_NAVIGATION_BAR_COLOR
import com.example.phonenumberlocator.pnlHelper.DEFAULT_TAB
import com.example.phonenumberlocator.pnlHelper.DELETE_PASSWORD_HASH
import com.example.phonenumberlocator.pnlHelper.DELETE_PASSWORD_PROTECTION
import com.example.phonenumberlocator.pnlHelper.DELETE_PROTECTION_TYPE
import com.example.phonenumberlocator.pnlHelper.ENABLE_PULL_TO_REFRESH
import com.example.phonenumberlocator.pnlHelper.FAVORITES
import com.example.phonenumberlocator.pnlHelper.FONT_SIZE
import com.example.phonenumberlocator.pnlHelper.HAD_THANK_YOU_INSTALLED
import com.example.phonenumberlocator.pnlHelper.INITIAL_WIDGET_HEIGHT
import com.example.phonenumberlocator.pnlHelper.INVALID_NAVIGATION_BAR_COLOR
import com.example.phonenumberlocator.pnlHelper.IS_USING_AUTO_THEME
import com.example.phonenumberlocator.pnlHelper.IS_USING_MODIFIED_APP_ICON
import com.example.phonenumberlocator.pnlHelper.IS_USING_SHARED_THEME
import com.example.phonenumberlocator.pnlHelper.IS_USING_SYSTEM_THEME
import com.example.phonenumberlocator.pnlHelper.KEEP_LAST_MODIFIED
import com.example.phonenumberlocator.pnlHelper.LAST_BLOCKED_NUMBERS_EXPORT_PATH
import com.example.phonenumberlocator.pnlHelper.LAST_CONFLICT_APPLY_TO_ALL
import com.example.phonenumberlocator.pnlHelper.LAST_CONFLICT_RESOLUTION
import com.example.phonenumberlocator.pnlHelper.LAST_EXPORTED_SETTINGS_FOLDER
import com.example.phonenumberlocator.pnlHelper.LAST_HANDLED_SHORTCUT_COLOR
import com.example.phonenumberlocator.pnlHelper.LAST_ICON_COLOR
import com.example.phonenumberlocator.pnlHelper.LAST_RENAME_PATTERN_USED
import com.example.phonenumberlocator.pnlHelper.LAST_RENAME_USED
import com.example.phonenumberlocator.pnlHelper.LAST_USED_VIEW_PAGER_PAGE
import com.example.phonenumberlocator.pnlHelper.LAST_VERSION
import com.example.phonenumberlocator.pnlHelper.NAVIGATION_BAR_COLOR
import com.example.phonenumberlocator.pnlHelper.OTG_ANDROID_DATA_TREE_URI
import com.example.phonenumberlocator.pnlHelper.OTG_ANDROID_OBB_TREE_URI
import com.example.phonenumberlocator.pnlHelper.OTG_PARTITION
import com.example.phonenumberlocator.pnlHelper.OTG_REAL_PATH
import com.example.phonenumberlocator.pnlHelper.OTG_TREE_URI
import com.example.phonenumberlocator.pnlHelper.PASSWORD_HASH
import com.example.phonenumberlocator.pnlHelper.PASSWORD_PROTECTION
import com.example.phonenumberlocator.pnlHelper.POPUP_POSITION_SETTINGS
import com.example.phonenumberlocator.pnlHelper.PREVENT_PHONE_FROM_SLEEPING
import com.example.phonenumberlocator.pnlHelper.PREVIOUS_INCOMMING_NUMBER
import com.example.phonenumberlocator.pnlHelper.PRIMARY_ANDROID_DATA_TREE_URI
import com.example.phonenumberlocator.pnlHelper.PRIMARY_ANDROID_OBB_TREE_URI
import com.example.phonenumberlocator.pnlHelper.PRIMARY_COLOR
import com.example.phonenumberlocator.pnlHelper.PRIVATE_HIDDEN_NUBERS
import com.example.phonenumberlocator.pnlHelper.PROTECTED_FOLDER_HASH
import com.example.phonenumberlocator.pnlHelper.PROTECTED_FOLDER_TYPE
import com.example.phonenumberlocator.pnlHelper.PROTECTION_NONE
import com.example.phonenumberlocator.pnlHelper.PROTECTION_PATTERN
import com.example.phonenumberlocator.pnlHelper.PROTECTION_TYPE
import com.example.phonenumberlocator.pnlHelper.REMEMBER_SIM_PREFIX
import com.example.phonenumberlocator.pnlHelper.REMIND_ME_OF_MISCALLS
import com.example.phonenumberlocator.pnlHelper.RENAME_SIMPLE
import com.example.phonenumberlocator.pnlHelper.SCROLL_HORIZONTALLY
import com.example.phonenumberlocator.pnlHelper.SD_ANDROID_DATA_TREE_URI
import com.example.phonenumberlocator.pnlHelper.SD_ANDROID_OBB_TREE_URI
import com.example.phonenumberlocator.pnlHelper.SD_TREE_URI
import com.example.phonenumberlocator.pnlHelper.SHOULD_USE_SHARED_THEME
import com.example.phonenumberlocator.pnlHelper.SHOW_AFTER_CALL_DETAIL
import com.example.phonenumberlocator.pnlHelper.SHOW_CALLER_ID_SETTINGS
import com.example.phonenumberlocator.pnlHelper.SHOW_CALL_ALERT_SETTINGS
import com.example.phonenumberlocator.pnlHelper.SHOW_CALL_CONFIRMATION
import com.example.phonenumberlocator.pnlHelper.SHOW_INFO_BUBBLE
import com.example.phonenumberlocator.pnlHelper.SHOW_MESSAGE_ALERT_SETTINGS
import com.example.phonenumberlocator.pnlHelper.SHOW_MISSCALL_NOTIFICATION_SETTINGS
import com.example.phonenumberlocator.pnlHelper.SHOW_MISSED_CALLS_NOTIFICATIONS
import com.example.phonenumberlocator.pnlHelper.SHOW_ONBOARDING_SCREENS
import com.example.phonenumberlocator.pnlHelper.SHOW_PHONEBOOK_CONTACTS
import com.example.phonenumberlocator.pnlHelper.SIDELOADING_UNCHECKED
import com.example.phonenumberlocator.pnlHelper.SKIP_DELETE_CONFIRMATION
import com.example.phonenumberlocator.pnlHelper.SNOOZE_TIME
import com.example.phonenumberlocator.pnlHelper.SORT_FOLDER_PREFIX
import com.example.phonenumberlocator.pnlHelper.SORT_ORDER
import com.example.phonenumberlocator.pnlHelper.START_NAME_WITH_SURNAME
import com.example.phonenumberlocator.pnlHelper.SUNDAY_FIRST
import com.example.phonenumberlocator.pnlHelper.TAB_LAST_USED
import com.example.phonenumberlocator.pnlHelper.TEXT_COLOR
import com.example.phonenumberlocator.pnlHelper.TIMEANDDATENOTIFICATION
import com.example.phonenumberlocator.pnlHelper.USE_24_HOUR_FORMAT
import com.example.phonenumberlocator.pnlHelper.USE_ENGLISH
import com.example.phonenumberlocator.pnlHelper.USE_SAME_SNOOZE
import com.example.phonenumberlocator.pnlHelper.VIBRATE_ON_MESSAGE_SETTING
import com.example.phonenumberlocator.pnlHelper.WAS_ALARM_WARNING_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_APP_ICON_CUSTOMIZATION_WARNING_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_APP_ON_SD_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_APP_RATED
import com.example.phonenumberlocator.pnlHelper.WAS_BEFORE_ASKING_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_BEFORE_RATE_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_CUSTOM_THEME_SWITCH_DESCRIPTION_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_FOLDER_LOCKING_NOTICE_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_INITIAL_UPGRADE_TO_PRO_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_MESSENGER_RECORDER_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_ORANGE_ICON_CHECKED
import com.example.phonenumberlocator.pnlHelper.WAS_OTG_HANDLED
import com.example.phonenumberlocator.pnlHelper.WAS_RATE_US_PROMPT_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_REMINDER_WARNING_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_SHARED_THEME_EVER_ACTIVATED
import com.example.phonenumberlocator.pnlHelper.WAS_SHARED_THEME_FORCED
import com.example.phonenumberlocator.pnlHelper.WAS_SORTING_BY_NUMERIC_VALUE_ADDED
import com.example.phonenumberlocator.pnlHelper.WAS_UPGRADED_FROM_FREE_SHOWN
import com.example.phonenumberlocator.pnlHelper.WAS_USE_ENGLISH_TOGGLED
import com.example.phonenumberlocator.pnlHelper.WIDGET_ID_TO_MEASURE
import com.example.phonenumberlocator.pnlHelper.WIDGET_TEXT_COLOR
import com.example.phonenumberlocator.pnlHelper.YOUR_ALARM_SOUNDS
import com.example.phonenumberlocator.pnlExtensionFun.getSharedPrefs
import java.text.SimpleDateFormat
import java.util.*

open class BaseConfigPNL(val context: Context) {
    protected val prefs = context.getSharedPrefs()

    companion object {
        fun newInstance(context: Context) = BaseConfigPNL(context)
    }

    var appRunCount: Int
        get() = prefs.getInt(APP_RUN_COUNT, 0)
        set(appRunCount) = prefs.edit().putInt(APP_RUN_COUNT, appRunCount).apply()

    //change to default false when deploy
    var showOnboardingScreens: Boolean
        get() = prefs.getBoolean(SHOW_ONBOARDING_SCREENS, true)
        set(showOnboardingScreens) = prefs.edit()
            .putBoolean(SHOW_ONBOARDING_SCREENS, showOnboardingScreens).apply()

    var lastVersion: Int
        get() = prefs.getInt(LAST_VERSION, 0)
        set(lastVersion) = prefs.edit().putInt(LAST_VERSION, lastVersion).apply()

    fun getCustomSIM(number: String) = prefs.getString(REMEMBER_SIM_PREFIX + number, "")

    fun saveCustomSIM(number: String, SIMlabel: String) {
        prefs.edit().putString(REMEMBER_SIM_PREFIX + number, Uri.encode(SIMlabel)).apply()
    }

    var appStarted: Boolean
        get() = prefs.getBoolean("show_splash_prefs", false)
        set(appStarted) = prefs.edit().putBoolean("show_splash_prefs", appStarted).apply()

    var isAppIntroComplete: Boolean
        get() = prefs.getBoolean("show_onboarding_screens_prefs", false)
        set(appStarted) = prefs.edit().putBoolean("show_onboarding_screens_prefs", appStarted).apply()

//for Localization
    var appLanguage: String?
        get() = prefs.getString("Language_Name", "English")
        set(appLanguage) = prefs.edit().putString("Language_Name", appLanguage).apply()


    //calls settings prefs values

    //notifications

    var previousTimeStamp: String?
        get() = prefs.getString(TIMEANDDATENOTIFICATION, "")
        set(previousTimeStamp) = prefs.edit()
            .putString(TIMEANDDATENOTIFICATION, previousTimeStamp).apply()

    var previousIncomingNumber: String?
        get() = prefs.getString(PREVIOUS_INCOMMING_NUMBER, "")
        set(previousIncomingNumber) = prefs.edit()
            .putString(PREVIOUS_INCOMMING_NUMBER, previousIncomingNumber).apply()

   /* var previousIncomingNumberType: CallType
        get() {
            return when (prefs.getString(PREVIOUS_INCOMMING_NUMBER_TYPE, "NONE")) {
                CallType.INCOMING_CALL.toString() -> {
                    CallType.INCOMING_CALL
                }
                CallType.OUTGOING_CALL.toString() -> {
                    CallType.OUTGOING_CALL
                }
                CallType.MISSED_CALL.toString() -> {
                    CallType.MISSED_CALL
                }
                else -> {
                    CallType.NONE
                }
            }
        }
        set(previousIncomingNumberType) {
            val store = when (previousIncomingNumberType) {
                CallType.INCOMING_CALL -> {
                    CallType.INCOMING_CALL.toString()
                }
                CallType.OUTGOING_CALL -> {
                    CallType.OUTGOING_CALL.toString()
                }
                CallType.MISSED_CALL -> {
                    CallType.MISSED_CALL.toString()
                }
                else -> {
                    CallType.NONE.toString()
                }
            }
            prefs.edit().putString(PREVIOUS_INCOMMING_NUMBER_TYPE, store).apply()
        }*/


//    Missed call notifications CallerIdSettingActivity

    var showMissedCallsNotifications: Boolean
        get() = prefs.getBoolean(SHOW_MISSED_CALLS_NOTIFICATIONS, true)
        set(showMissedCallsNotifications) = prefs.edit()
            .putBoolean(SHOW_MISSED_CALLS_NOTIFICATIONS, showMissedCallsNotifications).apply()

    var callerIdScreenSize: Int
        get() = prefs.getInt(CALLER_ID_SCREEN_SIZE_SETTING, 1)
        set(CallerIdScreenSize) = prefs.edit()
            .putInt(CALLER_ID_SCREEN_SIZE_SETTING, CallerIdScreenSize).apply()

    var showCallerId: Boolean
        get() = prefs.getBoolean(SHOW_CALLER_ID_SETTINGS, true)
        set(showCallerId) = prefs.edit().putBoolean(SHOW_CALLER_ID_SETTINGS, showCallerId).apply()

    var showCallerIdForContacts: Boolean
        get() = prefs.getBoolean(SHOW_PHONEBOOK_CONTACTS, true)
        set(showCallerIdForContacts) = prefs.edit()
            .putBoolean(SHOW_PHONEBOOK_CONTACTS, showCallerIdForContacts).apply()

    var showAfterCallDetail: Boolean
        get() = prefs.getBoolean(SHOW_AFTER_CALL_DETAIL, true)
        set(showAfterCallDetail) = prefs.edit()
            .putBoolean(SHOW_AFTER_CALL_DETAIL, showAfterCallDetail).apply()

    var popupPosition: Int
        get() = prefs.getInt(POPUP_POSITION_SETTINGS, 2)
        set(popupPosition) = prefs.edit().putInt(POPUP_POSITION_SETTINGS, popupPosition).apply()


    //SoundsAndNotificationsActivity  notifications switches
    var showMessageAlertNotificationSetting: Boolean
        get() = prefs.getBoolean(SHOW_MESSAGE_ALERT_SETTINGS, true)
        set(showMessageAlertSetting) = prefs.edit()
            .putBoolean(SHOW_MESSAGE_ALERT_SETTINGS, showMessageAlertSetting).apply()

    var showCallAlertNotificationsSetting: Boolean
        get() = prefs.getBoolean(SHOW_CALL_ALERT_SETTINGS, true)
        set(callAlertSetting) = prefs.edit().putBoolean(SHOW_CALL_ALERT_SETTINGS, callAlertSetting)
            .apply()

    var showMissedCallNotificationSetting: Boolean
        get() = prefs.getBoolean(SHOW_MISSCALL_NOTIFICATION_SETTINGS, true)
        set(showMissedCallNotificationSetting) = prefs.edit()
            .putBoolean(SHOW_MISSCALL_NOTIFICATION_SETTINGS, showMissedCallNotificationSetting)
            .apply()

    var remindMeOfMiscalls: Boolean
        get() = prefs.getBoolean(REMIND_ME_OF_MISCALLS, false)
        set(remindMeOfMiscalls) = prefs.edit().putBoolean(REMIND_ME_OF_MISCALLS, remindMeOfMiscalls)
            .apply()

    var vibrateOnMessage: Boolean
        get() = prefs.getBoolean(VIBRATE_ON_MESSAGE_SETTING, false)
        set(vibrateOnMessage) = prefs.edit()
            .putBoolean(VIBRATE_ON_MESSAGE_SETTING, vibrateOnMessage).apply()


    //block settings
    var blockTopSpammers: Boolean
        get() = prefs.getBoolean(BLOCK_TOP_SPAMMERS, false)
        set(blockTopSpammers) = prefs.edit()
            .putBoolean(BLOCK_TOP_SPAMMERS, blockTopSpammers).apply()

    var privateHiddenNumbers: Boolean
        get() = prefs.getBoolean(PRIVATE_HIDDEN_NUBERS, false)
        set(privateHiddenNumbers) = prefs.edit()
            .putBoolean(PRIVATE_HIDDEN_NUBERS, privateHiddenNumbers).apply()

    var blockUnknownNumbers: Boolean
        get() = prefs.getBoolean(BLOCK_UNKNOWN_NUMBERS, false)
        set(blockUnknownNumbers) = prefs.edit()
            .putBoolean(BLOCK_UNKNOWN_NUMBERS, blockUnknownNumbers).apply()

    var blockInternationalCalls: Boolean
        get() = prefs.getBoolean(BLOCK_INTERNATIONAL_CALLS, false)
        set(blockInternationalCalls) = prefs.edit()
            .putBoolean(BLOCK_INTERNATIONAL_CALLS, blockInternationalCalls).apply()

    var blockedCountryCodesList: String?
        get() = prefs.getString(BLOCKED_COUNTRY_CODES_LIST, null)
        set(blockedCountryCodesList) = prefs.edit()
            .putString(BLOCKED_COUNTRY_CODES_LIST, blockedCountryCodesList).apply()


    //user profile preferences
    var unknownIdentifiedCount: Int
        get() = prefs.getInt("UNKNOWN_NUMBERS_IDENTIFIED_COUNT", 0)
        set(unknownIdentifiedCount) = prefs.edit()
            .putInt("UNKNOWN_NUMBERS_IDENTIFIED_COUNT", unknownIdentifiedCount).apply()

    var messagesMovedToSpamCount: Int
        get() = prefs.getInt("MESSAGES_MOVED_TO_SPAM_COUNT", 0)
        set(messagesMovedToSpamCount) = prefs.edit()
            .putInt("MESSAGES_MOVED_TO_SPAM_COUNT", messagesMovedToSpamCount).apply()

    var spamCallsIdentifiedCount: Int
        get() = prefs.getInt("SPAM_CALLS_IDENTIFIED_COUNT", 0)
        set(spamCallsIdentifiedCount) = prefs.edit()
            .putInt("SPAM_CALLS_IDENTIFIED_COUNT", spamCallsIdentifiedCount).apply()

    var timeSavedFromSpammersCount: Int
        get() = prefs.getInt("TIME_SAVED_FROM_SPAMMERS_COUNT", 0)
        set(timeSavedFromSpammersCount) = prefs.edit()
            .putInt("TIME_SAVED_FROM_SPAMMERS_COUNT", timeSavedFromSpammersCount).apply()


    //other

    var dateFormat: String
        get() = prefs.getString(DATE_FORMAT, getDefaultDateFormat())!!
        set(dateFormat) = prefs.edit().putString(DATE_FORMAT, dateFormat).apply()

    var dateFormatOnlyDate: String
        get() = prefs.getString(DATE_FORMAT, DATE_FORMAT_FIVE)!!
        set(dateFormat) = prefs.edit().putString(DATE_FORMAT, dateFormat).apply()

    private fun getDefaultDateFormat(): String {
        val format = DateFormat.getDateFormat(context)
        val pattern = (format as SimpleDateFormat).toLocalizedPattern()
        return when (pattern.toLowerCase().replace(" ", "")) {
            "d.M.y" -> DATE_FORMAT_ONE
            "dd/mm/y" -> DATE_FORMAT_TWO
            "mm/dd/y" -> DATE_FORMAT_THREE
            "y-mm-dd" -> DATE_FORMAT_FOUR
            "dmmmmy" -> DATE_FORMAT_FIVE
            "mmmmdy" -> DATE_FORMAT_SIX
            "mm-dd-y" -> DATE_FORMAT_SEVEN
            "dd-mm-y" -> DATE_FORMAT_EIGHT
            else -> DATE_FORMAT_FIVE
        }
    }


    var primaryAndroidDataTreeUri: String
        get() = prefs.getString(PRIMARY_ANDROID_DATA_TREE_URI, "")!!
        set(uri) = prefs.edit().putString(PRIMARY_ANDROID_DATA_TREE_URI, uri).apply()

    var sdAndroidDataTreeUri: String
        get() = prefs.getString(SD_ANDROID_DATA_TREE_URI, "")!!
        set(uri) = prefs.edit().putString(SD_ANDROID_DATA_TREE_URI, uri).apply()

    var otgAndroidDataTreeUri: String
        get() = prefs.getString(OTG_ANDROID_DATA_TREE_URI, "")!!
        set(uri) = prefs.edit().putString(OTG_ANDROID_DATA_TREE_URI, uri).apply()

    var primaryAndroidObbTreeUri: String
        get() = prefs.getString(PRIMARY_ANDROID_OBB_TREE_URI, "")!!
        set(uri) = prefs.edit().putString(PRIMARY_ANDROID_OBB_TREE_URI, uri).apply()

    var sdAndroidObbTreeUri: String
        get() = prefs.getString(SD_ANDROID_OBB_TREE_URI, "")!!
        set(uri) = prefs.edit().putString(SD_ANDROID_OBB_TREE_URI, uri).apply()

    var otgAndroidObbTreeUri: String
        get() = prefs.getString(OTG_ANDROID_OBB_TREE_URI, "")!!
        set(uri) = prefs.edit().putString(OTG_ANDROID_OBB_TREE_URI, uri).apply()

    var sdTreeUri: String
        get() = prefs.getString(SD_TREE_URI, "")!!
        set(uri) = prefs.edit().putString(SD_TREE_URI, uri).apply()

    var OTGTreeUri: String
        get() = prefs.getString(OTG_TREE_URI, "")!!
        set(OTGTreeUri) = prefs.edit().putString(OTG_TREE_URI, OTGTreeUri).apply()

    var OTGPartition: String
        get() = prefs.getString(OTG_PARTITION, "")!!
        set(OTGPartition) = prefs.edit().putString(OTG_PARTITION, OTGPartition).apply()

    var OTGPath: String
        get() = prefs.getString(OTG_REAL_PATH, "")!!
        set(OTGPath) = prefs.edit().putString(OTG_REAL_PATH, OTGPath).apply()

/*
    var sdCardPath: String
        get() = prefs.getString(SD_CARD_PATH, getDefaultSDCardPath())!!
        set(sdCardPath) = prefs.edit().putString(SD_CARD_PATH, sdCardPath).apply()

    private fun getDefaultSDCardPath() =
        if (prefs.contains(SD_CARD_PATH)) "" else context.getSDCardPath()
*/

  /*  var internalStoragePath: String
        get() = prefs.getString(INTERNAL_STORAGE_PATH, getDefaultInternalPath())!!
        set(internalStoragePath) = prefs.edit()
            .putString(INTERNAL_STORAGE_PATH, internalStoragePath).apply()
*/
/*
    private fun getDefaultInternalPath() =
        if (prefs.contains(INTERNAL_STORAGE_PATH)) "" else context.getInternalStoragePath()
*/

    var textColor: Int
        get() = prefs.getInt(TEXT_COLOR, context.resources.getColor(R.color.white))
        set(textColor) = prefs.edit().putInt(TEXT_COLOR, textColor).apply()

    var backgroundColor: Int
        get() = prefs.getInt(
            BACKGROUND_COLOR,
            context.resources.getColor(R.color.white))
        set(backgroundColor) = prefs.edit().putInt(BACKGROUND_COLOR, backgroundColor).apply()

    var primaryColor: Int
        get() = prefs.getInt(PRIMARY_COLOR, context.resources.getColor(R.color.blue))
        set(primaryColor) = prefs.edit().putInt(PRIMARY_COLOR, primaryColor).apply()

    var accentColor: Int
        get() = prefs.getInt(ACCENT_COLOR, context.resources.getColor(R.color.blue))
        set(accentColor) = prefs.edit().putInt(ACCENT_COLOR, accentColor).apply()

    var navigationBarColor: Int
        get() = prefs.getInt(NAVIGATION_BAR_COLOR, INVALID_NAVIGATION_BAR_COLOR)
        set(navigationBarColor) = prefs.edit().putInt(NAVIGATION_BAR_COLOR, navigationBarColor)
            .apply()

    var defaultNavigationBarColor: Int
        get() = prefs.getInt(DEFAULT_NAVIGATION_BAR_COLOR, INVALID_NAVIGATION_BAR_COLOR)
        set(defaultNavigationBarColor) = prefs.edit()
            .putInt(DEFAULT_NAVIGATION_BAR_COLOR, defaultNavigationBarColor).apply()

    var lastHandledShortcutColor: Int
        get() = prefs.getInt(LAST_HANDLED_SHORTCUT_COLOR, 1)
        set(lastHandledShortcutColor) = prefs.edit()
            .putInt(LAST_HANDLED_SHORTCUT_COLOR, lastHandledShortcutColor).apply()

    var appIconColor: Int
        get() = prefs.getInt(APP_ICON_COLOR, context.resources.getColor(R.color.blue))
        set(appIconColor) {
            isUsingModifiedAppIcon =
                appIconColor != context.resources.getColor(R.color.blue)
            prefs.edit().putInt(APP_ICON_COLOR, appIconColor).apply()
        }

    var lastIconColor: Int
        get() = prefs.getInt(LAST_ICON_COLOR, context.resources.getColor(R.color.blue))
        set(lastIconColor) = prefs.edit().putInt(LAST_ICON_COLOR, lastIconColor).apply()

    var customTextColor: Int
        get() = prefs.getInt(CUSTOM_TEXT_COLOR, textColor)
        set(customTextColor) = prefs.edit().putInt(CUSTOM_TEXT_COLOR, customTextColor).apply()

    var customBackgroundColor: Int
        get() = prefs.getInt(CUSTOM_BACKGROUND_COLOR, backgroundColor)
        set(customBackgroundColor) = prefs.edit()
            .putInt(CUSTOM_BACKGROUND_COLOR, customBackgroundColor).apply()

    var customPrimaryColor: Int
        get() = prefs.getInt(CUSTOM_PRIMARY_COLOR, primaryColor)
        set(customPrimaryColor) = prefs.edit().putInt(CUSTOM_PRIMARY_COLOR, customPrimaryColor)
            .apply()

    var customAccentColor: Int
        get() = prefs.getInt(CUSTOM_ACCENT_COLOR, accentColor)
        set(customAccentColor) = prefs.edit().putInt(CUSTOM_ACCENT_COLOR, customAccentColor).apply()

    var customAppIconColor: Int
        get() = prefs.getInt(CUSTOM_APP_ICON_COLOR, appIconColor)
        set(customAppIconColor) = prefs.edit().putInt(CUSTOM_APP_ICON_COLOR, customAppIconColor)
            .apply()

    var customNavigationBarColor: Int
        get() = prefs.getInt(CUSTOM_NAVIGATION_BAR_COLOR, INVALID_NAVIGATION_BAR_COLOR)
        set(customNavigationBarColor) = prefs.edit()
            .putInt(CUSTOM_NAVIGATION_BAR_COLOR, customNavigationBarColor).apply()

//    var widgetBgColor: Int
//        get() = prefs.getInt(WIDGET_BG_COLOR,
//            context.resources.getInteger(R.integer.default_widget_bg_color))
//        set(widgetBgColor) = prefs.edit().putInt(WIDGET_BG_COLOR, widgetBgColor).apply()

    var widgetTextColor: Int
        get() = prefs.getInt(WIDGET_TEXT_COLOR, context.resources.getColor(R.color.blue))
        set(widgetTextColor) = prefs.edit().putInt(WIDGET_TEXT_COLOR, widgetTextColor).apply()

    // hidden folder visibility protection
    var isHiddenPasswordProtectionOn: Boolean
        get() = prefs.getBoolean(PASSWORD_PROTECTION, false)
        set(isHiddenPasswordProtectionOn) = prefs.edit()
            .putBoolean(PASSWORD_PROTECTION, isHiddenPasswordProtectionOn).apply()

    var hiddenPasswordHash: String
        get() = prefs.getString(PASSWORD_HASH, "")!!
        set(hiddenPasswordHash) = prefs.edit().putString(PASSWORD_HASH, hiddenPasswordHash).apply()

    var hiddenProtectionType: Int
        get() = prefs.getInt(PROTECTION_TYPE, PROTECTION_PATTERN)
        set(hiddenProtectionType) = prefs.edit().putInt(PROTECTION_TYPE, hiddenProtectionType)
            .apply()

    // whole app launch protection
    var isAppPasswordProtectionOn: Boolean
        get() = prefs.getBoolean(APP_PASSWORD_PROTECTION, false)
        set(isAppPasswordProtectionOn) = prefs.edit()
            .putBoolean(APP_PASSWORD_PROTECTION, isAppPasswordProtectionOn).apply()

    var appPasswordHash: String
        get() = prefs.getString(APP_PASSWORD_HASH, "")!!
        set(appPasswordHash) = prefs.edit().putString(APP_PASSWORD_HASH, appPasswordHash).apply()

    var appProtectionType: Int
        get() = prefs.getInt(APP_PROTECTION_TYPE, PROTECTION_PATTERN)
        set(appProtectionType) = prefs.edit().putInt(APP_PROTECTION_TYPE, appProtectionType).apply()

    // file delete and move protection
    var isDeletePasswordProtectionOn: Boolean
        get() = prefs.getBoolean(DELETE_PASSWORD_PROTECTION, false)
        set(isDeletePasswordProtectionOn) = prefs.edit()
            .putBoolean(DELETE_PASSWORD_PROTECTION, isDeletePasswordProtectionOn).apply()

    var deletePasswordHash: String
        get() = prefs.getString(DELETE_PASSWORD_HASH, "")!!
        set(deletePasswordHash) = prefs.edit().putString(DELETE_PASSWORD_HASH, deletePasswordHash)
            .apply()

    var deleteProtectionType: Int
        get() = prefs.getInt(DELETE_PROTECTION_TYPE, PROTECTION_PATTERN)
        set(deleteProtectionType) = prefs.edit()
            .putInt(DELETE_PROTECTION_TYPE, deleteProtectionType).apply()

    // folder locking
    fun addFolderProtection(path: String, hash: String, type: Int) {
        prefs.edit()
            .putString("$PROTECTED_FOLDER_HASH$path", hash)
            .putInt("$PROTECTED_FOLDER_TYPE$path", type)
            .apply()
    }

    fun removeFolderProtection(path: String) {
        prefs.edit()
            .remove("$PROTECTED_FOLDER_HASH$path")
            .remove("$PROTECTED_FOLDER_TYPE$path")
            .apply()
    }

    fun isFolderProtected(path: String) = getFolderProtectionType(path) != PROTECTION_NONE

    fun getFolderProtectionHash(path: String) =
        prefs.getString("$PROTECTED_FOLDER_HASH$path", "") ?: ""

    fun getFolderProtectionType(path: String) =
        prefs.getInt("$PROTECTED_FOLDER_TYPE$path", PROTECTION_NONE)

    var keepLastModified: Boolean
        get() = prefs.getBoolean(KEEP_LAST_MODIFIED, true)
        set(keepLastModified) = prefs.edit().putBoolean(KEEP_LAST_MODIFIED, keepLastModified)
            .apply()

    var useEnglish: Boolean
        get() = prefs.getBoolean(USE_ENGLISH, false)
        set(useEnglish) {
            wasUseEnglishToggled = true
            prefs.edit().putBoolean(USE_ENGLISH, useEnglish).commit()
        }

    var wasUseEnglishToggled: Boolean
        get() = prefs.getBoolean(WAS_USE_ENGLISH_TOGGLED, false)
        set(wasUseEnglishToggled) = prefs.edit()
            .putBoolean(WAS_USE_ENGLISH_TOGGLED, wasUseEnglishToggled).apply()

    var wasSharedThemeEverActivated: Boolean
        get() = prefs.getBoolean(WAS_SHARED_THEME_EVER_ACTIVATED, false)
        set(wasSharedThemeEverActivated) = prefs.edit()
            .putBoolean(WAS_SHARED_THEME_EVER_ACTIVATED, wasSharedThemeEverActivated).apply()

    var isUsingSharedTheme: Boolean
        get() = prefs.getBoolean(IS_USING_SHARED_THEME, false)
        set(isUsingSharedTheme) = prefs.edit().putBoolean(IS_USING_SHARED_THEME, isUsingSharedTheme)
            .apply()

    // used by Simple Thank You, stop using shared Shared Theme if it has been changed in it
    var shouldUseSharedTheme: Boolean
        get() = prefs.getBoolean(SHOULD_USE_SHARED_THEME, false)
        set(shouldUseSharedTheme) = prefs.edit()
            .putBoolean(SHOULD_USE_SHARED_THEME, shouldUseSharedTheme).apply()

    var isUsingAutoTheme: Boolean
        get() = prefs.getBoolean(IS_USING_AUTO_THEME, false)
        set(isUsingAutoTheme) = prefs.edit().putBoolean(IS_USING_AUTO_THEME, isUsingAutoTheme)
            .apply()

    var isUsingSystemTheme: Boolean
        get() = prefs.getBoolean(IS_USING_SYSTEM_THEME, true)
        set(isUsingSystemTheme) = prefs.edit().putBoolean(IS_USING_SYSTEM_THEME, isUsingSystemTheme)
            .apply()

    var wasCustomThemeSwitchDescriptionShown: Boolean
        get() = prefs.getBoolean(WAS_CUSTOM_THEME_SWITCH_DESCRIPTION_SHOWN, false)
        set(wasCustomThemeSwitchDescriptionShown) = prefs.edit().putBoolean(
            WAS_CUSTOM_THEME_SWITCH_DESCRIPTION_SHOWN, wasCustomThemeSwitchDescriptionShown)
            .apply()

    var wasSharedThemeForced: Boolean
        get() = prefs.getBoolean(WAS_SHARED_THEME_FORCED, false)
        set(wasSharedThemeForced) = prefs.edit()
            .putBoolean(WAS_SHARED_THEME_FORCED, wasSharedThemeForced).apply()

    var showInfoBubble: Boolean
        get() = prefs.getBoolean(SHOW_INFO_BUBBLE, true)
        set(showInfoBubble) = prefs.edit().putBoolean(SHOW_INFO_BUBBLE, showInfoBubble).apply()

    var lastConflictApplyToAll: Boolean
        get() = prefs.getBoolean(LAST_CONFLICT_APPLY_TO_ALL, true)
        set(lastConflictApplyToAll) = prefs.edit()
            .putBoolean(LAST_CONFLICT_APPLY_TO_ALL, lastConflictApplyToAll).apply()

    var lastConflictResolution: Int
        get() = prefs.getInt(LAST_CONFLICT_RESOLUTION, CONFLICT_SKIP)
        set(lastConflictResolution) = prefs.edit()
            .putInt(LAST_CONFLICT_RESOLUTION, lastConflictResolution).apply()

    var sorting: Int
        get() = prefs.getInt(SORT_ORDER, context.resources.getInteger(R.integer.default_sorting))
        set(sorting) = prefs.edit().putInt(SORT_ORDER, sorting).apply()

    fun saveCustomSorting(path: String, value: Int) {
        if (path.isEmpty()) {
            sorting = value
        } else {
            prefs.edit().putInt(SORT_FOLDER_PREFIX + path.toLowerCase(), value).apply()
        }
    }

    fun getFolderSorting(path: String) =
        prefs.getInt(SORT_FOLDER_PREFIX + path.toLowerCase(), sorting)

    fun removeCustomSorting(path: String) {
        prefs.edit().remove(SORT_FOLDER_PREFIX + path.toLowerCase()).apply()
    }

    fun hasCustomSorting(path: String) = prefs.contains(SORT_FOLDER_PREFIX + path.toLowerCase())

    var hadThankYouInstalled: Boolean
        get() = prefs.getBoolean(HAD_THANK_YOU_INSTALLED, false)
        set(hadThankYouInstalled) = prefs.edit()
            .putBoolean(HAD_THANK_YOU_INSTALLED, hadThankYouInstalled).apply()

    var skipDeleteConfirmation: Boolean
        get() = prefs.getBoolean(SKIP_DELETE_CONFIRMATION, false)
        set(skipDeleteConfirmation) = prefs.edit()
            .putBoolean(SKIP_DELETE_CONFIRMATION, skipDeleteConfirmation).apply()

    var enablePullToRefresh: Boolean
        get() = prefs.getBoolean(ENABLE_PULL_TO_REFRESH, true)
        set(enablePullToRefresh) = prefs.edit()
            .putBoolean(ENABLE_PULL_TO_REFRESH, enablePullToRefresh).apply()

    var scrollHorizontally: Boolean
        get() = prefs.getBoolean(SCROLL_HORIZONTALLY, false)
        set(scrollHorizontally) = prefs.edit().putBoolean(SCROLL_HORIZONTALLY, scrollHorizontally)
            .apply()

    var preventPhoneFromSleeping: Boolean
        get() = prefs.getBoolean(PREVENT_PHONE_FROM_SLEEPING, true)
        set(preventPhoneFromSleeping) = prefs.edit()
            .putBoolean(PREVENT_PHONE_FROM_SLEEPING, preventPhoneFromSleeping).apply()

    var lastUsedViewPagerPage: Int
        get() = prefs.getInt(
            LAST_USED_VIEW_PAGER_PAGE,
            context.resources.getInteger(R.integer.default_viewpager_page))
        set(lastUsedViewPagerPage) = prefs.edit()
            .putInt(LAST_USED_VIEW_PAGER_PAGE, lastUsedViewPagerPage).apply()

    var use24HourFormat: Boolean
        get() = prefs.getBoolean(USE_24_HOUR_FORMAT, DateFormat.is24HourFormat(context))
        set(use24HourFormat) = prefs.edit().putBoolean(USE_24_HOUR_FORMAT, use24HourFormat).apply()

    var isSundayFirst: Boolean
        get() {
            val isSundayFirst =
                Calendar.getInstance(Locale.getDefault()).firstDayOfWeek == Calendar.SUNDAY
            return prefs.getBoolean(SUNDAY_FIRST, isSundayFirst)
        }
        set(sundayFirst) = prefs.edit().putBoolean(SUNDAY_FIRST, sundayFirst).apply()

    var wasAlarmWarningShown: Boolean
        get() = prefs.getBoolean(WAS_ALARM_WARNING_SHOWN, false)
        set(wasAlarmWarningShown) = prefs.edit()
            .putBoolean(WAS_ALARM_WARNING_SHOWN, wasAlarmWarningShown).apply()

    var wasReminderWarningShown: Boolean
        get() = prefs.getBoolean(WAS_REMINDER_WARNING_SHOWN, false)
        set(wasReminderWarningShown) = prefs.edit()
            .putBoolean(WAS_REMINDER_WARNING_SHOWN, wasReminderWarningShown).apply()

    var useSameSnooze: Boolean
        get() = prefs.getBoolean(USE_SAME_SNOOZE, true)
        set(useSameSnooze) = prefs.edit().putBoolean(USE_SAME_SNOOZE, useSameSnooze).apply()

    var snoozeTime: Int
        get() = prefs.getInt(SNOOZE_TIME, 10)
        set(snoozeDelay) = prefs.edit().putInt(SNOOZE_TIME, snoozeDelay).apply()

//    var vibrateOnButtonPress: Boolean
//        get() = prefs.getBoolean(VIBRATE_ON_BUTTON_PRESS,
//            context.resources.getBoolean(R.bool.default_vibrate_on_press))
//        set(vibrateOnButton) = prefs.edit().putBoolean(VIBRATE_ON_BUTTON_PRESS, vibrateOnButton)
//            .apply()

    var yourAlarmSounds: String
        get() = prefs.getString(YOUR_ALARM_SOUNDS, "")!!
        set(yourAlarmSounds) = prefs.edit().putString(YOUR_ALARM_SOUNDS, yourAlarmSounds).apply()

    var isUsingModifiedAppIcon: Boolean
        get() = prefs.getBoolean(IS_USING_MODIFIED_APP_ICON, false)
        set(isUsingModifiedAppIcon) = prefs.edit()
            .putBoolean(IS_USING_MODIFIED_APP_ICON, isUsingModifiedAppIcon).apply()

    var appId: String
        get() = prefs.getString(APP_ID, "")!!
        set(appId) = prefs.edit().putString(APP_ID, appId).apply()

    var initialWidgetHeight: Int
        get() = prefs.getInt(INITIAL_WIDGET_HEIGHT, 0)
        set(initialWidgetHeight) = prefs.edit().putInt(INITIAL_WIDGET_HEIGHT, initialWidgetHeight)
            .apply()

    var widgetIdToMeasure: Int
        get() = prefs.getInt(WIDGET_ID_TO_MEASURE, 0)
        set(widgetIdToMeasure) = prefs.edit().putInt(WIDGET_ID_TO_MEASURE, widgetIdToMeasure)
            .apply()

    var wasOrangeIconChecked: Boolean
        get() = prefs.getBoolean(WAS_ORANGE_ICON_CHECKED, false)
        set(wasOrangeIconChecked) = prefs.edit()
            .putBoolean(WAS_ORANGE_ICON_CHECKED, wasOrangeIconChecked).apply()

    var wasAppOnSDShown: Boolean
        get() = prefs.getBoolean(WAS_APP_ON_SD_SHOWN, false)
        set(wasAppOnSDShown) = prefs.edit().putBoolean(WAS_APP_ON_SD_SHOWN, wasAppOnSDShown).apply()

    var wasBeforeAskingShown: Boolean
        get() = prefs.getBoolean(WAS_BEFORE_ASKING_SHOWN, false)
        set(wasBeforeAskingShown) = prefs.edit()
            .putBoolean(WAS_BEFORE_ASKING_SHOWN, wasBeforeAskingShown).apply()

    var wasBeforeRateShown: Boolean
        get() = prefs.getBoolean(WAS_BEFORE_RATE_SHOWN, false)
        set(wasBeforeRateShown) = prefs.edit().putBoolean(WAS_BEFORE_RATE_SHOWN, wasBeforeRateShown)
            .apply()

    var wasInitialUpgradeToProShown: Boolean
        get() = prefs.getBoolean(WAS_INITIAL_UPGRADE_TO_PRO_SHOWN, false)
        set(wasInitialUpgradeToProShown) = prefs.edit()
            .putBoolean(WAS_INITIAL_UPGRADE_TO_PRO_SHOWN, wasInitialUpgradeToProShown).apply()

    var wasAppIconCustomizationWarningShown: Boolean
        get() = prefs.getBoolean(WAS_APP_ICON_CUSTOMIZATION_WARNING_SHOWN, false)
        set(wasAppIconCustomizationWarningShown) = prefs.edit().putBoolean(
            WAS_APP_ICON_CUSTOMIZATION_WARNING_SHOWN, wasAppIconCustomizationWarningShown)
            .apply()

    var appSideloadingStatus: Int
        get() = prefs.getInt(APP_SIDELOADING_STATUS, SIDELOADING_UNCHECKED)
        set(appSideloadingStatus) = prefs.edit()
            .putInt(APP_SIDELOADING_STATUS, appSideloadingStatus).apply()


    var wasOTGHandled: Boolean
        get() = prefs.getBoolean(WAS_OTG_HANDLED, false)
        set(wasOTGHandled) = prefs.edit().putBoolean(WAS_OTG_HANDLED, wasOTGHandled).apply()

    var wasUpgradedFromFreeShown: Boolean
        get() = prefs.getBoolean(WAS_UPGRADED_FROM_FREE_SHOWN, false)
        set(wasUpgradedFromFreeShown) = prefs.edit()
            .putBoolean(WAS_UPGRADED_FROM_FREE_SHOWN, wasUpgradedFromFreeShown).apply()

    var wasRateUsPromptShown: Boolean
        get() = prefs.getBoolean(WAS_RATE_US_PROMPT_SHOWN, false)
        set(wasRateUsPromptShown) = prefs.edit()
            .putBoolean(WAS_RATE_US_PROMPT_SHOWN, wasRateUsPromptShown).apply()

    var wasAppRated: Boolean
        get() = prefs.getBoolean(WAS_APP_RATED, false)
        set(wasAppRated) = prefs.edit().putBoolean(WAS_APP_RATED, wasAppRated).apply()

    var wasSortingByNumericValueAdded: Boolean
        get() = prefs.getBoolean(WAS_SORTING_BY_NUMERIC_VALUE_ADDED, false)
        set(wasSortingByNumericValueAdded) = prefs.edit().putBoolean(
            WAS_SORTING_BY_NUMERIC_VALUE_ADDED, wasSortingByNumericValueAdded).apply()

    var wasFolderLockingNoticeShown: Boolean
        get() = prefs.getBoolean(WAS_FOLDER_LOCKING_NOTICE_SHOWN, false)
        set(wasFolderLockingNoticeShown) = prefs.edit()
            .putBoolean(WAS_FOLDER_LOCKING_NOTICE_SHOWN, wasFolderLockingNoticeShown).apply()

    var lastRenameUsed: Int
        get() = prefs.getInt(LAST_RENAME_USED, RENAME_SIMPLE)
        set(lastRenameUsed) = prefs.edit().putInt(LAST_RENAME_USED, lastRenameUsed).apply()

    var lastRenamePatternUsed: String
        get() = prefs.getString(LAST_RENAME_PATTERN_USED, "")!!
        set(lastRenamePatternUsed) = prefs.edit()
            .putString(LAST_RENAME_PATTERN_USED, lastRenamePatternUsed).apply()

    var lastExportedSettingsFolder: String
        get() = prefs.getString(LAST_EXPORTED_SETTINGS_FOLDER, "")!!
        set(lastExportedSettingsFolder) = prefs.edit()
            .putString(LAST_EXPORTED_SETTINGS_FOLDER, lastExportedSettingsFolder).apply()

    var lastBlockedNumbersExportPath: String
        get() = prefs.getString(LAST_BLOCKED_NUMBERS_EXPORT_PATH, "")!!
        set(lastBlockedNumbersExportPath) = prefs.edit()
            .putString(LAST_BLOCKED_NUMBERS_EXPORT_PATH, lastBlockedNumbersExportPath).apply()


    var fontSize: Int
        get() = prefs.getInt(FONT_SIZE, context.resources.getInteger(R.integer.default_font_size))
        set(size) = prefs.edit().putInt(FONT_SIZE, size).apply()

    // notify the users about new SMS Messenger and Voice Recorder released
    var wasMessengerRecorderShown: Boolean
        get() = prefs.getBoolean(WAS_MESSENGER_RECORDER_SHOWN, false)
        set(wasMessengerRecorderShown) = prefs.edit()
            .putBoolean(WAS_MESSENGER_RECORDER_SHOWN, wasMessengerRecorderShown).apply()

    var defaultTab: Int
        get() = prefs.getInt(DEFAULT_TAB, TAB_LAST_USED)
        set(defaultTab) = prefs.edit().putInt(DEFAULT_TAB, defaultTab).apply()

    var startNameWithSurname: Boolean
        get() = prefs.getBoolean(START_NAME_WITH_SURNAME, false)
        set(startNameWithSurname) = prefs.edit()
            .putBoolean(START_NAME_WITH_SURNAME, startNameWithSurname).apply()

    var favorites: MutableSet<String>
        get() = prefs.getStringSet(FAVORITES, HashSet())!!
        set(favorites) = prefs.edit().remove(FAVORITES).putStringSet(FAVORITES, favorites).apply()

    var showCallConfirmation: Boolean
        get() = prefs.getBoolean(SHOW_CALL_CONFIRMATION, false)
        set(showCallConfirmation) = prefs.edit()
            .putBoolean(SHOW_CALL_CONFIRMATION, showCallConfirmation).apply()


    var defaultCountryISO: String?
        get() = prefs.getString("DEFAULT_COUNTRY_ISO_CODE", "US")
        set(defaultCountryISO) = prefs.edit().putString("DEFAULT_COUNTRY_ISO_CODE", defaultCountryISO)
            .apply()


//
//    // color picker last used colors
//    internal var colorPickerRecentColors: LinkedList<Int>
//        get(): LinkedList<Int> {
//            val defaultList = arrayListOf(
//                context.resources.getColor(R.color.white),
//                context.resources.getColor(R.color.app_color),
//                context.resources.getColor(R.color.buttonColor),
//                context.resources.getColor(R.color.black),
//                context.resources.getColor(R.color.clickColor)
//            )
//            return LinkedList(prefs.getString(COLOR_PICKER_RECENT_COLORS, null)?.lines()
//                ?.map { it.toInt() } ?: defaultList)
//        }
//        set(recentColors) = prefs.edit()
//            .putString(COLOR_PICKER_RECENT_COLORS, recentColors.joinToString(separator = "\n"))
//            .apply()



}

