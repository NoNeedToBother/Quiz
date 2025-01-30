package ru.kpfu.itis.paramonov.questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.usecase.GetMaxScoreUseCase
import javax.inject.Inject

class GetMaxScoreUseCaseImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val resultRepository: ResultRepository
): GetMaxScoreUseCase {

    override suspend operator fun invoke(): Double {
        return withContext(dispatcher) {
            resultRepository.getMaxScore()
        }
    }
}