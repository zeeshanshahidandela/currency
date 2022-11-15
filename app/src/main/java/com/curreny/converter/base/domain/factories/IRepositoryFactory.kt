package com.curreny.converter.base.domain.factories

import com.curreny.converter.base.domain.IRepository


interface IRepositoryFactory {
    fun <T : IRepository> create(modelClass: Class<T>): T
}