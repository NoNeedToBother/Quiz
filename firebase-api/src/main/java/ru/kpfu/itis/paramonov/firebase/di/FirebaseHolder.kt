package ru.kpfu.itis.paramonov.firebase.di

import ru.kpfu.itis.paramonov.common.di.FeatureApiHolder
import ru.kpfu.itis.paramonov.common.di.FeatureContainer
import javax.inject.Inject

class FirebaseHolder @Inject constructor(
    featureContainer: FeatureContainer
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val firebaseDependencies = DaggerFirebaseComponent_FirebaseDependenciesComponent.builder()
            .commonApi(commonApi())
            .build()
        return DaggerFirebaseComponent.builder()
            .firebaseDependencies(firebaseDependencies)
            .build()
    }
}