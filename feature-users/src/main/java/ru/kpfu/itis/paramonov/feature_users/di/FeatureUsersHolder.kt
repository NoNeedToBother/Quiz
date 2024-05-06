package ru.kpfu.itis.paramonov.feature_users.di

import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import javax.inject.Inject

@ApplicationScope
class FeatureUsersHolder @Inject constructor(
    dependenciesContainer: FeatureUsersDependenciesContainer,
    private val authenticationRouter: AuthenticationRouter
): FeatureUsersApiHolder(dependenciesContainer) {

    override fun initializeDependencies(): Any {
        val featureUsersDependencies = DaggerFeatureUsersComponent_FeatureUsersDependenciesComponent.builder()
            .commonApi(commonApi())
            .firebaseApi(firebaseApi())
            .build()

        return DaggerFeatureUsersComponent.builder()
            .dependencies(featureUsersDependencies)
            .authenticationRouter(authenticationRouter)
            .build()
    }
}