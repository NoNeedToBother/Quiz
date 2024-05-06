package ru.kpfu.itis.paramonov.feature_users.di

import ru.kpfu.itis.paramonov.common.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.common_android.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import javax.inject.Inject

@ApplicationScope
class FeatureUsersHolder @Inject constructor(
    private val dependenciesContainer: FeatureUsersDependenciesContainer,
    private val authenticationRouter: AuthenticationRouter
): FeatureApiHolder(dependenciesContainer) {

    override fun initializeDependencies(): Any {
        val featureUsersDependencies = DaggerFeatureUsersComponent_FeatureUsersDependenciesComponent.builder()
            .commonApi(commonApi())
            .firebaseApi(dependenciesContainer.firebaseApi())
            .build()

        return DaggerFeatureUsersComponent.builder()
            .dependencies(featureUsersDependencies)
            .authenticationRouter(authenticationRouter)
            .build()
    }
}