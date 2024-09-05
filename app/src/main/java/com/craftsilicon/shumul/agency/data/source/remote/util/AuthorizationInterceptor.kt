package com.craftsilicon.shumul.agency.data.source.remote.util

import com.craftsilicon.shumul.agency.data.source.storage.pref.StorageDataSource
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthorizationInterceptor @Inject constructor(
    private val storage: StorageDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header(
            "Authorization",
            "Bearer ${storage.token.value}"
        ).build()
        return chain.proceed(request)
    }
}