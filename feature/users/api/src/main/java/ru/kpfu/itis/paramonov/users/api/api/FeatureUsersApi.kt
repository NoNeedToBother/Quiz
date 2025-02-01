package ru.kpfu.itis.paramonov.users.api.api

import ru.kpfu.itis.paramonov.users.api.repository.UserRepository

interface FeatureUsersApi {

    fun userRepository(): UserRepository

}