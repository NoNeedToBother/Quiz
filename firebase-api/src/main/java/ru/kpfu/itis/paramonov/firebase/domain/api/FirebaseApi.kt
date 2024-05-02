package ru.kpfu.itis.paramonov.firebase.domain.api

import ru.kpfu.itis.paramonov.firebase.domain.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.firebase.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

interface FirebaseApi {
    fun userRepository(): UserRepository

    fun authenticationRepository(): AuthenticationRepository

    fun resultRepository(): ResultRepository
}