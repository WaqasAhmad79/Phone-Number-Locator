package com.example.phonenumberlocator.pnlAppCallModels

import android.os.Parcelable
import com.example.phonenumberlocator.pnlAppCallModels.RecentCallModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecentCallsDetailModel(
    var id: Int,
    var phoneNumber: String,
    var name: String,
    var photoUri: String,
//    var location:String,
//    var network:String,
    var totalDuration: Int,
    var incomingCallDuration: Int,
    var outgoingCallDuration: Int,
    var incomingCalls: Int,
    var outgoingCalls: Int,
    var missCalls: Int,
    var unAnswerCalls: Int,
    var countTotalCalls: ArrayList<RecentCallModel>?

) : Parcelable
