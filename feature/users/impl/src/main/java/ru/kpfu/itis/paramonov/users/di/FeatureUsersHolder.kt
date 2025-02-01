package ru.kpfu.itis.paramonov.users.di

import ru.kpfu.itis.paramonov.core.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.navigation.UserRouter
import javax.inject.Inject

class FeatureUsersHolder @Inject constructor(
    private val dependenciesContainer: FeatureUsersDependenciesContainer,
    private val userRouter: UserRouter
): FeatureApiHolder(dependenciesContainer) {
    override fun initializeDependencies(): Any {
        val featureProfilesDependencies = DaggerFeatureUsersComponent_FeatureUsersDependenciesComponent.builder()
            .commonApi(commonApi())
            .featureUsersApi(dependenciesContainer.featureUsersApi())
            .build()

        return DaggerFeatureUsersComponent.builder()
            .dependencies(featureProfilesDependencies)
            .userRouter(userRouter)
            .build()
    }
}