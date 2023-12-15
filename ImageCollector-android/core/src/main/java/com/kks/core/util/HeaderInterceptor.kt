package com.kks.core.util

import okhttp3.Interceptor
import okhttp3.Response
/**
 * HeaderInterceptor에서 인증키를 세팅함으로써 api 호출마다 헤더에 인증키 값을 넣어주지 않아도 됩니다.
 * */
class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        proceed(
            request()
                .newBuilder()
                .addHeader("Authorization", Constants.AUTH_HEADER)
                .build()
        )
    }
}