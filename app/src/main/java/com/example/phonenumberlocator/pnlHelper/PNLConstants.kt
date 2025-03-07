package com.example.phonenumberlocator.pnlHelper


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.pnlOverloads.times
import java.util.*

var IS_PERMISSION_ON="ispermissionon"

const val CURRENT_THEME_TYPE = "current_theme_name_type"
const val IS_REWARDED = "is_rewarded_ad"
const val CURRENT_THEME = "current_theme_name"
const val PATH_OUTPUT = "output_path"

const val CONTECT_MODEL = "contact_model"
const val EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents"
const val EXTRA_SHOW_ADVANCED = "android.content.extra.SHOW_ADVANCED"
const val SELECTED_COUNTRY = "selected_country"
const val CURRENT_UNIT = "current_UNIT_name"

const val UNIT_CM = "Square Centimeter (cm²)"
const val UNIT_FT = "Square Feet (ft²)"
const val UNIT_M = "Square Meter (m²)"
const val UNIT_KM = "Square Kilometer (km²) "
const val UNIT_MI = "Square Mile (mi²)"
const val UNIT_MARLA = "Marla"
const val UNIT_KANAL = "Kanal"
const val UNIT_BIGHA = "Bigha"
const val UNIT_ACRE = "Acre"
const val UNIT_KILLA = "Killa"
const val UNIT_MURABBA = "Murabba"



const val UNIT_METERS = "m"
const val UNIT_KILOMETERS = "km"
const val UNIT_MILES = "miles"

//const val PERMISSION_ACCESS_FINE_LOCATION = 16

const val APP_NAME = "app_name"
const val APP_LICENSES = "app_licenses"
const val APP_FAQ = "app_faq"
const val APP_VERSION_NAME = "app_version_name"
const val APP_ICON_IDS = "app_icon_ids"
const val APP_ID = "app_id"
const val APP_LAUNCHER_NAME = "app_launcher_name"
const val REAL_FILE_PATH = "real_file_path_2"
const val IS_FROM_GALLERY = "is_from_gallery"
const val BROADCAST_REFRESH_MEDIA = "com.simplemobiletools.REFRESH_MEDIA"
const val REFRESH_PATH = "refresh_path"
const val IS_CUSTOMIZING_COLORS = "is_customizing_colors"
const val BLOCKED_NUMBERS_EXPORT_DELIMITER = ","
const val BLOCKED_NUMBERS_EXPORT_EXTENSION = ".txt"
const val NOMEDIA = ".nomedia"
const val YOUR_ALARM_SOUNDS_MIN_ID = 1000
const val SHOW_FAQ_BEFORE_MAIL = "show_faq_before_mail"
const val INVALID_NAVIGATION_BAR_COLOR = -1
const val CHOPPED_LIST_DEFAULT_SIZE = 50
const val SAVE_DISCARD_PROMPT_INTERVAL = 1000L
const val SD_OTG_PATTERN = "^/storage/[A-Za-z0-9]{4}-[A-Za-z0-9]{4}$"
const val SD_OTG_SHORT = "^[A-Za-z0-9]{4}-[A-Za-z0-9]{4}$"
const val KEY_PHONE = "phone"
const val KEY_MAILTO = "mailto"
const val CONTACT_ID = "contact_id"
const val IS_PRIVATE = "is_private"
const val MD5 = "MD5"
const val SHORT_ANIMATION_DURATION = 150L
val DARK_GREY = 0xFF333333.toInt()

const val LOWER_ALPHA = 0.25f
const val MEDIUM_ALPHA = 0.5f
const val HIGHER_ALPHA = 0.75f

const val HOUR_MINUTES = 60
const val DAY_MINUTES = 24 * HOUR_MINUTES
const val WEEK_MINUTES = DAY_MINUTES * 7
const val MONTH_MINUTES = DAY_MINUTES * 30
const val YEAR_MINUTES = DAY_MINUTES * 365

const val MINUTE_SECONDS = 60
const val HOUR_SECONDS = HOUR_MINUTES * 60
const val DAY_SECONDS = DAY_MINUTES * 60
const val WEEK_SECONDS = WEEK_MINUTES * 60
const val MONTH_SECONDS = MONTH_MINUTES * 60
const val YEAR_SECONDS = YEAR_MINUTES * 60

const val DEFAULT_FILE_NAME = "contacts.vcf"

