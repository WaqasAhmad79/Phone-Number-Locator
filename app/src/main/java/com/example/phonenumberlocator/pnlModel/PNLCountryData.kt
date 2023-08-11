package com.example.tracklocation.tlModel

import com.google.gson.annotations.SerializedName

data class PNLCountryData(
    @SerializedName("NAME"  ) var NAME  : String? = null,
    @SerializedName("ISD"   ) var ISD   : String? = null,
    @SerializedName("CODE1" ) var CODE1 : String? = null,
    @SerializedName("CODE2" ) var CODE2 : String? = null
)
