package com.curreny.converter.data.remote.datasource

import com.curreny.converter.data.remote.client.CurrencyApiClient
import com.curreny.converter.data.remote.models.LatestRatesResponse
import com.curreny.converter.data.remote.models.SymbolsResponseData
import com.curreny.converter.base.utils.ApiState
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CurrencyRemoteDataSource @Inject constructor(
    private val currencyClientApi: CurrencyApiClient,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getAllCurrencies(): ApiState<SymbolsResponseData> {
        return ApiState.Success(currencyClientApi.getCountries())
    }

    suspend fun getLatestRates(base: String, conversion: String): ApiState<LatestRatesResponse> {
        return ApiState.Success(currencyClientApi.getLatestRates(base, conversion))
    }

}