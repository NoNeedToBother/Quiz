package ru.kpfu.itis.paramonov.quiz

import android.app.Application
import com.google.firebase.FirebaseApp
import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import ru.kpfu.itis.paramonov.common.di.FeatureContainer
import ru.kpfu.itis.paramonov.common.di.dependencies.CommonApi
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseApi
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseContainer
import ru.kpfu.itis.paramonov.quiz.di.AppComponent
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependenciesProvider
import ru.kpfu.itis.paramonov.quiz.di.firebase.DaggerFirebaseComponent
import ru.kpfu.itis.paramonov.quiz.di.firebase.DaggerFirebaseComponent_FirebaseDependenciesComponent
import ru.kpfu.itis.paramonov.quiz.di.firebase.FirebaseComponent
import javax.inject.Inject

class App: Application(), FeatureContainer, FirebaseContainer {
    @Inject
    lateinit var featureHolderManager: FeatureHolderManager

    @Inject
    lateinit var dependencies: ComponentDependenciesProvider

    private lateinit var appComponent: AppComponent

    private lateinit var firebaseComponent: FirebaseComponent

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initFirebase()
        initAppComponent()
        initFirebaseComponent()
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

    override fun <T> getFeature(key: Class<*>): T {
        return featureHolderManager.getFeature<T>(key)!!
    }

    override fun releaseFeature(key: Class<*>) {
        featureHolderManager.releaseFeature(key)
    }

    override fun commonApi(): CommonApi {
        return appComponent
    }

    override fun firebaseApi(): FirebaseApi {
        return firebaseComponent
    }
}