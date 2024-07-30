package com.craftsilicon.shumul.agency.data.source.remote.util

import okhttp3.Interceptor
import okhttp3.Response

class StatusInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val statusCode = response.code
        println("Received status code: $statusCode")
        return response
    }
}