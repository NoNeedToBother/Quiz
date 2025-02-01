package ru.kpfu.itis.paramonov.profiles.api.api

import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository

interface FeatureProfilesApi {

    fun friendRepository(): FriendRepository

    fun resultRepository(): ResultRepository

    fun userRepository(): UserRepository

}