// phone number/email types
const val CELL = "CELL"
const val WORK = "WORK"
const val HOME = "HOME"
const val OTHER = "OTHER"
const val PREF = "PREF"
const val MAIN = "MAIN"
const val FAX = "FAX"
const val WORK_FAX = "WORK;FAX"
const val HOME_FAX = "HOME;FAX"
const val PAGER = "PAGER"
const val MOBILE = "MOBILE"

// global intents
const val OPEN_DOCUMENT_TREE_FOR_ANDROID_DATA_OR_OBB = 1000
const val OPEN_DOCUMENT_TREE_SD = 1002
const val OPEN_DOCUMENT_TREE_OTG = 1001
const val OPEN_DOCUMENT_TREE_FOR_SDK_30 = 1003

// shared preferences
const val PREFS_KEY = "Prefs"
const val APP_RUN_COUNT = "app_run_count"
const val SHOW_ONBOARDING_SCREENS = "show_onboarding_screens_prefs"
const val LAST_VERSION = "last_version"
const val SD_TREE_URI = "tree_uri_2"
const val PRIMARY_ANDROID_DATA_TREE_URI = "primary_android_data_tree_uri_2"
const val OTG_ANDROID_DATA_TREE_URI = "otg_android_data_tree__uri_2"
const val SD_ANDROID_DATA_TREE_URI = "sd_android_data_tree_uri_2"
const val PRIMARY_ANDROID_OBB_TREE_URI = "primary_android_obb_tree_uri_2"
const val OTG_ANDROID_OBB_TREE_URI = "otg_android_obb_tree_uri_2"
const val SD_ANDROID_OBB_TREE_URI = "sd_android_obb_tree_uri_2"
const val OTG_TREE_URI = "otg_tree_uri_2"
const val SD_CARD_PATH = "sd_card_path_2"
const val OTG_REAL_PATH = "otg_real_path_2"
const val INTERNAL_STORAGE_PATH = "internal_storage_path"
const val TEXT_COLOR = "text_color"
const val BACKGROUND_COLOR = "background_color"
const val PRIMARY_COLOR = "primary_color_2"
const val ACCENT_COLOR = "accent_color"
const val APP_ICON_COLOR = "app_icon_color"
const val NAVIGATION_BAR_COLOR = "navigation_bar_color"
const val DEFAULT_NAVIGATION_BAR_COLOR = "default_navigation_bar_color"
const val LAST_HANDLED_SHORTCUT_COLOR = "last_handled_shortcut_color"
const val LAST_ICON_COLOR = "last_icon_color"
const val CUSTOM_TEXT_COLOR = "custom_text_color"
const val CUSTOM_BACKGROUND_COLOR = "custom_background_color"
const val CUSTOM_PRIMARY_COLOR = "custom_primary_color"
const val CUSTOM_ACCENT_COLOR = "custom_accent_color"
const val CUSTOM_NAVIGATION_BAR_COLOR = "custom_navigation_bar_color"
const val CUSTOM_APP_ICON_COLOR = "custom_app_icon_color"
const val WIDGET_BG_COLOR = "widget_bg_color"
const val WIDGET_TEXT_COLOR = "widget_text_color"
const val PASSWORD_PROTECTION = "password_protection"
const val PASSWORD_HASH = "password_hash"
const val PROTECTION_TYPE = "protection_type"
const val APP_PASSWORD_PROTECTION = "app_password_protection"
const val APP_PASSWORD_HASH = "app_password_hash"
const val APP_PROTECTION_TYPE = "app_protection_type"
const val DELETE_PASSWORD_PROTECTION = "delete_password_protection"
const val DELETE_PASSWORD_HASH = "delete_password_hash"
const val DELETE_PROTECTION_TYPE = "delete_protection_type"
const val PROTECTED_FOLDER_PATH = "protected_folder_path_"
const val PROTECTED_FOLDER_HASH = "protected_folder_hash_"
const val PROTECTED_FOLDER_TYPE = "protected_folder_type_"
const val KEEP_LAST_MODIFIED = "keep_last_modified"
const val USE_ENGLISH = "use_english"
const val WAS_USE_ENGLISH_TOGGLED = "was_use_english_toggled"
const val WAS_SHARED_THEME_EVER_ACTIVATED = "was_shared_theme_ever_activated"
const val IS_USING_SHARED_THEME = "is_using_shared_theme"
const val IS_USING_AUTO_THEME = "is_using_auto_theme"
const val IS_USING_SYSTEM_THEME = "is_using_system_theme"
const val SHOULD_USE_SHARED_THEME = "should_use_shared_theme"
const val WAS_SHARED_THEME_FORCED = "was_shared_theme_forced"
const val WAS_CUSTOM_THEME_SWITCH_DESCRIPTION_SHOWN = "was_custom_theme_switch_description_shown"
const val SHOW_INFO_BUBBLE = "show_info_bubble"
const val LAST_CONFLICT_RESOLUTION = "last_conflict_resolution"
const val LAST_CONFLICT_APPLY_TO_ALL = "last_conflict_apply_to_all"
const val HAD_THANK_YOU_INSTALLED = "had_thank_you_installed"
const val SKIP_DELETE_CONFIRMATION = "skip_delete_confirmation"
const val ENABLE_PULL_TO_REFRESH = "enable_pull_to_refresh"
const val SCROLL_HORIZONTALLY = "scroll_horizontally"
const val PREVENT_PHONE_FROM_SLEEPING = "prevent_phone_from_sleeping"
const val LAST_USED_VIEW_PAGER_PAGE = "last_used_view_pager_page"
const val USE_24_HOUR_FORMAT = "use_24_hour_format"
const val SUNDAY_FIRST = "sunday_first"
const val WAS_ALARM_WARNING_SHOWN = "was_alarm_warning_shown"
const val WAS_REMINDER_WARNING_SHOWN = "was_reminder_warning_shown"
const val USE_SAME_SNOOZE = "use_same_snooze"
const val SNOOZE_TIME = "snooze_delay"
const val VIBRATE_ON_BUTTON_PRESS = "vibrate_on_button_press"
const val YOUR_ALARM_SOUNDS = "your_alarm_sounds"
const val SILENT = "silent"
const val OTG_PARTITION = "otg_partition_2"
const val IS_USING_MODIFIED_APP_ICON = "is_using_modified_app_icon"
const val INITIAL_WIDGET_HEIGHT = "initial_widget_height"
const val WIDGET_ID_TO_MEASURE = "widget_id_to_measure"
const val WAS_ORANGE_ICON_CHECKED = "was_orange_icon_checked"
const val WAS_APP_ON_SD_SHOWN = "was_app_on_sd_shown"
const val WAS_BEFORE_ASKING_SHOWN = "was_before_asking_shown"
const val WAS_BEFORE_RATE_SHOWN = "was_before_rate_shown"
const val WAS_INITIAL_UPGRADE_TO_PRO_SHOWN = "was_initial_upgrade_to_pro_shown"
const val WAS_APP_ICON_CUSTOMIZATION_WARNING_SHOWN = "was_app_icon_customization_warning_shown"
const val APP_SIDELOADING_STATUS = "app_sideloading_status"
const val DATE_FORMAT = "date_format"
const val WAS_OTG_HANDLED = "was_otg_handled_2"
const val WAS_UPGRADED_FROM_FREE_SHOWN = "was_upgraded_from_free_shown"
const val WAS_RATE_US_PROMPT_SHOWN = "was_rate_us_prompt_shown"
const val WAS_APP_RATED = "was_app_rated"
const val WAS_SORTING_BY_NUMERIC_VALUE_ADDED = "was_sorting_by_numeric_value_added"
const val WAS_FOLDER_LOCKING_NOTICE_SHOWN = "was_folder_locking_notice_shown"
const val LAST_RENAME_USED = "last_rename_used"
const val LAST_RENAME_PATTERN_USED = "last_rename_pattern_used"
const val LAST_EXPORTED_SETTINGS_FOLDER = "last_exported_settings_folder"
const val LAST_EXPORTED_SETTINGS_FILE = "last_exported_settings_file"
const val LAST_BLOCKED_NUMBERS_EXPORT_PATH = "last_blocked_numbers_export_path"
const val FONT_SIZE = "font_size"
const val WAS_MESSENGER_RECORDER_SHOWN = "was_messenger_recorder_shown"
const val DEFAULT_TAB = "default_tab"
const val START_NAME_WITH_SURNAME = "start_name_with_surname"
const val FAVORITES = "favorites"
const val SHOW_CALL_CONFIRMATION = "show_call_confirmation"
internal const val COLOR_PICKER_RECENT_COLORS = "color_picker_recent_colors"

