package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnlsplashBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlUtil.setCurrentLocale
import com.example.phonenumberlocator.ui.MainActivity
import com.example.tracklocation.tlSharedPreferencesLang.PNLMySharePreferences

class PNLSplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnlsplashBinding

    var started = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnlsplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

      /*  loadAndReturnAd(this,resources.getString(R.string.admob_native_lang_high)){
            if(it!=null)
            {
                TrackLocationAppClass.instance.nativeAdLang.value=it
            }else{
                loadAndReturnAd(this,resources.getString(R.string.admob_native_lang_low)){it2->
                    if(it2!=null)
                    {
                        TrackLocationAppClass.instance.nativeAdLang.value=it2
                    }
                }
            }
        }
        loadAndReturnAd(this,resources.getString(R.string.admob_native_main_high)){
            if(it!=null)
            {
                TrackLocationAppClass.instance.nativeAdMain.value=it
            }else{
                loadAndReturnAd(this,resources.getString(R.string.admob_native_main_low)){it2->
                    if(it2!=null)
                    {
                        TrackLocationAppClass.instance.nativeAdMain.value=it2
                    }
                }
            }
        }
*/

        setCurrentLocale(baseConfig.appLanguage.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        } else {
            val w: Window = this.window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            )
        }

        val mySharePreferences = PNLMySharePreferences(this)
        started = mySharePreferences.appStarted

        splashEnd()

    }

    private fun splashEnd() {

        Handler(Looper.getMainLooper()).postDelayed({

            if (!baseConfig.appStarted) {
                // The user has seen the Onboard
                startActivity(Intent(this, PNLLanguageActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }, 8000)
    }

}