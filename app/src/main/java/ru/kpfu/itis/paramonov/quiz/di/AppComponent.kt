package ru.kpfu.itis.paramonov.quiz.di

import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentHolderModule
import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.di.CommonModule
import ru.kpfu.itis.paramonov.common.di.dependencies.CommonApi
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.quiz.App
import ru.kpfu.itis.paramonov.quiz.di.dependencies.ComponentDependenciesModule
import ru.kpfu.itis.paramonov.quiz.di.main.MainDependencies
@ApplicationScope
@Component(
    modules = [
        CommonModule::class,
        AppModule::class,
        ComponentHolderModule::class,
        ComponentDependenciesModule::class,
        NavigationModule::class,
        FeatureManagerModule::class,
    ],
)
interface AppComponent: CommonApi, MainDependencies {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

    companion object {
        fun init(application: App): AppComponent {
            return DaggerAppComponent
                .builder()
                .application(application)
                .build()
        }
    }
}