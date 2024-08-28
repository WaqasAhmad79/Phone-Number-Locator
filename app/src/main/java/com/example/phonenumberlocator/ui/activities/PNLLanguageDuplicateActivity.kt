package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.phonenumberlocator.PhoneNumberLocator
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLangOther
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.nativeAdLangOtherDup
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.loadAndReturnAd
import com.example.phonenumberlocator.admob_ads.showLoadedNativeAd
import com.example.phonenumberlocator.databinding.ActivityPnllanguageDuplicateBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.hideNavBar
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.ui.MainActivity
import java.util.Locale

class PNLLanguageDuplicateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPnllanguageDuplicateBinding
    private var langName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnllanguageDuplicateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleAds()
        initLanguageIdentifier()
        initListeners()
        hideNavBar()

    }

    private fun initLanguageIdentifier() {
        val selectedLanguage = intent.getStringExtra("selectedLanguage")
        val languageName = when (selectedLanguage) {
            "en" -> "English"
            "hi" -> "Hindi"
            else -> "Unknown"
        }

        binding.languageNameHeading.text = languageName

        if (selectedLanguage == "hi") {
            binding.apply {
                englishImg.setImageResource(R.drawable.india)
                afrikaansImg.setImageResource(R.drawable.india)
                portugueseImg.setImageResource(R.drawable.india)
                hindiImg.setImageResource(R.drawable.india)
                arabicImg.setImageResource(R.drawable.india)
                espanolImg.setImageResource(R.drawable.india)

                englishText.text = "Hindi"
                afrikaansText.text = "Marathi"
                portugueseText.text = "Telugu"
                hindiText.text = "Tamil"
                arabicText.text = "Odia"
                espanolText.text = "Malayalam"
            }
        }

    }

    private fun initListeners() {

        binding.apply {
            english.setOnClickListener { updateLanguageSelection("en") }
            espanol.setOnClickListener { updateLanguageSelection("es") }
            hindi.setOnClickListener { updateLanguageSelection("hi") }
            arabic.setOnClickListener { updateLanguageSelection("ar") }
            afrikaans.setOnClickListener { updateLanguageSelection("af") }
            portuguese.setOnClickListener { updateLanguageSelection("pt") }

            tick.setOnClickListener {

                if (langName != null) {
                    if (!baseConfig.appStarted) {

                        startActivity(
                            Intent(
                                this@PNLLanguageDuplicateActivity,
                                WelcomeScreenActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        startActivity(
                            Intent(
                                this@PNLLanguageDuplicateActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@PNLLanguageDuplicateActivity,
                        "Please select an option first",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }
    }

    private fun updateLanguageSelection(selectedLanguage: String) {
        val clIds: List<ConstraintLayout>
        binding.apply {
            clIds = listOf(

                clEnglish,
                clHindi,
                clArabic,
                clAfrikaans,
                clPortuguese,
                clEspanol,
            )
        }


        langName = selectedLanguage
        val lowercaseLangName = langName?.toLowerCase(Locale.ROOT) ?: ""
        val langIndex = langNameIndex(lowercaseLangName)
        clIds.forEach { it.background = null }
        clIds[langIndex].background = resources.getDrawable(R.drawable.drawablestroke)

        // Show the second ad when a language is selected on second screen
        showSecondAdDup()

    }

    private fun langNameIndex(language: String): Int {
        val languageCodes = listOf(
            "en",
            "hi",
            "ar",
            "af",
            "pt",
            "es"
        )
        val index = languageCodes.indexOf(language)
        return if (index >= 0) index else 0
    }

    private fun showSecondAdDup() {
        if (RemoteConfigClass.native_language_other_dup_activity) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.visibility = View.VISIBLE
                nativeAdLangOtherDup.observe(this) { nativeAd ->
                    //

                    showLoadedNativeAd(
                        this,
                        binding.ads,
                        binding.includeShimmer.shimmerContainerNative,
                        R.layout.native_large_2,
                        nativeAd
                    )

                    //
                }
            }
        } else {
            binding.ads.beGone()
        }
    }

    private fun handleAds() {

        // first native ad on second language screen
        if (RemoteConfigClass.native_language_other_activity) {
            if (isNetworkAvailable() && PhoneNumberLocator.canRequestAd) {
                binding.ads.visibility = View.VISIBLE
                nativeAdLangOther.observe(this) { nativeAd ->
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

        // second/duplicate native ad on second language screen
        if (
            RemoteConfigClass.native_language_other_dup_activity &&
            isNetworkAvailable() &&
            PhoneNumberLocator.canRequestAd
        ) {
            loadAndReturnAd(
                this@PNLLanguageDuplicateActivity,
                resources.getString(R.string.admob_native_lang_low)
            ) { it1 ->
                Log.d("Native_dup_language_ad ", "value: $it1")
                if (it1 != null) {
                    nativeAdLangOtherDup.value = it1
                }
            }
        } else {
            nativeAdLangOtherDup.value = null
        }
    }

}
