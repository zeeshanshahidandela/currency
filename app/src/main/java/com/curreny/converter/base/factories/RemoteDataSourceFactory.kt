package com.curreny.converter.base.factories

import com.curreny.converter.base.domain.IRemoteDataSource
import com.curreny.converter.base.domain.factories.IRemoteDataSourceFactory
import javax.inject.Inject
import javax.inject.Provider

class RemoteDataSourceFactory @Inject constructor(
    private val creators: Map<Class<out IRemoteDataSource>, @JvmSuppressWildcards Provider<IRemoteDataSource>>
) : IRemoteDataSourceFactory {
    override fun <T : IRemoteDataSource> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown RemoteDataSource Class: $modelClass")

        return try {
            @Suppress("UNCHECKED_CAST")
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}