// shared prefs
const val SPEED_DIAL = "speed_dial"
const val REMEMBER_SIM_PREFIX = "remember_sim_"
const val GROUP_SUBSEQUENT_CALLS = "group_subsequent_calls"
const val DISABLE_PROXIMITY_SENSOR = "disable_proximity_sensor"
const val DISABLE_SWIPE_TO_ANSWER = "disable_swipe_to_answer"
private const val PATH = "com.simplemobiletools.dialer.action."
const val ACCEPT_CALL = PATH + "accept_call"
const val DECLINE_CALL = PATH + "decline_call"

//caller id settings prefs keys
const val BLOCK_UNKNOWN_NUMBERS = "block_unknown_numbers"
const val BLOCK_TOP_SPAMMERS = "block_top_spammers"
const val PRIVATE_HIDDEN_NUBERS = "private_hidden_numbers"
const val BLOCK_INTERNATIONAL_CALLS = "block_international_calls"
const val BLOCKED_COUNTRY_CODES_LIST = "blockED_COUNTRY_CODES_LIST"
const val SHOW_MISSED_CALLS_NOTIFICATIONS = "Show_Missed_Calls_Notifications"
const val SHOW_CALLER_ID_SETTINGS = "show_Caller_Id_Setting"
const val SHOW_PHONEBOOK_CONTACTS = "show_Phonebook_Contacts"
const val SHOW_AFTER_CALL_DETAIL = "Show_After_Call_Detail"
const val POPUP_POSITION_SETTINGS = "popup_Position"
const val CALLER_ID_SCREEN_SIZE_SETTING = " caller_is_screen_size_prefs"

