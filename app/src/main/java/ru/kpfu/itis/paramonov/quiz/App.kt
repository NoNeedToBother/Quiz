package ru.kpfu.itis.paramonov.quiz

import android.app.Application
import com.google.firebase.FirebaseApp
import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import ru.kpfu.itis.paramonov.common_android.di.FeatureContainer
import ru.kpfu.itis.paramonov.common_android.di.CommonApi
import ru.kpfu.itis.paramonov.firebase.domain.api.FirebaseApi
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependenciesContainer
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependenciesContainer
import ru.kpfu.itis.paramonov.local_database_api.domain.api.LocalDatabaseApi
import ru.kpfu.itis.paramonov.question_api.domain.api.QuestionApi
import ru.kpfu.itis.paramonov.quiz.di.AppComponent
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependenciesProvider
import ru.kpfu.itis.paramonov.quiz.di.firebase.DaggerFirebaseComponent
import ru.kpfu.itis.paramonov.quiz.di.firebase.DaggerFirebaseComponent_FirebaseDependenciesComponent
import ru.kpfu.itis.paramonov.quiz.di.firebase.FirebaseComponent
import ru.kpfu.itis.paramonov.quiz.di.local_database.DaggerLocalDatabaseComponent
import ru.kpfu.itis.paramonov.quiz.di.local_database.DaggerLocalDatabaseComponent_LocalDatabaseDependenciesComponent
import ru.kpfu.itis.paramonov.quiz.di.local_database.LocalDatabaseComponent
import ru.kpfu.itis.paramonov.quiz.di.questions.DaggerQuestionsComponent
import ru.kpfu.itis.paramonov.quiz.di.questions.DaggerQuestionsComponent_QuestionDependenciesComponent
import ru.kpfu.itis.paramonov.quiz.di.questions.QuestionsComponent
import javax.inject.Inject

class App: Application(), FeatureContainer, FeatureAuthenticationDependenciesContainer,
    FeatureQuestionsDependenciesContainer {
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
        initFirebase()
        initAppComponent()
        initFirebaseComponent()
        initLocalDatabaseComponent()
        initQuestionsComponent()
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
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