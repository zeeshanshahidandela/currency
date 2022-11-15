package com.curreny.converter.base.domain.factories

import com.curreny.converter.base.domain.IRemoteDataSource


interface IRemoteDataSourceFactory {
    fun <T : IRemoteDataSource> create(modelClass: Class<T>): T
}