const val SHOW_MESSAGE_ALERT_SETTINGS = "show_message_alert_notification"
const val SHOW_CALL_ALERT_SETTINGS = "show_call_Alert_Setting"
const val SHOW_MISSCALL_NOTIFICATION_SETTINGS = "show_MissedCall_Notification_Setting"
const val REMIND_ME_OF_MISCALLS = "remind_Me_Of_Miscalls"
const val VIBRATE_ON_MESSAGE_SETTING = "vibrate_On_Message_setting"

const val TIMEANDDATENOTIFICATION = "TIME_AND_DATE_NOTIFICATION"
const val PREVIOUS_INCOMMING_NUMBER = "PREVIOUS_INCOMMING_NUMBER"
const val PREVIOUS_INCOMMING_NUMBER_TYPE = "PREVIOUS_INCOMMING_NUMBER_TYPE"


// global intents
const val REQUEST_SET_AS = 1004
const val REQUEST_EDIT_IMAGE = 1005
const val REQUEST_CODE_SET_DEFAULT_DIALER = 1007

// sorting
const val SORT_ORDER = "sort_order"
const val SORT_FOLDER_PREFIX =
    "sort_folder_"       // storing folder specific values at using "Use for this folder only"
const val SORT_BY_NAME = 1
const val SORT_BY_DATE_MODIFIED = 2
const val SORT_BY_SIZE = 4
const val SORT_BY_DATE_TAKEN = 8
const val SORT_BY_EXTENSION = 16
const val SORT_BY_PATH = 32
const val SORT_BY_NUMBER = 64
const val SORT_BY_FIRST_NAME = 128
const val SORT_BY_MIDDLE_NAME = 256
const val SORT_BY_SURNAME = 512
const val SORT_DESCENDING = 1024
const val SORT_BY_TITLE = 2048
const val SORT_BY_ARTIST = 4096
const val SORT_BY_DURATION = 8192
const val SORT_BY_RANDOM = 16384
const val SORT_USE_NUMERIC_VALUE = 32768
const val SORT_BY_FULL_NAME = 65536
const val SORT_BY_CUSTOM = 131072
const val SORT_BY_DATE_CREATED = 262144

// security
const val WAS_PROTECTION_HANDLED = "was_protection_handled"
const val PROTECTION_NONE = -1
const val PROTECTION_PATTERN = 0
const val PROTECTION_PIN = 1
const val PROTECTION_FINGERPRINT = 2

// renaming
const val RENAME_SIMPLE = 0
const val RENAME_PATTERN = 1

const val SHOW_ALL_TABS = -1
const val SHOW_PATTERN = 0
const val SHOW_PIN = 1
const val SHOW_FINGERPRINT = 2

