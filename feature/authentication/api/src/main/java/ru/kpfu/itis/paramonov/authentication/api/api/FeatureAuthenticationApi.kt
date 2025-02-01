package ru.kpfu.itis.paramonov.authentication.api.api

import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository

interface FeatureAuthenticationApi {

    fun authenticationRepository(): AuthenticationRepository

}