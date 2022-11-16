package com.curreny.converter.base.di

import android.content.Context
import androidx.room.Room
import com.curreny.converter.base.database.CurrencyDao
import com.curreny.converter.base.database.DatabaseRoom
import com.curreny.converter.base.utils.NetworkComponents
import com.curreny.converter.base.utils.NetworkInterceptor
import com.curreny.converter.data.remote.client.CurrencyApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun provideHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkInterceptor: NetworkInterceptor
    ): OkHttpClient.Builder =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(networkInterceptor)

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient.Builder): CurrencyApiClient =
        Retrofit.Builder()
            .baseUrl(NetworkComponents.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
            .create(CurrencyApiClient::class.java)

    @Singleton
    @Provides
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun getCurrenciesDB(@ApplicationContext context: Context) : CurrencyDao =
        Room.databaseBuilder(context, DatabaseRoom::class.java,"CurrencyDB").build().getConversion()
}