// permissions
const val PERMISSION_READ_STORAGE = 1
const val PERMISSION_WRITE_STORAGE = 2
const val PERMISSION_CAMERA = 3
const val PERMISSION_RECORD_AUDIO = 4
const val PERMISSION_READ_CONTACTS = 5
const val PERMISSION_WRITE_CONTACTS = 6
const val PERMISSION_READ_CALENDAR = 7
const val PERMISSION_WRITE_CALENDAR = 8
const val PERMISSION_CALL_PHONE = 9
const val PERMISSION_READ_CALL_LOG = 10
const val PERMISSION_WRITE_CALL_LOG = 11
const val PERMISSION_GET_ACCOUNTS = 12
const val PERMISSION_READ_SMS = 13
const val PERMISSION_SEND_SMS = 14
const val PERMISSION_READ_PHONE_STATE = 15
const val PERMISSION_MEDIA_LOCATION = 16
const val PERMISSION_POST_NOTIFICATIONS = 17
const val PERMISSION_READ_MEDIA_IMAGES = 18
const val PERMISSION_READ_MEDIA_VIDEO = 19
const val PERMISSION_READ_MEDIA_AUDIO = 20
const val PERMISSION_ACCESS_FINE_LOCATION = 16

// conflict resolving
const val CONFLICT_SKIP = 1
const val CONFLICT_OVERWRITE = 2
const val CONFLICT_MERGE = 3
const val CONFLICT_KEEP_BOTH = 4

// font sizes
const val FONT_SIZE_SMALL = 0
const val FONT_SIZE_MEDIUM = 1
const val FONT_SIZE_LARGE = 2
const val FONT_SIZE_EXTRA_LARGE = 3

const val MONDAY_BIT = 1
const val TUESDAY_BIT = 2
const val WEDNESDAY_BIT = 4
const val THURSDAY_BIT = 8
const val FRIDAY_BIT = 16
const val SATURDAY_BIT = 32
const val SUNDAY_BIT = 64
const val EVERY_DAY_BIT =
    MONDAY_BIT or TUESDAY_BIT or WEDNESDAY_BIT or THURSDAY_BIT or FRIDAY_BIT or SATURDAY_BIT or SUNDAY_BIT
const val WEEK_DAYS_BIT = MONDAY_BIT or TUESDAY_BIT or WEDNESDAY_BIT or THURSDAY_BIT or FRIDAY_BIT
const val WEEKENDS_BIT = SATURDAY_BIT or SUNDAY_BIT

const val SIDELOADING_UNCHECKED = 0
const val SIDELOADING_TRUE = 1
const val SIDELOADING_FALSE = 2

// default tabs
const val TAB_LAST_USED = 0
const val TAB_CONTACTS = 1
const val TAB_FAVORITES = 2
const val TAB_CALL_HISTORY = 4
const val TAB_GROUPS = 8
const val TAB_FILES = 16
const val TAB_RECENT_FILES = 32
const val TAB_STORAGE_ANALYSIS = 64

val photoExtensions: Array<String>
    get() = arrayOf(
        ".jpg",
        ".png",
        ".jpeg",
        ".bmp",
        ".webp",
        ".heic",
        ".heif",
        ".apng",
        ".avif"
    )
val videoExtensions: Array<String>
    get() = arrayOf(
        ".mp4",
        ".mkv",
        ".webm",
        ".avi",
        ".3gp",
        ".mov",
        ".m4v",
        ".3gpp"
    )
val audioExtensions: Array<String>
    get() = arrayOf(
        ".mp3",
        ".wav",
        ".wma",
        ".ogg",
        ".m4a",
        ".opus",
        ".flac",
        ".aac"
    )
val rawExtensions: Array<String>
    get() = arrayOf(
        ".dng",
        ".orf",
        ".nef",
        ".arw",
        ".rw2",
        ".cr2",
        ".cr3"
    )

val extensionsSupportingEXIF: Array<String>
    get() = arrayOf(
        ".jpg",
        ".jpeg",
        ".png",
        ".webp",
        ".dng"
    )

const val DATE_FORMAT_ONE = "dd.MM.yyyy"
const val DATE_FORMAT_TWO = "dd/MM/yyyy"
const val DATE_FORMAT_THREE = "MM/dd/yyyy"
const val DATE_FORMAT_FOUR = "yyyy-MM-dd"
const val DATE_FORMAT_FIVE = "d MMMM yyyy"
const val DATE_FORMAT_SIX = "MMMM d yyyy"
const val DATE_FORMAT_SEVEN = "MM-dd-yyyy"
const val DATE_FORMAT_EIGHT = "dd-MM-yyyy"
const val DATE_FORMAT_NINE = "yyyyMMdd"
const val DATE_FORMAT_TEN = "yyyy.MM.dd"
const val DATE_FORMAT_ELEVEN = "yy-MM-dd"
const val DATE_FORMAT_TWELVE = "yyMMdd"
const val DATE_FORMAT_THIRTEEN = "yy.MM.dd"
const val DATE_FORMAT_FOURTEEN = "yy/MM/dd"
const val DATE_FORMAT_NEW = "d MMMM"

