package ru.kpfu.itis.paramonov.feature_questions.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import javax.inject.Inject

class GetMaxScoreUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val resultRepository: ResultRepository
) {

    suspend operator fun invoke(): Double {
        return withContext(dispatcher) {
            resultRepository.getMaxScore()
        }
    }
}