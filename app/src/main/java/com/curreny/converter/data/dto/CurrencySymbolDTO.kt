package com.curreny.converter.data.dto

import com.curreny.converter.domain.entity.CurrencyModel

fun Map<String, String>.convertToCurrencyModel(): List<CurrencyModel> {
    return this.map {
        CurrencyModel(it.key, it.value)
    }
}