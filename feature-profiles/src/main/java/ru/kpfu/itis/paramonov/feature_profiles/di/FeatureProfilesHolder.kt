package ru.kpfu.itis.paramonov.feature_profiles.di

import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
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
            .firebaseApi(dependenciesContainer.firebaseApi())
            .build()

        return DaggerFeatureProfilesComponent.builder()
            .dependencies(featureProfileDependencies)
            .authenticationRouter(authenticationRouter)
            .build()
    }
}