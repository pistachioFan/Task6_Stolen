package com.example.AuthApp.data.remote


import com.example.AuthApp.data.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder()


        val token = TokenManager.token
        if (token != null) {
            requestBuilder
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Content-Type", "application/json")
        }

        return chain.proceed(requestBuilder.build())
    }
}