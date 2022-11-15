package com.curreny.converter.data.remote.client


import com.curreny.converter.base.domain.IApiClient
import com.curreny.converter.data.remote.models.LatestRatesResponse
import com.curreny.converter.data.remote.models.SymbolsResponseData
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface ICurrencyApiClient : IApiClient {

    fun getCountries(): Call<SymbolsResponseData>

    fun getLatestRates(base: String, conversion: String): Call<LatestRatesResponse>
}