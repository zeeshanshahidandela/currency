package com.curreny.converter.data.remote.models

import com.google.gson.annotations.SerializedName

data class Rates (
  @SerializedName("EUR" ) var EUR : Double? = null,
  @SerializedName("GBP" ) var GBP : Double? = null

)