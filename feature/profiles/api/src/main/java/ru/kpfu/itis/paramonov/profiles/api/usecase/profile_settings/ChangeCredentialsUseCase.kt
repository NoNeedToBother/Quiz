package ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings

interface ChangeCredentialsUseCase {

    suspend operator fun invoke(email: String?, password: String?)

}