package ru.kpfu.itis.paramonov.leaderboards.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetGameModeUseCaseImpl

class GetGameModeUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetGameModeUseCaseImpl_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val mockedGameMode = GameMode.BLITZ
        val questionSettingsRepository = mock<QuestionSettingsRepository> {
            onBlocking { getGameMode() } doReturn mockedGameMode
        }

        val impl = GetGameModeUseCaseImpl(dispatcher, questionSettingsRepository)
        val gameMode = impl.invoke()

        assert(gameMode == mockedGameMode)
        verify(questionSettingsRepository).getGameMode()
    }
}