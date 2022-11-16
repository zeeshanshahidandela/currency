package com.curreny.converter.presentation.viewModels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.curreny.converter.base.database.CurrencyDao
import com.curreny.converter.base.database.DatabaseRoom
import com.curreny.converter.data.local.datasoruce.CurrencyLocalDataSource
import com.curreny.converter.data.remote.client.CurrencyApiClient
import com.curreny.converter.data.remote.datasource.CurrencyRemoteDataSource
import com.curreny.converter.data.repository.CurrencyRepository
import com.curreny.converter.domain.entity.ConversionTransaction
import dagger.hilt.android.AndroidEntryPoint
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@AndroidEntryPoint
class MainViewModelTest {

    @Inject
    protected lateinit var currencyClientApi: CurrencyApiClient

    private lateinit var viewModel: MainViewModel
    private lateinit var localDataSource: CurrencyLocalDataSource

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, DatabaseRoom::class.java)
            .allowMainThreadQueries().build()
        localDataSource = CurrencyLocalDataSource(db.getConversion(), Dispatchers.IO)
        val remoteDataSource = CurrencyRemoteDataSource(currencyClientApi, Dispatchers.IO)
        val repository = CurrencyRepository(remoteDataSource, localDataSource, Dispatchers.IO)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun testTransactionViewModel() = runBlocking{
        val conversionTransaction = ConversionTransaction(System.currentTimeMillis(), "ABC", "DEF", 123.22)
        localDataSource.saveTransactions(conversionTransaction)
        viewModel.getHistory()
        val result = viewModel.transactions.getOrAwaitValue().find { it.timestamp == conversionTransaction.timestamp }
        assertTrue(result != null)
    }

    @Test
    fun testCalculateArea_returnsGood(){
        val result = viewModel.calculateRate(3.0, 3.0)
        assertTrue(result == 9.0)
    }

}