package com.curreny.converter.base.utils


sealed class ApiState<out T> {
    object  Loading : ApiState<Nothing>()
    class Failure(val msg: Throwable) : ApiState<Nothing>()
    class Success<out T>(val data: T): ApiState<T>()
    object Empty : ApiState<Nothing>()
}

