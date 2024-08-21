package com.example.phonenumberlocator.ui.activities.helpScreens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.phonenumberlocator.PhoneNumberLocator.Companion.canRequestAd
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.RemoteConfigClass
import com.example.phonenumberlocator.admob_ads.showNormalAdmobInterstitial
import com.example.phonenumberlocator.databinding.ActivityPnlintroSliderBinding
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.beGone
import com.example.phonenumberlocator.pnlExtensionFun.beVisible
import com.example.phonenumberlocator.pnlExtensionFun.hasPermission
import com.example.phonenumberlocator.pnlExtensionFun.isNetworkAvailable
import com.example.phonenumberlocator.pnlExtensionFun.toast
import com.example.phonenumberlocator.pnlHelper.PERMISSION_ACCESS_FINE_LOCATION
import com.example.phonenumberlocator.ui.MainActivity

class PNLIntroSliderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnlintroSliderBinding
    private var fragmentDestination = 0
    var isIncomingFromSplash = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnlintroSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = window
        val drawable = resources.getDrawable(R.drawable.bg_dialog_white)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.app_color)
        window.setBackgroundDrawable(drawable)
        isIncomingFromSplash = intent.getBooleanExtra("SplashNumber", false)
        if (isIncomingFromSplash) {

            if (RemoteConfigClass.inter_pnl_intro_slider_activity
                && isNetworkAvailable()
                && canRequestAd
            ) {
                showNormalAdmobInterstitial()
            }

        }
        initViews()
        handleClicks()

    }

    private fun initViews() {
        val adapter = PNLIntroPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = adapter
        setupIndicatorDots()
    }

    private fun handleClicks() {
        binding.btnNext.setOnClickListener {
            fragmentDestination++
            binding.viewPager.currentItem = fragmentDestination
        }
        binding.tvDone.setOnClickListener {
            if (hasPermission(PERMISSION_ACCESS_FINE_LOCATION)) {
                baseConfig.isAppIntroComplete = true
                baseConfig.appStarted = true
                val intent = Intent(this, MainActivity::class.java)
                if (isIncomingFromSplash) intent.putExtra("LanguageActivity", true)
                startActivity(intent)
                finish()
            } else {
                toast(R.string.permission_denied_title)
            }
        }
    }

    private fun setupIndicatorDots() {
        val numPages = binding.viewPager.adapter?.count ?: 0
        val dots = arrayOfNulls<ImageView>(numPages)
        for (i in 0 until numPages) {
            dots[i] = ImageView(this)
            dots[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    if (i == 0) R.drawable.indicator_dot_selected else R.drawable.indicator_dot_unselected
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.layoutDots.addView(dots[i], params)
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until numPages) {
                    dots[i]?.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@PNLIntroSliderActivity,
                            if (i == position) R.drawable.indicator_dot_selected else R.drawable.indicator_dot_unselected
                        )
                    )
                }

                Log.d("TAG42", "--------------------------------------")
                Log.d("TAG42", "onPageSelected: $position")

                if (position == 3) {
                    binding.btnNext.beGone() // next button
                    binding.btnNext1.beVisible() // the whole constraint layout
                    binding.tvDone.beVisible() // lets go button
                    binding.layoutDots.beVisible()

                } else if (position == 2) {
                    binding.btnNext1.beGone()
                    binding.layoutDots.beGone()
                } else {
                    binding.tvDone.beGone()
                    binding.btnNext.beVisible()
                    binding.btnNext1.beVisible()
                    binding.layoutDots.beVisible()
                }
                fragmentDestination = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                Log.d("TAG42", "onPageScrollStateChanged: ")
            }
        })
    }
}