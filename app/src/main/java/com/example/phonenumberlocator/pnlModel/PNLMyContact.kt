package com.example.phonenumberlocator.pnlModel

import android.os.Parcelable
import android.telephony.PhoneNumberUtils
import com.example.phonenumberlocator.pnlExtensionFun.normalizePhoneNumber
import com.example.phonenumberlocator.pnlExtensionFun.normalizeString
import com.example.phonenumberlocator.pnlHelper.SORT_BY_FULL_NAME
import com.example.phonenumberlocator.pnlHelper.SORT_DESCENDING
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class PNLMyContact(
    val rawId: Int,
    val contactId: Int,
    var name: String,
    var photoUri: String,
//    var location:String,
//    var network:String,
    var phoneNumbers:@RawValue ArrayList<PNLLocatorPhoneNumber>,
    var birthdays: ArrayList<String>,
    var anniversaries: ArrayList<String>,
) : Comparable<PNLMyContact>, Parcelable {

    companion object {
        var sorting = -1
    }

    override fun compareTo(other: PNLMyContact): Int {
        if (sorting == -1) {
            return compareByFullName(other)
        }

        var result = when {
            sorting and SORT_BY_FULL_NAME != 0 -> compareByFullName(other)
            else -> rawId.compareTo(other.rawId)
        }

        if (sorting and SORT_DESCENDING != 0) {
            result *= -1
        }

        return result
    }

    private fun compareByFullName(other: PNLMyContact): Int {
        val firstString = name.normalizeString()
        val secondString = other.name.normalizeString()

        return if (firstString.firstOrNull()?.isLetter() == true && secondString.firstOrNull()
                ?.isLetter() == false
        ) {
            -1
        } else if (firstString.firstOrNull()?.isLetter() == false && secondString.firstOrNull()
                ?.isLetter() == true
        ) {
            1
        } else {
            if (firstString.isEmpty() && secondString.isNotEmpty()) {
                1
            } else if (firstString.isNotEmpty() && secondString.isEmpty()) {
                -1
            } else {
                firstString.compareTo(secondString, true)
            }
        }
    }

    fun doesContainPhoneNumber(text: String): Boolean {
        return if (text.isNotEmpty()) {
            val normalizedText = text.normalizePhoneNumber()
            if (normalizedText.isEmpty()) {
                phoneNumbers.map { it.normalizedNumber }.any { phoneNumber ->
                    phoneNumber.contains(text)
                }
            } else {
                phoneNumbers.map { it.normalizedNumber }.any { phoneNumber ->
                    PhoneNumberUtils.compare(phoneNumber.normalizePhoneNumber(), normalizedText) ||
                            phoneNumber.contains(text) ||
                            phoneNumber.normalizePhoneNumber().contains(normalizedText) ||
                            phoneNumber.contains(normalizedText)
                }
            }
        } else {
            false
        }
    }

    fun doesHavePhoneNumber(text: String): Boolean {
        return if (text.isNotEmpty()) {
            val normalizedText = text.normalizePhoneNumber()
            if (normalizedText.isEmpty()) {
                phoneNumbers.map { it.normalizedNumber }.any { phoneNumber ->
                    phoneNumber == text
                }
            } else {
                phoneNumbers.map { it.normalizedNumber }.any { phoneNumber ->
                    PhoneNumberUtils.compare(phoneNumber.normalizePhoneNumber(), normalizedText) ||
                            phoneNumber == text ||
                            phoneNumber.normalizePhoneNumber() == normalizedText ||
                            phoneNumber == normalizedText
                }
            }
        } else {
            false
        }
    }
}
