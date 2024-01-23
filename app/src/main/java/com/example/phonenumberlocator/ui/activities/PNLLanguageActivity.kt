package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLang
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.AdsConsentManager
import com.example.phonenumberlocator.admob_ads.interstitialAdPriority
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.admob_ads.showSplashInterstitial
import com.example.phonenumberlocator.databinding.ActivityPnllanguageBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlSharedPreferencesLang.PNLMySharePreferences
import com.example.phonenumberlocator.pnlUtil.changeLanguage
import com.example.phonenumberlocator.pnlUtil.refreshLanguageStrings
import com.example.phonenumberlocator.ui.activities.helpScreens.PNLIntroSliderActivity
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


        langName = baseConfig.appLanguage
        langName?.let { updateLanguageSelection(it) }
        //binding.clEnglish.background = resources.getDrawable(R.drawable.drawablestroke)

        initListeners()

        if(AdsConsentManager.getConsentResult(this)){

            showAd()
        }else{

        }

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
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun handleAds() {
        if (isNetworkAvailable()) {
            val lang = intent.getBooleanExtra("setting", false)
            if (!lang) {
                showSplashInterstitial()
            } else {
                loadAndReturnAd(
                    this@PNLLanguageActivity, resources.getString(R.string.admob_native_lang_low)
                ) { it2 ->
                    if (it2 != null) {
                        showLoadedNativeAd(
                            this,
                            binding.ads,
                            R.layout.native_large_2, it2
                        )
                    } else {
                        binding.ads.beGone()
                    }

                }
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
        Log.d(
            "LanguageDebug",
            "Selected Language Name: $langName, Lowercase Language Name: $lowercaseLangName"
        )
        val langIndex = langNameIndex(lowercaseLangName)
        clIds.forEach { it.background = null }
        clIds[langIndex].background =
            resources.getDrawable(R.drawable.drawablestroke)
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

        if (!baseConfig.appStarted) {
            baseConfig.appStarted = true
            startActivity(Intent(this@PNLLanguageActivity, PNLIntroSliderActivity::class.java))
            finish()
        } else {
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
        if (isNetworkAvailable()) {
            binding.ads.visibility = View.VISIBLE
            nativeAdLang.observe(this) {
                showLoadedNativeAd(this, binding.ads, R.layout.native_large_2, it)
            }
        } else {
            binding.ads.visibility = View.GONE
        }
    }
}
