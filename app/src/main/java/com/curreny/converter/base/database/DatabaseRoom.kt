package com.curreny.converter.base.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.curreny.converter.domain.entity.ConversionTransaction

@Database(
  entities = [ConversionTransaction::class],
  version = 1,
  exportSchema = false
)
abstract class DatabaseRoom : RoomDatabase() {
    abstract fun getConversion(): CurrencyDao
}