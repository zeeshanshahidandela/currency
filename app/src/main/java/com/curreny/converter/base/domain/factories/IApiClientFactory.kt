package com.curreny.converter.base.domain.factories

import com.curreny.converter.base.domain.IApiClient

interface IApiClientFactory {
    fun <T : IApiClient> create(modelClass: Class<T>): T
}