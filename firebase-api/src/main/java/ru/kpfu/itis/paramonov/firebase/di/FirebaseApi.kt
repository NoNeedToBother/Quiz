package ru.kpfu.itis.paramonov.firebase.di

import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository

interface FirebaseApi {
    fun repository(): UserRepository
}