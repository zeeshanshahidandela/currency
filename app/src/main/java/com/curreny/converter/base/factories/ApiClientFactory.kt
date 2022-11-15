package com.curreny.converter.base.factories

import com.curreny.converter.base.domain.IApiClient
import com.curreny.converter.base.domain.factories.IApiClientFactory
import javax.inject.Inject
import javax.inject.Provider

class ApiClientFactory @Inject constructor(
    private val creators: Map<Class<out IApiClient>, @JvmSuppressWildcards Provider<IApiClient>>
) : IApiClientFactory {
    override fun <T : IApiClient> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown ApiClient Class: $modelClass")

        return try {
            @Suppress("UNCHECKED_CAST")
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}