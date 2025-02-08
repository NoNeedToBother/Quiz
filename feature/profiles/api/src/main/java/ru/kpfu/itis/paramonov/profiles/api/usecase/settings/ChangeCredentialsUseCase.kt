package ru.kpfu.itis.paramonov.profiles.api.usecase.settings

interface ChangeCredentialsUseCase {

    suspend operator fun invoke(email: String?, password: String?)

}
