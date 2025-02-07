package ru.kpfu.itis.paramonov.questions.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetMaxScoreUseCaseImpl

class GetMaxScoreUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getMaxScoreUseCaseImpl_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resultRepository = mock<ResultRepository> {
            onBlocking { getMaxScore() } doReturn 10.0
        }

        val impl = GetMaxScoreUseCaseImpl(dispatcher, resultRepository)
        val result = impl.invoke()

        assert(result.equals(10.0))
        verify(resultRepository).getMaxScore()
    }
}