package com.example.phonenumberlocator.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.core.os.BuildCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnllanguageBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlUtil.changeLanguage
import com.example.phonenumberlocator.pnlUtil.refreshLanguageStrings
import com.example.phonenumberlocator.ui.MainActivity
import com.example.tracklocation.tlSharedPreferencesLang.PNLMySharePreferences
import com.example.phonenumberlocator.pnlUtil.PNLCheckInternetConnection
import java.util.Locale

class PNLLanguageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnllanguageBinding
    var lang: String = "English"
    val mySharePreferences = PNLMySharePreferences(this)
    var started = false

    lateinit var checkInternetConnection: PNLCheckInternetConnection
    var buttonsIds: ArrayList<RadioButton> = arrayListOf()
//    private var dialog: PNLResumeLoadingDialog?=null

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lang =intent.getBooleanExtra("setting",false )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(R.color.app_color)
        }
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        binding = ActivityPnllanguageBinding.inflate(layoutInflater)
        loadLocale()
        setContentView(binding.root)
       /* dialog = TLResumeLoadingDialog(this)
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

        loadAd()*/

        checkInternetConnection = PNLCheckInternetConnection(this)
        initListeners()
        if (BuildCompat.isAtLeastT()) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                finish()


            }
        } else {
            onBackPressedDispatcher.addCallback(
                this, // lifecycle owner
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        finish()

                    }
                })
        }
    }

    private fun initListeners() {
        binding.tvTitle.text = resources.getString(R.string.language)
        binding.tick.setOnClickListener {
            when (lang) {
                "English" -> {
                    baseConfig.appLanguage = "en"
                    next()

                }

                "Hindi" -> {
                    baseConfig.appLanguage = "hi"
                    next()

                }

                "Spanish" -> {
                    baseConfig.appLanguage = "es"
                    next()

                }

                "French" -> {
                    baseConfig.appLanguage = "fr"
                    next()

                }

                "Arabic" -> {
                    baseConfig.appLanguage = "ar"
                    next()

                }

                "Urdu" -> {
                    baseConfig.appLanguage = "ur"
                    next()

                }

                "Indonesia" -> {
                    baseConfig.appLanguage = "in"
                    next()

                }

                "Russia" -> {
                    baseConfig.appLanguage = "ru"
                    next()

                }

                "Afrikaans" -> {
                    baseConfig.appLanguage = "af"
                    next()

                }

                "Vietnamese" -> {
                    baseConfig.appLanguage = "vi"
                    next()

                }
                "Chinese" -> {
                    baseConfig.appLanguage = "zh"
                    next()

                }

                "German" -> {
                    baseConfig.appLanguage = "de"
                    next()

                }

                "Japanese" -> {
                    baseConfig.appLanguage = "ja"
                    next()

                }

                "Korean" -> {
                    baseConfig.appLanguage = "ko"
                    next()

                }

                "Thai" -> {
                    baseConfig.appLanguage = "th"
                    next()

                }
                "Portuguese" -> {
                    baseConfig.appLanguage = "pt"
                    next()

                }

            }
        }

        buttonsIds = arrayListOf(
            binding.english,
            binding.hindi,
            binding.spanish,
            binding.french,
            binding.arabic,
            binding.urdu,
            binding.indonesia,
            binding.russia,
            binding.african,
            binding.vietnamese,
            binding.china,
            binding.german,
            binding.japanese,
            binding.korean,
            binding.thailand ,
            binding.portuguese
        )

        buttonsIds.forEachIndexed { index, mRadioButton ->
            mRadioButton.setOnClickListener {
                mRadioButton.isChecked = true
                lang = mRadioButton.text.toString()
                buttonsIds.forEachIndexed { index, radioButton ->
                    if (radioButton.id != mRadioButton.id) {
                        radioButton.isChecked = false
                    }
                }

            }

        }
        arrayListOf(
            binding.clEnglish,
            binding.clHindi,
            binding.clSpanish,
            binding.clFrench,
            binding.clArabic,
            binding.clUrdu,
            binding.clIndonesia,
            binding.clRussia,
            binding.clAfrican,
            binding.clVietnamese,
            binding.clChina,
            binding.clGermany,
            binding.clJapanese,
            binding.clKorean,
            binding.clThailand,
            binding.clPortuguese
        ).forEachIndexed { index, constraintLayout ->
            constraintLayout.setOnClickListener {
                handleRadioButton(buttonsIds[index])
            }
        }

    }

    private fun handleRadioButton(mButton: RadioButton) {
        buttonsIds.forEach { button ->
            if (button.id == mButton.id) {
                button.isChecked = true
                lang = button.text.toString()
            } else {
                button.isChecked = false
            }

        }

    }

    fun next() {
        changeLanguage(baseConfig.appLanguage.toString())
        refreshLanguageStrings()
        if (!baseConfig.appStarted) {
            baseConfig.appStarted = true
            startActivity(Intent(this, MainActivity::class.java).putExtra("LanguageActivity",true))
            finish()
            return
        }
        finish()
    }

    fun radio_button_click(view: View) {
        // Get the clicked radio button instance
//        val radio: RadioButton = findViewById(binding.radioGroup.checkedRadioButtonId)
//        Toast.makeText(applicationContext,"On click : ${radio.text}",
//            Toast.LENGTH_SHORT).show()
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

    private fun loadLocale() {
//        val preferences: SharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
//        val language = preferences.getString("app_lang", "")
        val language = baseConfig.appLanguage
        language?.let {
            setLocale(it)
            when (it) {
                "en" -> {
                    binding.english.isChecked = true
                }

                "hi" -> {
                    binding.hindi.isChecked = true

                }

                "es" -> {
                    binding.spanish.isChecked = true

                }

                "fr" -> {
                    binding.french.isChecked = true

                }

                "ar" -> {
                    binding.arabic.isChecked = true

                }

                "ur" -> {
                    binding.urdu.isChecked = true
                }

                "in" -> {
                    binding.indonesia.isChecked = true
                }
                "ru" -> {
                    binding.russia.isChecked = true
                }
                "af" -> {
                    binding.african.isChecked = true
                }
                "vi" -> {
                    binding.vietnamese.isChecked = true
                }

                "zh" -> {
                    binding.china.isChecked = true
                }

                "de" -> {
                    binding.german.isChecked = true
                }
                "ja" -> {
                    binding.japanese.isChecked = true
                }
                "ko" -> {
                    binding.korean.isChecked = true
                }
                "th" -> {
                    binding.thailand.isChecked = true
                }
                "pt" -> {
                    binding.portuguese.isChecked = true
                }
            }

        }
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