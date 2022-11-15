package com.curreny.converter.data.remote.models

import com.google.gson.annotations.SerializedName

data class LatestRatesResponse (
  @SerializedName("success") var success   : Boolean? = null,
  @SerializedName("timestamp") var timestamp : Int?     = null,
  @SerializedName("base") var base      : String?  = null,
  @SerializedName("date") var date      : String?  = null,
  @SerializedName("rates") var rates     : Map<String, Double>?   = mutableMapOf<String, Double>()
)