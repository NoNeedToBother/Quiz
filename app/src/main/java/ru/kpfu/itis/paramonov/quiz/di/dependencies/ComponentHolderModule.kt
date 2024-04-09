package ru.kpfu.itis.paramonov.quiz.di.dependencies

import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.common.di.FeatureContainer
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependencies
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationHolder
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseApi
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseContainer
import ru.kpfu.itis.paramonov.quiz.App
import ru.kpfu.itis.paramonov.quiz.di.firebase.FirebaseComponent

@Module
interface ComponentHolderModule {

    @ApplicationScope
    @Binds
    fun featureContainer(application: App): FeatureContainer

    @ApplicationScope
    @Binds
    fun firebaseContainer(application: App): FirebaseContainer

    @ApplicationScope
    @Binds
    @ClassKey(FeatureAuthenticationDependencies::class)
    @IntoMap
    fun authenticationFeatureHolder(authenticationFeatureHolder: FeatureAuthenticationHolder): FeatureApiHolder
}