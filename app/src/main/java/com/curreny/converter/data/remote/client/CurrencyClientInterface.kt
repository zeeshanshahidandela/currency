package com.curreny.converter.data.remote.client


import com.curreny.converter.data.remote.models.LatestRatesResponse
import com.curreny.converter.data.remote.models.SymbolsResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface CurrencyApiClient : ICurrencyApiClient {

    @GET("symbols")
    override fun getCountries(): Call<SymbolsResponseData>

    @GET("latest")
    override  fun getLatestRates(base: String, conversion: String): Call<LatestRatesResponse>
}