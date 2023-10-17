package com.example.phonenumberlocator.admob_ads

import com.example.phonenumberlocator.pnlUtil.PNLGenericAdapter


data class Ads(var isLoaded : Boolean = false) : PNLGenericAdapter.Searchable{
    override fun getSearchCriteria(): String {
        return ""
    }
}