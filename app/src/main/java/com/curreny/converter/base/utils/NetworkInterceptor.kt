package com.curreny.converter.base.utils

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .apply {
                addHeader("apikey", NetworkComponents.apiKey)
            }
            .build()
        return chain.proceed(request)
    }
}