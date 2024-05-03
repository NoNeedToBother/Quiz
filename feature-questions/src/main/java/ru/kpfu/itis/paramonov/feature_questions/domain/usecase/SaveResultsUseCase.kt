package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.firebase.domain.model.Result
import ru.kpfu.itis.paramonov.firebase.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import javax.inject.Inject

class SaveResultsUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val resultRepository: ResultRepository,
    private val mapper: QuestionSettingsApiModelMapper
) {

    suspend operator fun invoke(
        difficultyUiModel: DifficultyUiModel, categoryUiModel: CategoryUiModel,
        gameModeUiModel: GameModeUiModel, time: Int, correct: Int, total: Int
    ): Double {
        return withContext(dispatcher) {
            val currentUser = userRepository.getCurrentUser()

            val result = Result(
                user = currentUser.get(),
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