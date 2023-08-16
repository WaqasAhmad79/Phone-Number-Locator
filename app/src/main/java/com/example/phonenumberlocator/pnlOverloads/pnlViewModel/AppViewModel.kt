package com.example.phonenumberlocator.pnlOverloads.pnlViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phonenumberlocator.pnlAppCallModels.RecentCallsDetailModel
import com.example.phonenumberlocator.pnlModel.PNLMyContact
import com.example.tracklocation.tlRepo.PNLContactRepo

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val contactRepo: PNLContactRepo,
) : ViewModel() {

    val contact = MutableLiveData<String>()
    val contactsList = MutableLiveData<ArrayList<PNLMyContact>>()
    val favContactList = MutableLiveData<ArrayList<PNLMyContact>>()
//    val recentList = MutableLiveData<ArrayList<ItemClassModel>>()
//    val recentListOnly = MutableLiveData<ArrayList<ItemClassModel>>()
//    val blockedCallsList = MutableLiveData<ArrayList<ItemClassModel>>()
    val advancedRecentList = MutableLiveData<ArrayList<RecentCallsDetailModel>>()


    fun getContacts() {
        contactRepo.getContacts(true) {
            viewModelScope.launch {
                favContactList.value = it
            }
        }
        contactRepo.getContacts(false) {
            viewModelScope.launch {
                contactsList.value = it
            }
        }

    }

    //get recent calls from system  then generate advance recent list with more info
//    fun getRecent(isFavoritesOnly: Boolean) {
//        recentRepo.getRecentCalls(isFavoritesOnly, {
//            viewModelScope.launch {
//                recentList.value = it
//            }
//        }, {
//            viewModelScope.launch {
//                advancedRecentList.value = it
//            }
//        })
//    }

//    fun getOnlyRecent(isFavoritesOnly: Boolean) {
//        recentRepo.getOnlyRecentCalls(isFavoritesOnly) {
//            viewModelScope.launch {
//                recentListOnly.value = it
//            }
//        }
//    }

//    fun getBlockedCalls(isFavoritesOnly: Boolean) {
//        recentRepo.getBlockedCalls(isFavoritesOnly) {
//            viewModelScope.launch {
//                blockedCallsList.value = it
//            }
//        }
//    }

  /*  fun deleteAllRecentCalls(context: Context, callback: () -> Unit) {
        RecentsHelper(context).removeAllRecentCalls(context) {
            recentListOnly.value = arrayListOf()
            recentList.value = arrayListOf()
            callback.invoke()
        }
    }

    fun deleteRecent(
        callsToRemove: ArrayList<RecentModel>,
        context: Context,
        callback: () -> Unit,
    ) {
        val idsToRemove = ArrayList<Int>()
        callsToRemove.forEach {
            idsToRemove.add(it.id)
            it.neighbourIDs.mapTo(idsToRemove, { it })
        }
        RecentsHelper(context).removeRecentCalls(idsToRemove) {
            callback.invoke()
        }
    }*/

    fun setContact(number: String) {
        contact.value = number
    }

    private val _searchText: MutableLiveData<String> = MutableLiveData()
    var searchText: LiveData<String> = _searchText
    fun setSearchText(txt: String) {
        _searchText.value = txt
    }

    private val _downloading: MutableLiveData<Boolean> = MutableLiveData()
    val downloading: LiveData<Boolean> = _downloading

    fun setDownloading(downloading: Boolean) {
        _downloading.value = downloading
    }

    private val _ProgressValue:MutableLiveData<Int> = MutableLiveData()
    val progress: LiveData<Int> =_ProgressValue
    fun setProgress(progress: Int) {
        _ProgressValue.value = progress
    }

}
