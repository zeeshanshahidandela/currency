package com.curreny.converter.domain.entity

data class CurrencyModel(
    val title: String,
    val country: String?
) {
    override fun toString(): String {
        return title
    }
}
