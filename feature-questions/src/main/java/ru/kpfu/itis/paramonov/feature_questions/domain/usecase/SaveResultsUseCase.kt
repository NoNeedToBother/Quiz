package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.firebase.external.domain.model.Result
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class SaveResultsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val resultRepository: ResultRepository,
    private val resourceManager: ResourceManager,
    private val mapper: QuestionSettingsApiModelMapper
) {

    suspend operator fun invoke(
        difficultyUiModel: DifficultyUiModel, categoryUiModel: CategoryUiModel,
        gameModeUiModel: GameModeUiModel, time: Int, correct: Int, total: Int
    ): Double {
        return withContext(dispatcher) {
            val currentUser = userRepository.getCurrentUser()
                ?: throw RuntimeException(resourceManager.getString(R.string.user_not_present))

            val result = Result(
                user = currentUser,
                time = time,
                correct = correct,
                total = total,
                difficulty = mapper.mapDifficulty(difficultyUiModel),
                category = mapper.mapCategory(categoryUiModel),
                gameMode = mapper.mapGameMode(gameModeUiModel)
            )
            resultRepository.save(result)
        }

    }
}