package ru.kpfu.itis.paramonov.feature_profiles.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.feature_profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import javax.inject.Inject

class GetCurrentUserLastResultsUseCase @Inject constructor(
    private val resultRepository: ResultRepository,
    private val dispatcher: CoroutineDispatcher,
    private val resultUiModelMapper: ResultUiModelMapper
) {

    suspend operator fun invoke(max: Int): List<ResultUiModel> {
        return withContext(dispatcher) {
            resultRepository.getLastResults(max).map {
                result -> resultUiModelMapper.map(result)
            }
        }
    }
}