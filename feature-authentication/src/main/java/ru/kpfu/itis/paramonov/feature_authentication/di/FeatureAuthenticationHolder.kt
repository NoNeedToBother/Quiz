package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.firebase.domain.FirebaseContainer
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import javax.inject.Inject

@ApplicationScope
class FeatureAuthenticationHolder @Inject constructor(
    featureContainer: FirebaseContainer,
    private val authenticationRouter: AuthenticationRouter
) : FirebaseFeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val authFeatureDependencies = DaggerFeatureAuthenticationComponent_FeatureAuthenticationDependenciesComponent.builder()
            .firebaseApi(firebaseApi())
            .commonApi(commonApi())
            .build()
        return DaggerFeatureAuthenticationComponent.builder()
            .authenticationRouter(authenticationRouter)
            .dependencies(authFeatureDependencies)
            .build()
    }
}