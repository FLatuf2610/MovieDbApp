package com.example.moviedbapp.data.network.interceptor

import com.example.moviedbapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {

    private val authToken = Constants.READ_TOKEN
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build()

        return chain.proceed(newRequest)
    }
}