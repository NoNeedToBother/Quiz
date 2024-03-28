package ru.kpfu.itis.paramonov.quiz.di.dependencies

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.quiz.di.AppComponent
import ru.kpfu.itis.paramonov.quiz.di.main.MainDependencies

@Module
interface ComponentDependenciesModule {

    @Binds
    @IntoMap
    @ComponentDependenciesKey(MainDependencies::class)
    fun provideMainDependencies(component: AppComponent): ComponentDependencies
}