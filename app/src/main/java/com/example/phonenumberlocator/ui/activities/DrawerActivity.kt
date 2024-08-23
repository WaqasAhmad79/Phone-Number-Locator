package com.example.phonenumberlocator.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.admob_ads.isAppOpenEnable
import com.example.phonenumberlocator.databinding.ActivityDrawerBinding
import com.example.phonenumberlocator.pnlUtil.PNLAppsUtils
import com.example.phonenumberlocator.ui.MainActivity

class DrawerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDrawerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initClicks()

    }


    private fun initClicks() {

        binding.apply {

            clPrivacy.setOnClickListener {
                isAppOpenEnable = true
                val browserIntent = Intent(
                    Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.privacy_policy_link))
                )
                startActivity(browserIntent)
            }

            clShare.setOnClickListener {
                isAppOpenEnable = true
                PNLAppsUtils.shareApp(this@DrawerActivity)
            }

            clRate.setOnClickListener {
                isAppOpenEnable = true
                PNLAppsUtils.rateUs(this@DrawerActivity)
            }

            clLanguage.setOnClickListener {
                startActivity(
                    Intent(this@DrawerActivity, PNLLanguageActivity::class.java).putExtra(
                        "setting",
                        true
                    )
                )
            }

            backArrow.setOnClickListener {
                startActivity(
                    Intent(
                        this@DrawerActivity,
                        MainActivity::class.java
                    )
                )
                finish()

            }


        }
    }
}