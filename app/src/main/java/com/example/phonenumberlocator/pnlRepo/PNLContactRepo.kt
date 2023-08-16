package com.example.tracklocation.tlRepo


import com.example.tracklocation.tlHelper.PNLMyContactsHelper
import com.example.phonenumberlocator.pnlModel.PNLMyContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PNLContactRepo @Inject constructor(private val contactHelper: PNLMyContactsHelper) {

    fun getContacts(isFavoritesOnly: Boolean, callback: ((ArrayList<PNLMyContact>) -> Unit)) {
        CoroutineScope(Dispatchers.IO).launch {
            contactHelper.getAvailableContacts(isFavoritesOnly) {
                callback.invoke(it)
            }
        }
    }



}