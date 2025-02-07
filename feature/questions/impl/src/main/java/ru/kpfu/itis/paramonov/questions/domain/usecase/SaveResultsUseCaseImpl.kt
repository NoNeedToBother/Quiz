package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.api.model.Result
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.repository.UserRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveResultsUseCase

class SaveResultsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val userRepository: UserRepository,
    private val resultRepository: ResultRepository,
    private val resourceManager: ResourceManager
): SaveResultsUseCase {

    override suspend operator fun invoke(
        difficulty: Difficulty, category: Category,
        gameMode: GameMode, time: Int, correct: Int, total: Int
    ): Double {
        return withContext(dispatcher) {
            val currentUser = userRepository.getCurrentUser()
                ?: throw RuntimeException(resourceManager.getString(R.string.user_not_present))

            val result = Result(
                user = currentUser,
                time = time,
                correct = correct,
                total = total,
                difficulty = difficulty,
                category = category,
                gameMode = gameMode
            )
            resultRepository.save(result)
        }
    }
}