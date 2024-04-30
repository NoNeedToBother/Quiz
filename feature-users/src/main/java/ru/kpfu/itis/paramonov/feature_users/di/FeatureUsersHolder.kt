package ru.kpfu.itis.paramonov.feature_users.di

import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope

@ApplicationScope
class FeatureUsersHolder(
    dependenciesContainer: FeatureUsersDependenciesContainer,
): FeatureUsersApiHolder(dependenciesContainer) {

    override fun initializeDependencies(): Any {
        val featureUsersDependencies = DaggerFeatureUsersComponent_FeatureUsersDependenciesComponent.builder()
            .commonApi(commonApi())
            .firebaseApi(firebaseApi())
            .build()

        return DaggerFeatureUsersComponent.builder()
            .dependencies(featureUsersDependencies)
            .build()
    }
}