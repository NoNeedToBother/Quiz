package ru.kpfu.itis.paramonov.question_api.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.paramonov.question_api.BuildConfig
import ru.kpfu.itis.paramonov.question_api.data.handler.QuestionExceptionHandler
import ru.kpfu.itis.paramonov.question_api.data.repository.QuestionRepositoryImpl
import ru.kpfu.itis.paramonov.question_api.data.interceptors.QuestionTypeInterceptor
import ru.kpfu.itis.paramonov.question_api.data.mapper.QuestionDomainModelMapper
import ru.kpfu.itis.paramonov.question_api.data.remote.QuestionApi
import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository

@Module
class QuestionModule {

    @Provides
    fun questionExceptionHandler(): QuestionExceptionHandler {
        return QuestionExceptionHandler()
    }

    @Provides
    fun questionTypeInterceptor(): QuestionTypeInterceptor {
        return QuestionTypeInterceptor()
    }

    @Provides
    fun questionDomainModelMapper(exceptionHandler: QuestionExceptionHandler): QuestionDomainModelMapper {
        return QuestionDomainModelMapper(exceptionHandler)
    }

    @Provides
    fun questionRepositoryImpl(api: QuestionApi, mapper: QuestionDomainModelMapper): QuestionRepositoryImpl {
        return QuestionRepositoryImpl(api, mapper)
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