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
import ru.kpfu.itis.paramonov.firebase.di.FirebaseApi
import ru.kpfu.itis.paramonov.firebase.di.FirebaseHolder
import ru.kpfu.itis.paramonov.quiz.App

@Module
interface ComponentHolderModule {

    @ApplicationScope
    @Binds
    fun provideFeatureContainer(application: App): FeatureContainer

    @ApplicationScope
    @Binds
    @ClassKey(FeatureAuthenticationDependencies::class)
    @IntoMap
    fun provideAuthenticationFeatureHolder(authenticationFeatureHolder: FeatureAuthenticationHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(FirebaseApi::class)
    @IntoMap
    fun provideFirebaseHolder(firebaseHolder: FirebaseHolder): FeatureApiHolder
}