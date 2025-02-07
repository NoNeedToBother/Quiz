package ru.kpfu.itis.paramonov.network.external.di

import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kpfu.itis.paramonov.network.BuildConfig
import ru.kpfu.itis.paramonov.network.external.domain.repository.QuestionRepository
import ru.kpfu.itis.paramonov.network.internal.data.handler.QuestionExceptionHandler
import ru.kpfu.itis.paramonov.network.internal.data.interceptors.QuestionTypeInterceptor
import ru.kpfu.itis.paramonov.network.internal.data.mapper.QuestionDomainModelMapper
import ru.kpfu.itis.paramonov.network.internal.data.remote.QuestionApi
import ru.kpfu.itis.paramonov.network.internal.data.repository.QuestionRepositoryImpl

val questionsModule = DI {
    bind<QuestionExceptionHandler>() with provider {
        QuestionExceptionHandler(instance())
    }
    bind<QuestionTypeInterceptor>() with provider { QuestionTypeInterceptor() }
    bind<QuestionDomainModelMapper>() with provider {
        QuestionDomainModelMapper(instance(), instance())
    }
    bind<QuestionRepository>() with provider {
        QuestionRepositoryImpl(
            api = instance(),
            mapper = instance(),
            resourceManager = instance(),
            exceptionHandler = instance(),
            dispatcher = instance()
        )
    }
    bind<OkHttpClient>() with singleton {
        val questionTypeInterceptor: QuestionTypeInterceptor = instance()
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(questionTypeInterceptor)
        clientBuilder.build()
    }
    bind<QuestionApi>() with singleton {
        val okHttpClient: OkHttpClient = instance()
        val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.TRIVIA_DB_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        builder.create(QuestionApi::class.java)
    }
}