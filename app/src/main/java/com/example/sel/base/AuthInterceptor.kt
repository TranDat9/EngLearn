package com.example.sel.base

import okhttp3.Interceptor
import okhttp3.Response

object TokenManager {
    var token: String? = null
}
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        val token = TokenManager.token

        if (token != null) {
            requestBuilder.header("Authorization", "Bearer $token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
