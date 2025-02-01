package ru.kpfu.itis.paramonov.authentication.di

import ru.kpfu.itis.paramonov.core.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.core.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import javax.inject.Inject

@ApplicationScope
class FeatureAuthenticationHolder @Inject constructor(
    private val dependenciesContainer: FeatureAuthenticationDependenciesContainer,
    private val authenticationRouter: AuthenticationRouter,
    private val mainMenuRouter: MainMenuRouter
) : FeatureApiHolder(dependenciesContainer) {

    override fun initializeDependencies(): Any {
        val authFeatureDependencies = DaggerFeatureAuthenticationComponent_FeatureAuthenticationDependenciesComponent.builder()
            .featureAuthenticationApi(dependenciesContainer.featureAuthenticationApi())
            .commonApi(commonApi())
            .build()
        return DaggerFeatureAuthenticationComponent.builder()
            .authenticationRouter(authenticationRouter)
            .mainMenuRouter(mainMenuRouter)
            .dependencies(authFeatureDependencies)
            .build()
    }
}