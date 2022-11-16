package com.curreny.converter.base.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.curreny.converter.domain.entity.ConversionTransaction
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Insert
    suspend fun insertAllConversions(conversionTransactions: List<ConversionTransaction>)

    @Insert
    suspend fun insertSingleConversion(conversionTransaction: ConversionTransaction)

    @Update
    fun singleConversion(conversionTransaction: ConversionTransaction)

    @Query("Select * from ConversionTransaction")
    fun observeTransactions(): LiveData<List<ConversionTransaction>>

    @Delete
    fun deleteConversion(conversionTransaction: ConversionTransaction)

    @Query("SELECT * from ConversionTransaction WHERE CAST((timeStamp / 1000) AS INTEGER) BETWEEN strftime('%s','now','-3 days') AND strftime('%s','now')  ORDER BY timeStamp DESC;")
    fun getHistory() : List<ConversionTransaction>

}