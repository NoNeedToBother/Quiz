package ru.kpfu.itis.paramonov.profiles.api.usecase.settings

interface SaveUserSettingsUseCase {

    suspend operator fun invoke(settings: Map<String, String>)

}
