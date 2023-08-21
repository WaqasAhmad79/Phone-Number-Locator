package com.example.phonenumberlocator.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.BuildCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnllanguageBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import com.example.phonenumberlocator.pnlUtil.changeLanguage
import com.example.phonenumberlocator.pnlUtil.refreshLanguageStrings
import com.example.phonenumberlocator.ui.MainActivity
import com.example.phonenumberlocator.ui.pnlDialog.PNLResumeLoadingDialog
import com.example.tracklocation.tlSharedPreferencesLang.PNLMySharePreferences
import java.util.Locale


class PNLLanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnllanguageBinding
    private var lang: String = "English"
    private val mySharePreferences = PNLMySharePreferences(this@PNLLanguageActivity)
    private var langName: String? = null

//    private var dialog: PNLResumeLoadingDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnllanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val lang =intent.getBooleanExtra("setting",false )



       /* dialog = PNLResumeLoadingDialog(this)
        if (isNetworkAvailable() && lang) {
            dialog?.show()
            showHighSplashAdmobInterstitial({}, {
                showLowSplashAdmobInterstitial({ }, { dialog?.dismiss() }, {
                    Handler().postDelayed({
                        dialog?.dismiss()
                    }, 1000)
                })
            }, {
                Handler().postDelayed({
                    dialog?.dismiss()
                }, 1000)
            })

        }

        loadAd()
*/
        langName = baseConfig.appLanguage
        langName?.let { updateLanguageSelection(it) }
        //binding.clEnglish.background = resources.getDrawable(R.drawable.drawablestroke)

        initListeners()

        binding.tick.setOnClickListener {
            when (langName) {
                "en"->    setLocaleAndChangeLanguage("en")
                "hi"    ->        setLocaleAndChangeLanguage("hi")
                "ar"       -> setLocaleAndChangeLanguage("pt")
                "af" -> setLocaleAndChangeLanguage("es")
                "pt"   -> setLocaleAndChangeLanguage("af")
                "es"    -> setLocaleAndChangeLanguage("ar")
                "fr"   -> setLocaleAndChangeLanguage("fr")
                "ur"-> setLocaleAndChangeLanguage("ur")
                "in"     -> setLocaleAndChangeLanguage("in")
                "ru"-> setLocaleAndChangeLanguage("ru")
                "vi"    -> setLocaleAndChangeLanguage("vi")
                "zh" -> setLocaleAndChangeLanguage("zh")
                "de"  -> setLocaleAndChangeLanguage("de")
                "ja"  -> setLocaleAndChangeLanguage("ja")
                "ko"  -> setLocaleAndChangeLanguage("ko")
                "th" -> setLocaleAndChangeLanguage("th")
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



        clIds.forEach { it.background = null }
        clIds[langNameIndex(selectedLanguage)].background =
            resources.getDrawable(R.drawable.drawablestroke)
    }

    private fun langNameIndex(language: String): Int =
        listOf(
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
        ).indexOf(language)

    private fun setLocaleAndChangeLanguage(language: String) {
        baseConfig.appLanguage = language
        setLocale(language)
        changeLanguage(language)
        refreshLanguageStrings()

        if (!baseConfig.appStarted) {
            baseConfig.appStarted = true
            startActivity(Intent(this@PNLLanguageActivity, MainActivity::class.java))
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
    /* private fun loadAd() {
        if (isNetworkAvailable()) {
            binding.ads.visibility = View.VISIBLE
            PhoneNumberLocator.instance.nativeAdLang.observe(this){
                showLoadedNativeAd(this,binding.ads, R.layout.native_large_2,it)
            }
        } else {
            binding.ads.visibility = View.GONE
        }
    }*/
}
