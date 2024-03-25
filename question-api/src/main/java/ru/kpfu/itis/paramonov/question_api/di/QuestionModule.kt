package ru.kpfu.itis.paramonov.question_api.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.paramonov.question_api.BuildConfig
import ru.kpfu.itis.paramonov.question_api.data.QuestionRepositoryImpl
import ru.kpfu.itis.paramonov.question_api.data.interceptors.QuestionTypeInterceptor
import ru.kpfu.itis.paramonov.question_api.data.remote.QuestionApi
import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository

@Module
@InstallIn(SingletonComponent::class)
class QuestionModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(QuestionTypeInterceptor())
        return clientBuilder.build()
    }

    @Provides
    fun provideQuestionApi(okHttpClient: OkHttpClient): QuestionApi {
        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.TRIVIA_DB_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return builder.create(QuestionApi::class.java)
    }

    @Provides
    fun provideQuestionRepository(repository: QuestionRepositoryImpl): QuestionRepository = repository
}