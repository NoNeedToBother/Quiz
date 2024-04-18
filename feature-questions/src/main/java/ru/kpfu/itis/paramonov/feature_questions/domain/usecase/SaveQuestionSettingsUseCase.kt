package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.local_database_api.domain.repository.SharedPreferencesRepository
import javax.inject.Inject

class SaveQuestionSettingsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: SharedPreferencesRepository
) {

    suspend operator fun invoke(difficulty: String?, category: String?, gameMode: String?) {
        withContext(dispatcher) {
            difficulty?.let {
                repository.saveDifficulty(it)
            }
            category?.let {
                repository.saveCategory(it)
            }
            gameMode?.let {
                repository.saveGameMode(it)
            }
        }
    }
}