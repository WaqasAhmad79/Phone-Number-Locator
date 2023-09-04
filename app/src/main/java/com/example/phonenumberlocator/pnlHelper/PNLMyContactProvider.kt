package com.example.phonenumberlocator.pnlHelper

import android.database.Cursor
import android.net.Uri
import com.example.phonenumberlocator.BuildConfig
import com.example.phonenumberlocator.pnlExtensionFun.getIntValue
import com.example.phonenumberlocator.pnlExtensionFun.getStringValue
import com.example.phonenumberlocator.pnlModel.PNLLocatorPhoneNumber
import com.example.phonenumberlocator.pnlModel.PNLMyContact
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



// used for sharing privately stored contacts in Simple Contacts with Simple Dialer, Simple SMS Messenger and Simple Calendar Pro
class PNLMyContactProvider {
    companion object {
//        private const val AUTHORITY = "com.example.true_caller_id_2022.contactsprovider"
        val CONTACTS_CONTENT_URI = Uri.parse("content://${BuildConfig.APPLICATION_ID}/contacts")

        const val FAVORITES_ONLY = "favorites_only"
        const val COL_RAW_ID = "raw_id"
        const val COL_CONTACT_ID = "contact_id"
        const val COL_NAME = "name"
        const val COL_PHOTO_URI = "photo_uri"
//        const val LOCATION = "location"
//        const val NETWORK = "network_name"
        const val COL_PHONE_NUMBERS = "phone_numbers"
        const val COL_BIRTHDAYS = "birthdays"
        const val COL_ANNIVERSARIES = "anniversaries"

        fun getSimpleContacts(cursor: Cursor?): ArrayList<PNLMyContact> {
            val contacts = ArrayList<PNLMyContact>()
            try {
                cursor?.use {
                    if (cursor.moveToFirst()) {
                        do {
                            val rawId = cursor.getIntValue(COL_RAW_ID)
                            val contactId = cursor.getIntValue(COL_CONTACT_ID)
                            val name = cursor.getStringValue(COL_NAME)
                            val photoUri = cursor.getStringValue(COL_PHOTO_URI)
//                            val location = cursor.getStringValue(LOCATION)
//                            val network = cursor.getStringValue(NETWORK)
                            val phoneNumbersJson = cursor.getStringValue(COL_PHONE_NUMBERS)
                            val birthdaysJson = cursor.getStringValue(COL_BIRTHDAYS)
                            val anniversariesJson = cursor.getStringValue(COL_ANNIVERSARIES)

                            val phoneNumbersToken = object : TypeToken<ArrayList<PNLLocatorPhoneNumber>>() {}.type
                            val phoneNumbers = Gson().fromJson<ArrayList<PNLLocatorPhoneNumber>>(phoneNumbersJson, phoneNumbersToken) ?: ArrayList()

                            val stringsToken = object : TypeToken<ArrayList<String>>() {}.type
                            val birthdays = Gson().fromJson<ArrayList<String>>(birthdaysJson, stringsToken) ?: ArrayList()
                            val anniversaries = Gson().fromJson<ArrayList<String>>(anniversariesJson, stringsToken) ?: ArrayList()

                            val contact = PNLMyContact(rawId, contactId, name, photoUri,phoneNumbers, birthdays, anniversaries)
                            contacts.add(contact)
                        } while (cursor.moveToNext())
                    }
                }
            } catch (ignored: Exception) {
            }
            return contacts
        }

    }
}
