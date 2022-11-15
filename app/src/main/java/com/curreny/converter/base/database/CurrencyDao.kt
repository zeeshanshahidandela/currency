package com.curreny.converter.base.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.curreny.converter.domain.entity.ConversionTransaction

@Dao
interface CurrencyDao {

    @Insert
    fun insertAllConversions(conversionTransactions: List<ConversionTransaction>)

    @Insert
    fun insertSingleConversion(conversionTransaction: ConversionTransaction)

    @Update
    fun singleConversion(conversionTransaction: ConversionTransaction)

    @Query("Select * from ConversionTransaction")
    fun getConversions(): LiveData<ConversionTransaction>

    @Delete
    fun deleteConversion(conversionTransaction: ConversionTransaction)

}