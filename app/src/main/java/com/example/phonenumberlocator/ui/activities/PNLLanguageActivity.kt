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
        langName = "English"
        binding.clEnglish.background = resources.getDrawable(R.drawable.drawablestroke)

        initListeners()

        binding.tick.setOnClickListener {
            when (langName) {
                "English" -> setLocaleAndChangeLanguage("en")
                "Hindi" -> setLocaleAndChangeLanguage("hi")
                "Portuguese" -> setLocaleAndChangeLanguage("pt")
                "Spanish" -> setLocaleAndChangeLanguage("es")
                "Afrikaans" -> setLocaleAndChangeLanguage("af")
                "Arabic" -> setLocaleAndChangeLanguage("ar")
                "French" -> setLocaleAndChangeLanguage("fr")
                "Urdu" -> setLocaleAndChangeLanguage("ur")
                "Indonesian" -> setLocaleAndChangeLanguage("in")
                "Russian" -> setLocaleAndChangeLanguage("ru")
                "Vietnamese" -> setLocaleAndChangeLanguage("vi")
                "Chinese" -> setLocaleAndChangeLanguage("zh")
                "German" -> setLocaleAndChangeLanguage("de")
                "Japanese" -> setLocaleAndChangeLanguage("ja")
                "Korean" -> setLocaleAndChangeLanguage("ko")
                "Thai" -> setLocaleAndChangeLanguage("th")
            }
        }
    }

    private fun initListeners() {
        binding.english.setOnClickListener { updateLanguageSelection("English") }
        binding.espanol.setOnClickListener { updateLanguageSelection("Spanish") }
        binding.hindi.setOnClickListener { updateLanguageSelection("Hindi") }
        binding.arabic.setOnClickListener { updateLanguageSelection("Arabic") }
        binding.afrikaans.setOnClickListener { updateLanguageSelection("Afrikaans") }
        binding.portuguese.setOnClickListener { updateLanguageSelection("Portuguese") }
        binding.french.setOnClickListener { updateLanguageSelection("French") }
        binding.urdu.setOnClickListener { updateLanguageSelection("Urdu") }
        binding.indonesia.setOnClickListener { updateLanguageSelection("Indonesian") }
        binding.russian.setOnClickListener { updateLanguageSelection("Russian") }
        binding.vietnamese.setOnClickListener { updateLanguageSelection("Vietnamese") }
        binding.china.setOnClickListener { updateLanguageSelection("Chinese") }
        binding.german.setOnClickListener { updateLanguageSelection("German") }
        binding.japanese.setOnClickListener { updateLanguageSelection("Japanese") }
        binding.korean.setOnClickListener { updateLanguageSelection("Korean") }
        binding.thai.setOnClickListener { updateLanguageSelection("Thai") }
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
            "English",
            "Hindi",
            "Arabic",
            "Afrikaans",
            "Portuguese",
            "Spanish",
            "French",
            "Urdu",
            "Indonesian",
            "Russian",
            "Vietnamese",
            "Chinese",
            "German",
            "Japanese",
            "Korean",
            "Thai"
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
