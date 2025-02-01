package ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings

interface SaveUserSettingsUseCase {

    suspend operator fun invoke(settings: Map<String, String>)

}