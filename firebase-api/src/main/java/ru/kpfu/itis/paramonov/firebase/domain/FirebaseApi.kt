package ru.kpfu.itis.paramonov.firebase.domain

import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

interface FirebaseApi {
    fun userRepository(): UserRepository
}