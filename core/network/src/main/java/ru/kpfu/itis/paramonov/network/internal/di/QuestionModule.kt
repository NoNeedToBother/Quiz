package ru.kpfu.itis.paramonov.network.internal.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.HtmlDecoder
import ru.kpfu.itis.paramonov.network.BuildConfig
import ru.kpfu.itis.paramonov.network.internal.data.handler.QuestionExceptionHandler
import ru.kpfu.itis.paramonov.network.internal.data.repository.QuestionRepositoryImpl
import ru.kpfu.itis.paramonov.network.internal.data.interceptors.QuestionTypeInterceptor
import ru.kpfu.itis.paramonov.network.internal.data.mapper.QuestionDomainModelMapper
import ru.kpfu.itis.paramonov.network.internal.data.remote.QuestionApi
import ru.kpfu.itis.paramonov.network.external.domain.repository.QuestionRepository

@Module
internal class QuestionModule {

    @Provides
    fun questionExceptionHandler(resourceManager: ResourceManager): QuestionExceptionHandler {
        return QuestionExceptionHandler(resourceManager)
    }

    @Provides
    fun questionTypeInterceptor(): QuestionTypeInterceptor {
        return QuestionTypeInterceptor()
    }

    @Provides
    fun questionDomainModelMapper(exceptionHandler: QuestionExceptionHandler,
                                  htmlDecoder: HtmlDecoder): QuestionDomainModelMapper {
        return QuestionDomainModelMapper(exceptionHandler, htmlDecoder)
    }

    @Provides
    fun questionRepositoryImpl(api: QuestionApi, mapper: QuestionDomainModelMapper,
                               resourceManager: ResourceManager,
                               exceptionHandler: QuestionExceptionHandler, dispatcher: CoroutineDispatcher
    ): QuestionRepositoryImpl {
        return QuestionRepositoryImpl(api, mapper, resourceManager, exceptionHandler, dispatcher)
    }

    @Provides
    fun okHttpClient(questionTypeInterceptor: QuestionTypeInterceptor): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(questionTypeInterceptor)
        return clientBuilder.build()
    }

    @Provides
    fun questionApi(okHttpClient: OkHttpClient): QuestionApi {
        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.TRIVIA_DB_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return builder.create(QuestionApi::class.java)
    }

    @Provides
    fun questionRepository(repository: QuestionRepositoryImpl): QuestionRepository = repository
}