package ru.kpfu.itis.paramonov.profiles.api.usecase

interface LogoutUserUseCase {

    suspend operator fun invoke(onLogout: suspend () -> Unit)

}
