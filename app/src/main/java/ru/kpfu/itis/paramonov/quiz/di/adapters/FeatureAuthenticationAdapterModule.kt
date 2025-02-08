package ru.kpfu.itis.paramonov.quiz.di.adapters

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.AuthenticationRepository as FirebaseAuthenticationRepository
import ru.kpfu.itis.paramonov.quiz.mapper.authentication.FirebaseUserToFeatureAuthenticationUserMapper

typealias AuthenticationAdapterUserMapper = FirebaseUserToFeatureAuthenticationUserMapper

val featureAuthenticationAdapterModule = DI.Module("featureAuthenticationAdapter") {
    bind<AuthenticationRepository>() with provider {
        object : AuthenticationRepository {
            val authenticationRepository: FirebaseAuthenticationRepository
                = instance()
            val firebaseUserToFeatureAuthenticationUserMapper: AuthenticationAdapterUserMapper = instance()

            override suspend fun registerUser(
                username: String,
                email: String,
                password: String,
                confirmPassword: String
            ): User = firebaseUserToFeatureAuthenticationUserMapper.map(
                authenticationRepository.registerUser(
                    username = username,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword
                )
            )

            override suspend fun authenticateUser(email: String, password: String): User =
                firebaseUserToFeatureAuthenticationUserMapper.map(
                    authenticationRepository.authenticateUser(email, password)
                )

            override suspend fun checkUserIsAuthenticated(): User? =
                authenticationRepository.checkUserIsAuthenticated()?.let {
                    firebaseUserToFeatureAuthenticationUserMapper.map(it)
                }
        }
    }
}
