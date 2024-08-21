package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLang
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLangDup
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLangOther
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdWelcome
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.admob_ads.showNormalAdmobInterstitial
import com.example.phonenumberlocator.databinding.ActivityPnllanguageBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlSharedPreferencesLang.PNLMySharePreferences
import com.example.phonenumberlocator.pnlUtil.changeLanguage
import com.example.phonenumberlocator.pnlUtil.refreshLanguageStrings
import com.example.phonenumberlocator.ui.MainActivity
import java.util.Locale

class PNLLanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnllanguageBinding
    private var lang: String = "English"
    private val mySharePreferences = PNLMySharePreferences(this@PNLLanguageActivity)
    private var langName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnllanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleAds()

        if (PhoneNumberLocator.canRequestAd) {
            showAd()
        }

        langName = null
        initListeners()

        binding.tick.setOnClickListener {
            when (langName) {
                "en" -> setLocaleAndChangeLanguage("en")
                "hi" -> setLocaleAndChangeLanguage("hi")
                "ar" -> setLocaleAndChangeLanguage("ar")
                "af" -> setLocaleAndChangeLanguage("af")
                "pt" -> setLocaleAndChangeLanguage("pt")
                "es" -> setLocaleAndChangeLanguage("es")
                "fr" -> setLocaleAndChangeLanguage("fr")
                "ur" -> setLocaleAndChangeLanguage("ur")
                "in" -> setLocaleAndChangeLanguage("in")
                "ru" -> setLocaleAndChangeLanguage("ru")
                "vi" -> setLocaleAndChangeLanguage("vi")
                "zh" -> setLocaleAndChangeLanguage("zh")
                "de" -> setLocaleAndChangeLanguage("de")
                "ja" -> setLocaleAndChangeLanguage("ja")
                "ko" -> setLocaleAndChangeLanguage("ko")
                "th" -> setLocaleAndChangeLanguage("th")
                else -> {
                    Toast.makeText(
                        this@PNLLanguageActivity,
                        "Please select a language first",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }


    private fun handleAds() {
        if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
            val lang = intent.getBooleanExtra( // if true it means that we are coming from settings
                "setting",
                false
            )
            if (!lang) {
                if (RemoteConfigClass.inter_pnl_language_activity) {
                    showNormalAdmobInterstitial()
                }


            } else {
                if (RemoteConfigClass.native_language) {
                    loadAndReturnAd(
                        this@PNLLanguageActivity,
                        resources.getString(R.string.admob_native_lang_low)
                    ) { it2 ->
                        if (it2 != null) {
                            if (RemoteConfigClass.native_language && PhoneNumberLocator.canRequestAd) {
                                showLoadedNativeAd(
                                    this,
                                    binding.ads,
                                    binding.includeShimmer.shimmerContainerNative,
                                    R.layout.native_large_2,
                                    it2
                                )
                            }
                        } else {
                            binding.ads.beGone()
                        }
                    }
                }
            }


            // Native language duplicate ad load first screen
            if (RemoteConfigClass.native_dup_language_activity) {
                loadAndReturnAd(
                    this@PNLLanguageActivity, resources.getString(R.string.admob_native_lang_low)
                ) { it1 ->
                    Log.d("Native_dup_language_ad ", "value: $it1")
                    if (it1 != null) {
                        nativeAdLangDup.value = it1
                    }
                }
            } else {
                nativeAdLangDup.value = null
            }


            // Native first ad load for welcome screen (native_welcome_screen_activity)
            if (!lang) { //  don't load the welcome screen ad if coming from the settings
                if (!baseConfig.isAppIntroComplete) { // don't load the welcome screen ad if app intro is complete i.e second run of app
                    if (RemoteConfigClass.native_welcome_screen_activity) {
                        loadAndReturnAd(
                            this@PNLLanguageActivity,
                            resources.getString(R.string.admob_native_large)
                        ) { it1 ->
                            Log.d("nativeAdWelcome ", "value: $it1")
                            if (it1 != null) {
                                nativeAdWelcome.value = it1
                            }
                        }
                    } else {
                        nativeAdWelcome.value = null
                    }
                }
            }

            // Native language other Ad load second language screen
            if (RemoteConfigClass.native_language_other_activity) {
                loadAndReturnAd(
                    this@PNLLanguageActivity, resources.getString(R.string.admob_native_lang_low)
                ) { it1 ->
                    if (it1 != null) {
                        nativeAdLangOther.value = it1
                    }
                }
            } else {
                nativeAdLangOther.value = null
            }


        }
    }

    private fun initListeners() {
        binding.english.setOnClickListener { updateLanguageSelection("en") }
        binding.espanol.setOnClickListener { updateLanguageSelection("es") }
        binding.hindi.setOnClickListener { updateLanguageSelection("hi") }
        binding.arabic.setOnClickListener { updateLanguageSelection("ar") }
        binding.afrikaans.setOnClickListener { updateLanguageSelection("af") }
        binding.portuguese.setOnClickListener { updateLanguageSelection("pt") }
        binding.french.setOnClickListener { updateLanguageSelection("fr") }
        binding.urdu.setOnClickListener { updateLanguageSelection("ur") }
        binding.indonesia.setOnClickListener { updateLanguageSelection("in") }
        binding.russian.setOnClickListener { updateLanguageSelection("ru") }
        binding.vietnamese.setOnClickListener { updateLanguageSelection("vi") }
        binding.china.setOnClickListener { updateLanguageSelection("zh") }
        binding.german.setOnClickListener { updateLanguageSelection("de") }
        binding.japanese.setOnClickListener { updateLanguageSelection("ja") }
        binding.korean.setOnClickListener { updateLanguageSelection("ko") }
        binding.thai.setOnClickListener { updateLanguageSelection("th") }
    }

    private fun updateLanguageSelection(selectedLanguage: String) {
        val clIds = listOf(
            binding.clEnglish,
            binding.clHindi,
            binding.clArabic,
            binding.clAfrikaans,
            binding.clPortuguese,
            binding.clEspanol,
            binding.clFrench,
            binding.clUrdu,
            binding.clIndonesian,
            binding.clRussian,
            binding.clVietnamese,
            binding.clChina,
            binding.clGerman,
            binding.clJapanese,
            binding.clKorean,
            binding.clThai
        )

        langName = selectedLanguage
        val lowercaseLangName = langName?.toLowerCase(Locale.ROOT) ?: ""
        val langIndex = langNameIndex(lowercaseLangName)
        clIds.forEach { it.background = null }

        clIds[langIndex].background =
            resources.getDrawable(R.drawable.drawablestroke)

        // Show the second ad when a language is selected
        showSecondAdDup()
    }

    private fun langNameIndex(language: String): Int {
        val languageCodes = listOf(
            "en",
            "hi",
            "ar",
            "af",
            "pt",
            "es",
            "fr",
            "ur",
            "in",
            "ru",
            "vi",
            "zh",
            "de",
            "ja",
            "ko",
            "th"
        )
        val index = languageCodes.indexOf(language)
        return if (index >= 0) index else 0 // Fallback to the first language if not found
    }

    private fun setLocaleAndChangeLanguage(language: String) {
        baseConfig.appLanguage = language
        setLocale(language)
        changeLanguage(language)
        refreshLanguageStrings()
        if (language == "en" || language == "hi") {

            // Start PNLLanguageDuplicateActivity activity
            val intent = Intent(this@PNLLanguageActivity, PNLLanguageDuplicateActivity::class.java)
            intent.putExtra("selectedLanguage", language)
            startActivity(intent)

        } else if (!baseConfig.appStarted) {
            startActivity(Intent(this@PNLLanguageActivity, WelcomeScreenActivity::class.java))
            finish()
        } else {
            startActivity(
                Intent(
                    this@PNLLanguageActivity,
                    MainActivity::class.java
                )
            )
            finish()
        }

    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(
            configuration,
            baseContext.resources.displayMetrics
        )

        val editor: SharedPreferences.Editor = getSharedPreferences("Settings", MODE_PRIVATE).edit()
        editor.putString("app_lang", language)
        editor.apply()
    }

    private fun showAd() {
        if (RemoteConfigClass.native_language) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.visibility = View.VISIBLE
                nativeAdLang.observe(this) {
                    showLoadedNativeAd(
                        this,
                        binding.ads,
                        binding.includeShimmer.shimmerContainerNative,
                        R.layout.native_large_2,
                        it
                    )
                }
            }
        } else {
            binding.ads.visibility = View.GONE
        }
    }

    private fun showSecondAdDup() {
        if (RemoteConfigClass.native_dup_language_activity) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.visibility = View.VISIBLE
                nativeAdLangDup.observe(this) { nativeAd ->
                    showLoadedNativeAd(
                        this,
                        binding.ads,
                        binding.includeShimmer.shimmerContainerNative,
                        R.layout.native_large_2,
                        nativeAd
                    )
                }
            }
        } else {
            binding.ads.beGone()
        }
    }
}