const val TIME_FORMAT_12 = "hh:mm a"
const val TIME_FORMAT_24 = "HH:mm"

// possible icons at the top left corner
enum class NavigationIcon {
    Cross,
    Arrow,
    None
}

val appIconColorStrings = arrayListOf(
    ".Red",
    ".Pink",
    ".Purple",
    ".Deep_purple",
    ".Indigo",
    ".Blue",
    ".Light_blue",
    ".Cyan",
    ".Teal",
    ".Green",
    ".Light_green",
    ".Lime",
    ".Yellow",
    ".Amber",
    ".Orange",
    ".Deep_orange",
    ".Brown",
    ".Blue_grey",
    ".Grey_black"
)

// most app icon colors from md_app_icon_colors with reduced alpha
// used at showing contact placeholders without image
val letterBackgroundColors = arrayListOf(
    0xCCD32F2F,
    0xCCC2185B,
    0xCC1976D2,
    0xCC0288D1,
    0xCC0097A7,
    0xCC00796B,
    0xCC388E3C,
    0xCC689F38,
    0xCCF57C00,
    0xCCE64A19
)
val contactBackgroundImagesIds = arrayListOf(
    R.drawable.user_icon1,
    R.drawable.user_icon1,
    R.drawable.user_icon1,
    R.drawable.user_icon1,
    R.drawable.user_icon1,
    R.drawable.user_icon1,
)

// view types
const val VIEW_TYPE_GRID = 1
const val VIEW_TYPE_LIST = 2

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()

fun ensureBackgroundThread(callback: () -> Unit) {
    if (isOnMainThread()) {
        Thread {
            callback()
        }.start()
    } else {
        callback()
    }
}

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
fun isMarshmallowPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
fun isNougatPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
fun isNougatMR1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
fun isOreoPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
fun isOreoMr1Plus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
fun isPiePlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isQPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
fun isRPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isSPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
fun isTiramisuPlus() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun getDateFormats() = arrayListOf(
    "--MM-dd",
    "yyyy-MM-dd",
    "yyyyMMdd",
    "yyyy.MM.dd",
    "yy-MM-dd",
    "yyMMdd",
    "yy.MM.dd",
    "yy/MM/dd",
    "MM-dd",
    "MMdd",
    "MM/dd",
    "MM.dd"
)

fun getDateFormatsWithYear() = arrayListOf(
    DATE_FORMAT_FOUR,
    DATE_FORMAT_NINE,
    DATE_FORMAT_TEN,
    DATE_FORMAT_ELEVEN,
    DATE_FORMAT_TWELVE,
    DATE_FORMAT_THIRTEEN,
    DATE_FORMAT_FOURTEEN,
)

val normalizeRegex = "\\p{InCombiningDiacriticalMarks}+".toRegex()

fun getConflictResolution(resolutions: LinkedHashMap<String, Int>, path: String): Int {
    return if (resolutions.size == 1 && resolutions.containsKey("")) {
        resolutions[""]!!
    } else if (resolutions.containsKey(path)) {
        resolutions[path]!!
    } else {
        CONFLICT_SKIP
    }
}

val proPackages = arrayListOf("draw", "gallery", "filemanager", "contacts", "notes", "calendar")

fun mydebug(message: String) = Log.e("DEBUG", message)

fun getQuestionMarks(size: Int) = ("?," * size).trimEnd(',')

fun Int.getContrastColor(): Int {
    val y = (299 * Color.red(this) + 587 * Color.green(this) + 114 * Color.blue(this)) / 1000
    return if (y >= 149 && this != Color.BLACK) DARK_GREY else Color.WHITE
}



@SuppressLint("UseCompatLoadingForDrawables")
fun Context.getImage(name: String?): Drawable? {
    return this.resources.getDrawable(
        this.resources.getIdentifier(
            name,
            "drawable",
            this.packageName
        )
    )
}
