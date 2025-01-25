package ru.kpfu.itis.paramonov.quiz

import android.app.Application
import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import ru.kpfu.itis.paramonov.core.di.FeatureContainer
import ru.kpfu.itis.paramonov.core.api.CommonApi
import ru.kpfu.itis.paramonov.core.api.ContextApi
import ru.kpfu.itis.paramonov.firebase.external.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependenciesContainer
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FeatureLeaderboardsDependenciesContainer
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependenciesContainer
import ru.kpfu.itis.paramonov.feature_profiles.di.FeatureProfilesDependenciesContainer
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersDependenciesContainer
import ru.kpfu.itis.paramonov.firebase.external.di.DaggerFirebaseComponent
import ru.kpfu.itis.paramonov.firebase.external.di.DaggerFirebaseComponent_FirebaseDependenciesComponent
import ru.kpfu.itis.paramonov.database.external.di.DaggerLocalDatabaseComponent
import ru.kpfu.itis.paramonov.database.external.di.DaggerLocalDatabaseComponent_LocalDatabaseDependenciesComponent
import ru.kpfu.itis.paramonov.database.external.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.network.external.domain.api.QuestionApi
import ru.kpfu.itis.paramonov.quiz.di.AppComponent
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependenciesProvider
import ru.kpfu.itis.paramonov.firebase.external.di.FirebaseComponent
import ru.kpfu.itis.paramonov.database.external.di.LocalDatabaseComponent
import ru.kpfu.itis.paramonov.network.external.di.DaggerQuestionsComponent
import ru.kpfu.itis.paramonov.network.external.di.DaggerQuestionsComponent_QuestionDependenciesComponent
import ru.kpfu.itis.paramonov.network.external.di.QuestionsComponent
import javax.inject.Inject

class App: Application(), FeatureContainer, FeatureAuthenticationDependenciesContainer,
    FeatureQuestionsDependenciesContainer, FeatureProfilesDependenciesContainer,
    FeatureLeaderboardsDependenciesContainer, FeatureUsersDependenciesContainer {
    @Inject
    lateinit var featureHolderManager: FeatureHolderManager

    @Inject
    lateinit var dependencies: ComponentDependenciesProvider

    private lateinit var appComponent: AppComponent

    private lateinit var firebaseComponent: FirebaseComponent

    private lateinit var localDatabaseComponent: LocalDatabaseComponent

    private lateinit var questionsComponent: QuestionsComponent

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initAppComponent()
        initFirebaseComponent()
        initLocalDatabaseComponent()
        initQuestionsComponent()
    }

    private fun initAppComponent() {
        appComponent = AppComponent.init(this)
        appComponent.inject(this)
    }

    private fun initFirebaseComponent() {
        val firebaseDependencies = DaggerFirebaseComponent_FirebaseDependenciesComponent.builder()
            .commonApi(appComponent)
            .build()

        firebaseComponent = DaggerFirebaseComponent.builder()
            .firebaseDependencies(firebaseDependencies)
            .build()
    }

    private fun initLocalDatabaseComponent() {
        val localDatabaseDependencies = DaggerLocalDatabaseComponent_LocalDatabaseDependenciesComponent.builder()
            .commonApi(appComponent)
            .contextApi(appComponent)
            .build()

        localDatabaseComponent = DaggerLocalDatabaseComponent.builder()
            .localDatabaseDependencies(localDatabaseDependencies)
            .build()
    }

    private fun initQuestionsComponent() {
        val questionsDependencies = DaggerQuestionsComponent_QuestionDependenciesComponent.builder()
            .commonApi(appComponent)
            .build()

        questionsComponent = DaggerQuestionsComponent.builder()
            .questionsDependencies(questionsDependencies)
            .build()
    }

    override fun <T> getFeature(key: Class<*>): T {
        return featureHolderManager.getFeature<T>(key)!!
    }

    override fun releaseFeature(key: Class<*>) {
        featureHolderManager.releaseFeature(key)
    }

    override fun commonApi(): CommonApi {
        return appComponent
    }

    override fun contextApi(): ContextApi {
        return appComponent
    }

    override fun questionApi(): QuestionApi {
        return questionsComponent
    }

    override fun firebaseApi(): FirebaseApi {
        return firebaseComponent
    }

    override fun localDatabaseApi(): LocalDatabaseApi {
        return localDatabaseComponent
    }
}