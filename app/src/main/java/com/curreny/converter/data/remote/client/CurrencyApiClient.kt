package com.curreny.converter.data.remote.client

import com.curreny.converter.data.remote.models.LatestRatesResponse
import com.curreny.converter.data.remote.models.SymbolsResponseData
import retrofit2.Call
import retrofit2.http.GET

interface CurrencyApiClient {

    @GET("symbols")
    fun getCountries(): SymbolsResponseData

    @GET("latest")
    fun getLatestRates(base: String, conversion: String): LatestRatesResponse
}