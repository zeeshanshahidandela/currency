package com.curreny.converter.base.factories

import com.curreny.converter.base.domain.IRepository
import com.curreny.converter.base.domain.factories.IRepositoryFactory
import javax.inject.Inject
import javax.inject.Provider

class RepositoryFactory @Inject constructor(
    private val creators: Map<Class<out IRepository>, @JvmSuppressWildcards Provider<IRepository>>
) : IRepositoryFactory {
    override fun <T : IRepository> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown Repository Class: $modelClass")

        return try {
            @Suppress("UNCHECKED_CAST")
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}