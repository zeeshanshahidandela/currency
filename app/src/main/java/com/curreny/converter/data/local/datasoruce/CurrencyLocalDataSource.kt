package com.curreny.converter.data.local.datasoruce

import androidx.lifecycle.LiveData
import com.curreny.converter.base.database.CurrencyDao
import com.curreny.converter.data.remote.client.CurrencyApiClient
import com.curreny.converter.data.remote.models.LatestRatesResponse
import com.curreny.converter.data.remote.models.SymbolsResponseData
import com.curreny.converter.base.utils.ApiState
import com.curreny.converter.domain.entity.ConversionTransaction
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyLocalDataSource @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend fun saveTransactions(conversionTransaction: ConversionTransaction) =  withContext(coroutineDispatcher) {
        currencyDao.insertSingleConversion(conversionTransaction)
    }

    fun observeTransactions(): LiveData<List<ConversionTransaction>> {
        return currencyDao.observeTransactions()
    }

    suspend fun getHistory(): List<ConversionTransaction> {
        return currencyDao.getHistory()
    }

}