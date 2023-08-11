package com.example.phonenumberlocator.pnlInterfaces

import android.location.Location

interface MyLocationCallback {
    fun gotAllLocation(location: Location?)
}