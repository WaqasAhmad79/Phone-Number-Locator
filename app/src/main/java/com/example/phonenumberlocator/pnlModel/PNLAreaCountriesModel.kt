package com.example.tracklocation.tlModel

import com.google.gson.annotations.SerializedName

data class PNLAreaCountriesModel (

    @SerializedName("id"         ) var id         : Int?     = null,
    @SerializedName("name"       ) var name       : String?  = null,
    @SerializedName("iso3"       ) var iso3       : String?  = null,
    @SerializedName("iso2"       ) var iso2       : String?  = null,
    @SerializedName("phonecode"  ) var phonecode  : String?  = null,
    @SerializedName("capital"    ) var capital    : String?  = null,
    @SerializedName("currency"   ) var currency   : String?  = null,
    @SerializedName("flag"       ) var flag       : Boolean? = null,
    @SerializedName("wikiDataId" ) var wikiDataId : String?  = null,
    @SerializedName("createdAt"  ) var createdAt  : String?  = null,
    @SerializedName("updatedAt"  ) var updatedAt  : String?  = null

)