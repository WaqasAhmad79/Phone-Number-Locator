package com.example.phonenumberlocator.pnlModel

import com.google.gson.annotations.SerializedName

//class AreaCodeModel(var city: String,var areacode: String)

data class PNLAreaCodeModel (

    @SerializedName("id"            ) var id           : String? = null,
    @SerializedName("country_id"    ) var countryId    : String? = null,
    @SerializedName("city"          ) var city         : String? = null,
    @SerializedName("area_code"     ) var areaCode     : String? = null,
    @SerializedName("dialing_start" ) var dialingStart : String? = null,
    @SerializedName("c_name"        ) var cName        : String? = null

)