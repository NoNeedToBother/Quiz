package ru.kpfu.itis.paramonov.feature_authentication.di

import ru.kpfu.itis.paramonov.common.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.common.di.FeatureContainer
import ru.kpfu.itis.paramonov.common.di.scopes.ApplicationScope
import ru.kpfu.itis.paramonov.firebase.di.FirebaseApi
import javax.inject.Inject

@ApplicationScope
class FeatureAuthenticationHolder @Inject constructor(
    featureContainer: FeatureContainer
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val authFeatureDependencies = DaggerFeatureAuthenticationComponent_FeatureAuthenticationDependenciesComponent.builder()
            .firebaseApi(getFeature(FirebaseApi::class.java))
            .commonApi(commonApi())
            .build()
        return DaggerFeatureAuthenticationComponent.builder()
            .dependencies(authFeatureDependencies)
            .build()
    }
}