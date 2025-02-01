package ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.model.UpdateKeys
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveUserSettingsUseCase
import javax.inject.Inject

class SaveUserSettingsUseCaseImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
): SaveUserSettingsUseCase {

    override suspend operator fun invoke(settings: Map<String, String>) {
        val pairs: MutableList<Pair<String, String>> = mutableListOf()
        for (key in settings.keys) {
            when(key) {
                USERNAME_KEY -> pairs.add(Pair(UpdateKeys.UPDATE_USERNAME_KEY, settings.getValue(key)))
                INFO_KEY -> pairs.add(Pair(UpdateKeys.UPDATE_INFO_KEY, settings.getValue(key)))
            }
        }
        withContext(dispatcher) {
            repository.updateUser(*pairs.toTypedArray())
        }
    }

    companion object {
        const val USERNAME_KEY = "username"
        const val INFO_KEY = "info"
    }
}