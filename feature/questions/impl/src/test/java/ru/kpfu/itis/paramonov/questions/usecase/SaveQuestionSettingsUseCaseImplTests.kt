package ru.kpfu.itis.paramonov.questions.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveQuestionSettingsUseCaseImpl

class SaveQuestionSettingsUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveQuestionSettingsUseCaseImpl_success_allNotNull() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository>()
        doNothing().`when`(questionSettingsRepository).saveDifficulty(Difficulty.EASY)
        doNothing().`when`(questionSettingsRepository).saveCategory(Category.GENERAL)
        doNothing().`when`(questionSettingsRepository).saveGameMode(GameMode.EXPERT)

        val impl = SaveQuestionSettingsUseCaseImpl(dispatcher, questionSettingsRepository)
        impl.invoke(Difficulty.EASY, Category.GENERAL, GameMode.EXPERT)

        verify(questionSettingsRepository).saveDifficulty(Difficulty.EASY)
        verify(questionSettingsRepository).saveCategory(Category.GENERAL)
        verify(questionSettingsRepository).saveGameMode(GameMode.EXPERT)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveQuestionSettingsUseCaseImpl_success_difficultyNull() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository>()
        doNothing().`when`(questionSettingsRepository).saveCategory(Category.GENERAL)
        doNothing().`when`(questionSettingsRepository).saveGameMode(GameMode.EXPERT)

        val impl = SaveQuestionSettingsUseCaseImpl(dispatcher, questionSettingsRepository)
        impl.invoke(null, Category.GENERAL, GameMode.EXPERT)

        verify(questionSettingsRepository, times(0)).saveDifficulty(Difficulty.EASY)
        verify(questionSettingsRepository).saveCategory(Category.GENERAL)
        verify(questionSettingsRepository).saveGameMode(GameMode.EXPERT)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveQuestionSettingsUseCaseImpl_success_categoryNull() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository>()
        doNothing().`when`(questionSettingsRepository).saveDifficulty(Difficulty.EASY)
        doNothing().`when`(questionSettingsRepository).saveGameMode(GameMode.EXPERT)

        val impl = SaveQuestionSettingsUseCaseImpl(dispatcher, questionSettingsRepository)
        impl.invoke(Difficulty.EASY, null, GameMode.EXPERT)

        verify(questionSettingsRepository).saveDifficulty(Difficulty.EASY)
        verify(questionSettingsRepository, times(0)).saveCategory(Category.GENERAL)
        verify(questionSettingsRepository).saveGameMode(GameMode.EXPERT)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveQuestionSettingsUseCaseImpl_success_gameModeNull() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository>()
        doNothing().`when`(questionSettingsRepository).saveDifficulty(Difficulty.EASY)
        doNothing().`when`(questionSettingsRepository).saveCategory(Category.GENERAL)

        val impl = SaveQuestionSettingsUseCaseImpl(dispatcher, questionSettingsRepository)
        impl.invoke(Difficulty.EASY, Category.GENERAL, null)

        verify(questionSettingsRepository).saveDifficulty(Difficulty.EASY)
        verify(questionSettingsRepository).saveCategory(Category.GENERAL)
        verify(questionSettingsRepository, times(0)).saveGameMode(GameMode.EXPERT)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveQuestionSettingsUseCaseImpl_success_allNull() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository>()

        val impl = SaveQuestionSettingsUseCaseImpl(dispatcher, questionSettingsRepository)
        impl.invoke(null, null, null)

        verify(questionSettingsRepository, times(0)).saveDifficulty(Difficulty.EASY)
        verify(questionSettingsRepository, times(0)).saveCategory(Category.GENERAL)
        verify(questionSettingsRepository, times(0)).saveGameMode(GameMode.EXPERT)
    }
}