package ru.kpfu.itis.paramonov.quiz

import android.app.Application
import com.google.firebase.FirebaseApp
import ru.kpfu.itis.paramonov.quiz.di.dependencies.FeatureHolderManager
import ru.kpfu.itis.paramonov.common.di.FeatureContainer
import ru.kpfu.itis.paramonov.common.di.dependencies.CommonApi
import ru.kpfu.itis.paramonov.firebase.di.FirebaseApi
import ru.kpfu.itis.paramonov.quiz.di.AppComponent
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependenciesProvider
import javax.inject.Inject

class App: Application(), FeatureContainer {
    @Inject
    lateinit var featureHolderManager: FeatureHolderManager

    @Inject
    lateinit var dependencies: ComponentDependenciesProvider

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        initFirebase()
        initAppComponent()
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }

    private fun initAppComponent() {
        appComponent = AppComponent.init(this)
        appComponent.inject(this)
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
}