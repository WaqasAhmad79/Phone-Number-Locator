package com.example.phonenumberlocator.ui.activities.helpScreens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.phonenumberlocator.R
import com.example.phonenumberlocator.databinding.ActivityPnlintroSliderBinding
import com.example.phonenumberlocator.pnlDatabases.TinyDB
import com.example.phonenumberlocator.pnlExtensionFun.PrefManager
import com.example.phonenumberlocator.pnlExtensionFun.baseConfig
import com.example.phonenumberlocator.pnlExtensionFun.toast
import com.example.phonenumberlocator.pnlHelper.IS_PERMISSION_ON
import com.example.phonenumberlocator.ui.MainActivity
import com.github.angads25.toggle.widget.LabeledSwitch

class PNLIntroSliderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPnlintroSliderBinding
    private var prefManager: PrefManager? = null
    private lateinit var dots: Array<TextView?>
    private lateinit var layouts: IntArray
    private var myViewPagerAdapter: MyViewPagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPnlintroSliderBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        loadAd()
        prefManager = PrefManager(this)
        if (!prefManager!!.isFirstTimeLaunch) {
            launchHomeScreen()
            finish()
        }


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

      /*  val isShowAD = intent.getBooleanExtra("isComingFromSplash", false)
        if (isShowAD) {
            showPriorityAdmobInterstitial(true,getString(R.string.admob_interistitial_search_high),
                getString(R.string.admob_interistitial_search_low)
                , {
                    interstitialAdPriority = it
                })
        }*/

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = intArrayOf(
            R.layout.welcome_slide1,
            R.layout.welcome_slide3,
            R.layout.welcome_slide2,
            R.layout.activity_app_permission
        )


        // adding bottom dots
        addBottomDots(0)

        // making notification bar transparent
        changeStatusBarColor()
        myViewPagerAdapter = MyViewPagerAdapter()
        binding.viewPager.adapter = myViewPagerAdapter
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        binding.btnNext.setOnClickListener {
            // checking for last page
            // if last page home screen will be launched
            val current = getItem(+1)
            if (current < layouts.size) {
                // move to next screen
                binding.viewPager.currentItem = current
            } else {
                launchHomeScreen()
            }
        }
    }

    private fun addBottomDots(currentPage: Int) {
        dots = arrayOfNulls(layouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        binding.layoutDots.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]!!.text = Html.fromHtml("&#8226;")
            dots[i]!!.textSize = 35f
            dots[i]!!.setTextColor(colorsInactive[currentPage])
            binding.layoutDots.addView(dots[i])
        }
        if (dots.isNotEmpty()) dots[currentPage]!!.setTextColor(colorsActive[currentPage])

    }

    private fun getItem(i: Int): Int {
        return binding.viewPager.currentItem + i
    }

    private fun launchHomeScreen() {
        baseConfig.isAppIntroComplete=true
        val arePermissionsGranted = areAllPermissionsGranted()
        val permissionSwitch =findViewById<LabeledSwitch>(R.id.permission_switch)
        if (permissionSwitch.isOn && arePermissionsGranted) {
            startActivity(Intent(this@PNLIntroSliderActivity, MainActivity::class.java))
            finish()
        } else {
            toast(R.string.grant_all_permissions)
        }

    }


    //  viewpager change listener
    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object :
        ViewPager.OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            addBottomDots(position)

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.size - 1) {
                // last page. make button text to Get Started
                binding.btnNext1.text = getString(R.string.go)

            } else {
                // still pages are left
                binding.btnNext1.text = getString(R.string.next)

            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }

    /**
     * Making notification bar transparent
     */
    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    /**
     * View pager adapter
     */
    inner class MyViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun getCount(): Int {
            return layouts.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)

            // Handle the permission layout
            if (position == layouts.size - 1) { // Check if it's the permission layout
                val permissionSwitch = view.findViewById<LabeledSwitch>(R.id.permission_switch)

                // Handle switch state changes
                permissionSwitch.setOnToggledListener { _, isOn ->
                    if (isOn){
                        TinyDB.getInstance(this@PNLIntroSliderActivity).putBoolean(IS_PERMISSION_ON, true)
                        runtimePer(isOn)
                    }
                    else{
                        TinyDB.getInstance(this@PNLIntroSliderActivity).putBoolean(IS_PERMISSION_ON, false)
                    }
                }
            }

            return view
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }
    private fun areAllPermissionsGranted(): Boolean {
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    private fun runtimePer(isSwitchOn: Boolean) {
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (!requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(
                this, requiredPermissions, 1
            )
        } else {
            // Handle permissions granted
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                    // Permissions are granted, set the switch to ON
                    val permissionSwitch = findViewById<LabeledSwitch>(R.id.permission_switch)
                    permissionSwitch.isOn = true
                    // Handle any other actions you need to perform when permissions are granted
                } else {
                    // Permissions are denied, set the switch to OFF
                    val permissionSwitch = findViewById<LabeledSwitch>(R.id.permission_switch)
                    permissionSwitch.isOn = false
                    // Handle any other actions you need to perform when permissions are denied
                }
            }
        }
    }

   /* private fun loadAd() {
        if (isNetworkAvailable()) {
            binding.ads.visibility = View.VISIBLE
            LocationTrackerAppClass.instance.nativeAdBoarding.observe(this) {
                showLoadedNativeAd(
                    this,
                    binding.ads,
                    R.layout.layout_admob_native_ad_withou_tmedia,
                    it
                )
            }
        } else {
            binding.ads.visibility = View.GONE
        }
    }*/
}