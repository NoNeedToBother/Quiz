package ru.kpfu.itis.paramonov.profiles.api.usecase.settings

interface ConfirmCredentialsUseCase {

    suspend operator fun invoke(email: String, password: String)

}
