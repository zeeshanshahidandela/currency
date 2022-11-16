package com.curreny.converter.data.remote.client

import com.curreny.converter.data.remote.models.LatestRatesResponse
import com.curreny.converter.data.remote.models.SymbolsResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface CurrencyApiClient {

    @GET("symbols")
    suspend fun getCountries(): SymbolsResponseData

    @GET("latest")
    suspend fun getLatestRates(
        @Query("base") base: String,
        @Query("symbols") conversion: String): LatestRatesResponse
}