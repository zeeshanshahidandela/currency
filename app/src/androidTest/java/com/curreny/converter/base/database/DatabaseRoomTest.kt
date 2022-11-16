package com.curreny.converter.base.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.curreny.converter.domain.entity.ConversionTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class DatabaseRoomTest: TestCase(){

    private lateinit var database: DatabaseRoom
    private lateinit var dao: CurrencyDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, DatabaseRoom::class.java).build()
        dao = database.getConversion()

    }

    @Test
    fun wirteAndReadConversion() = runBlocking{
        val conversionTransaction = ConversionTransaction(System.currentTimeMillis(), "ABC", "DEF", 123.22)
        dao.insertSingleConversion(conversionTransaction)
        val transactions = dao.getHistory()
        assertTrue(transactions.contains(conversionTransaction))
    }

    @After
    fun closeDb(){
        database.close()
    }
}