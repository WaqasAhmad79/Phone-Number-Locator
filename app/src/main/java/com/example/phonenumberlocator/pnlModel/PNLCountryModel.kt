package com.example.tracklocation.tlModel

import com.google.gson.annotations.SerializedName

data class PNLCountryModel(val name: String,
                           @SerializedName("dial_code")
                       val dialCode: String,
                           val code: String)

//fetched from this sample json

//"name": "Pakistan",
//"dial_code": "+92",
//"code": "PK"