package com.example.phonenumberlocator.ui.activities.helpScreens

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PNLIntroPagerAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragmentList = listOf(
        WelcomeSlide1Fragment(),
        WelcomeSlide2Fragment(),
        WelcomeSlide3Fragment(),
        WelcomeSlide4Fragment()
    )

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
