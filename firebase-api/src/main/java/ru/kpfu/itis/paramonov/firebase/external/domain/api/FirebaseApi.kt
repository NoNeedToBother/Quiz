package ru.kpfu.itis.paramonov.firebase.external.domain.api

import ru.kpfu.itis.paramonov.firebase.external.domain.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.FriendRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository

interface FirebaseApi {
    fun userRepository(): UserRepository

    fun authenticationRepository(): AuthenticationRepository

    fun resultRepository(): ResultRepository

    fun friendRepository(): FriendRepository
}