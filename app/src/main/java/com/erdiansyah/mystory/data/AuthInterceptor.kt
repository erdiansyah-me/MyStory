package com.erdiansyah.mystory.data

import android.annotation.SuppressLint
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var token: String?) : Interceptor {

    @SuppressLint("SuspiciousIndentation")
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val basicReq = req.newBuilder()
            if (token != null) {
                basicReq.addHeader("Authorization", "Bearer $token")
            }
        return chain.proceed(basicReq.build())
    }
}