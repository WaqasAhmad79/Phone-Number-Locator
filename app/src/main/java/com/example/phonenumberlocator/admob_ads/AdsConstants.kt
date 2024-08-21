//package com.example.numbertracker.admob_ads
//
//import android.util.Log
//
//var previousAdClosedTime = 0L
//var isAdFirstClick = true
//var isFirstTime = true
//
//fun isAdShownTimeIsOk(): Boolean {
//    if (isFirstTime) {
//        isFirstTime = false
//        previousAdClosedTime = System.currentTimeMillis()
//        return true
//    }
//    Log.i("DanishMainTAG", "isAdShownTimeIsOk: ${System.currentTimeMillis() - previousAdClosedTime > 15000}")
//    return System.currentTimeMillis() - previousAdClosedTime > 15000
//}
//
//fun isAdNotFirstTimeClicked(): Boolean {
//    return if (isAdFirstClick) {
//        isAdFirstClick = false
//        false
//    } else {
//        true
//    }
//}