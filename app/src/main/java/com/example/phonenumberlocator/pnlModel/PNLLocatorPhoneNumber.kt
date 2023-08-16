package com.example.phonenumberlocator.pnlModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PNLLocatorPhoneNumber(var value: String, var type: Int, var label: String, var normalizedNumber: String, var isPrimary: Boolean = false):
    Parcelable

