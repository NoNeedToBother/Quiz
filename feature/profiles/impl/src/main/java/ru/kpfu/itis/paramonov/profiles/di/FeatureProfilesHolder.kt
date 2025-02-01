package ru.kpfu.itis.paramonov.profiles.di

import ru.kpfu.itis.paramonov.core.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.core.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import javax.inject.Inject

@ApplicationScope
class FeatureProfilesHolder @Inject constructor(
    private val dependenciesContainer: FeatureProfilesDependenciesContainer,
    private val authenticationRouter: AuthenticationRouter
): FeatureApiHolder(dependenciesContainer) {

    override fun initializeDependencies(): Any {
        val featureProfileDependencies = DaggerFeatureProfilesComponent_FeatureProfilesDependenciesComponent.builder()
            .commonApi(commonApi())
            .featureProfilesApi(dependenciesContainer.featureProfilesApi())
            .build()

        return DaggerFeatureProfilesComponent.builder()
            .dependencies(featureProfileDependencies)
            .authenticationRouter(authenticationRouter)
            .build()
    }
}