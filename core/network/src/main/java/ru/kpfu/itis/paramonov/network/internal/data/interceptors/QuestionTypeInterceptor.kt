package ru.kpfu.itis.paramonov.network.internal.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.kpfu.itis.paramonov.network.internal.utils.Params

internal class QuestionTypeInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newUrl = chain.request().url.newBuilder()
            .addQueryParameter(Params.QUERY_TYPE_KEY, Params.QUERY_TYPE_DEFAULT_VALUE)
            .build()

        val requestBuilder = chain.request().newBuilder().url(newUrl)

        return chain.proceed(requestBuilder.build())
    }
}
