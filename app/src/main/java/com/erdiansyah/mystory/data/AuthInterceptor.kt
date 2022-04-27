package com.erdiansyah.mystory.data

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var token: String?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val basicReq = req.newBuilder()
                if (token != null) {
                    basicReq.addHeader("Authorization", "Bearer $token")
                }
        return chain.proceed(basicReq.build())
    }
}