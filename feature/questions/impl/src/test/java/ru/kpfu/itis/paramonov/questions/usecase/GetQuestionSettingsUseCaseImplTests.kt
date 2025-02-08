package ru.kpfu.itis.paramonov.questions.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionSettingsUseCaseImpl

class GetQuestionSettingsUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getMaxScoreUseCaseImpl_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository> {
            onBlocking { getDifficulty() } doReturn Difficulty.EASY
            onBlocking { getCategory() } doReturn Category.GENERAL
            onBlocking { getGameMode() } doReturn GameMode.BLITZ
        }

        val impl = GetQuestionSettingsUseCaseImpl(dispatcher, questionSettingsRepository)
        val result = impl.invoke()

        assertEquals(Difficulty.EASY, result.difficulty)
        assertEquals(Category.GENERAL, result.category)
        assertEquals(GameMode.BLITZ, result.gameMode)

        verify(questionSettingsRepository).getDifficulty()
        verify(questionSettingsRepository).getCategory()
        verify(questionSettingsRepository).getGameMode()
    }
}