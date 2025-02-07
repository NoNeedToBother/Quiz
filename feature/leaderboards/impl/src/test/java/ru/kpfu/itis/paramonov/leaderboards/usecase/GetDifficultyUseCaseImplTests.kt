package ru.kpfu.itis.paramonov.leaderboards.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetDifficultyUseCaseImpl

class GetDifficultyUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetDifficultyUseCaseImpl_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val mockedDifficulty = Difficulty.EASY
        val questionSettingsRepository = mock<QuestionSettingsRepository> {
            onBlocking { getDifficulty() } doReturn mockedDifficulty
        }

        val impl = GetDifficultyUseCaseImpl(dispatcher, questionSettingsRepository)
        val difficulty = impl.invoke()

        assert(difficulty == mockedDifficulty)
        verify(questionSettingsRepository).getDifficulty()
    }
}