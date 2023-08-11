package com.example.phonenumberlocator.pnlAppCallModels

import android.os.Parcelable
import android.telephony.PhoneNumberUtils
import com.example.phonenumberlocator.pnlExtensionFun.normalizePhoneNumber
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecentCallModel(
    var id: Int,
    var phoneNumber: String,
    var name: String,
    var photoUri: String,
//    var location:String,
//    var network:String,
    var startTS: Int,
    var duration: Int,
    var type: Int,
    var neighbourIDs: ArrayList<Int>,
    val simID: Int
) : Parcelable {
    fun doesContainPhoneNumber(text: String): Boolean {
        val normalizedText = text.normalizePhoneNumber()
        return PhoneNumberUtils.compare(phoneNumber.normalizePhoneNumber(), normalizedText) ||
            phoneNumber.contains(text) ||
            phoneNumber.normalizePhoneNumber().contains(normalizedText) ||
            phoneNumber.contains(normalizedText)
    }
}
