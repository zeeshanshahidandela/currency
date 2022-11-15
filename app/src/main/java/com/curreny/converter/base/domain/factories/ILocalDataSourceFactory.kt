package com.curreny.converter.base.domain.factories

import com.curreny.converter.base.domain.ILocalDataSource


interface ILocalDataSourceFactory {
    fun <T : ILocalDataSource> create(modelClass: Class<T>): T
}