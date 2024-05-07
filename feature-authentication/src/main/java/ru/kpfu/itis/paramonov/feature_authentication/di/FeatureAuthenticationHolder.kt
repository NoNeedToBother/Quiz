package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
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
            .firebaseApi(dependenciesContainer.firebaseApi())
            .commonApi(commonApi())
            .build()
        return DaggerFeatureAuthenticationComponent.builder()
            .authenticationRouter(authenticationRouter)
            .mainMenuRouter(mainMenuRouter)
            .dependencies(authFeatureDependencies)
            .build()
    }
}