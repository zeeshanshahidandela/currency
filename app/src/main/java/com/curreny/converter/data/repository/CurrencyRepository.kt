package com.curreny.converter.data.repository

import androidx.lifecycle.LiveData
import com.curreny.converter.data.remote.datasource.CurrencyRemoteDataSource
import com.curreny.converter.data.remote.models.SymbolsResponseData
import com.curreny.converter.base.utils.ApiState
import com.curreny.converter.data.dto.convertToCurrencyModel
import com.curreny.converter.data.local.datasoruce.CurrencyLocalDataSource
import com.curreny.converter.data.remote.models.LatestRatesResponse
import com.curreny.converter.domain.entity.ConversionTransaction
import com.curreny.converter.domain.entity.CurrencyModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyRemoteDataSource: CurrencyRemoteDataSource,
    private val currencyLocalDataSource: CurrencyLocalDataSource,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getAllCurrencies(): Flow<List<CurrencyModel>?> = flow {

            when (val response = currencyRemoteDataSource.getAllCurrencies()) {
                is ApiState.Success -> {
                    emit(response.data.symbols?.convertToCurrencyModel())
                }
                is ApiState.Loading -> emit(emptyList())
                is ApiState.Failure -> emit(emptyList())
                is ApiState.Empty -> emit(emptyList())
            }
    }.flowOn(dispatcher)

    suspend fun getConvertedCurrency(base: String, conversion: String): Flow<ApiState<ConversionTransaction>> = flow {
        when(val response = currencyRemoteDataSource.getLatestRates(base, conversion)){
            is ApiState.Success -> {
                val data =  response.data
                if(data.success == true) {
                    val convertedRate = data.rates?.get(conversion)
                    if(convertedRate != null) {
                        val transaction = ConversionTransaction(System.currentTimeMillis(), base, conversion, convertedRate)
                        currencyLocalDataSource.saveTransactions(transaction)
                        emit(ApiState.Success(transaction))
                    }
                }
            }
            is ApiState.Failure -> {
                emit(ApiState.Failure(response.msg))
            }
            is ApiState.Loading -> {
                emit(ApiState.Loading)
            }
            is ApiState.Empty -> {
                emit(ApiState.Empty)
            }
        }

    }.flowOn(dispatcher)

    suspend fun getTopCurrency(base: String, conversion: String): Flow<ApiState<List<ConversionTransaction>>> = flow {
        when(val response = currencyRemoteDataSource.getLatestRates(base, conversion)){
            is ApiState.Success -> {
                val data =  response.data
                if(data.success == true) {
                    val list = mutableListOf<ConversionTransaction>()
                    data.rates?.forEach{
                        val convertedRate = it.value
                        val transaction = ConversionTransaction(System.currentTimeMillis(), base, it.key, convertedRate)
                        list.add(transaction)
                    }
                    emit(ApiState.Success(list))
                }
            }
            is ApiState.Failure -> {
                emit(ApiState.Failure(response.msg))
            }
            is ApiState.Loading -> {
                emit(ApiState.Loading)
            }
            is ApiState.Empty -> {
                emit(ApiState.Empty)
            }
        }

    }.flowOn(dispatcher)

    fun observeTransactions(): LiveData<List<ConversionTransaction>> {
        return currencyLocalDataSource.observeTransactions()
    }

    suspend fun getHistory(): List<ConversionTransaction> {
        return currencyLocalDataSource.getHistory()
    }
}