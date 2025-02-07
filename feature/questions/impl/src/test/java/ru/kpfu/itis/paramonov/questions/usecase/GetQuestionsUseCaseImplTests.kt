package ru.kpfu.itis.paramonov.questions.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.questions.api.model.AnswerData
import ru.kpfu.itis.paramonov.questions.api.model.Question
import ru.kpfu.itis.paramonov.questions.api.model.QuestionData
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionsUseCaseImpl

class GetQuestionsUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getQuestionsUseCaseImpl_success_blitzGameMode() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository> {
            onBlocking { getDifficulty() } doReturn Difficulty.EASY
            onBlocking { getCategory() } doReturn Category.GENERAL
            onBlocking { getGameMode() } doReturn GameMode.BLITZ
        }
        val mockQuestionData = QuestionData(
            text = "aa",
            answers = listOf(AnswerData("bb", true, true))
        )
        val questionRepository = mock<QuestionRepository>() {
            onBlocking { getCategoryCode(Category.GENERAL) } doReturn 1
            onBlocking { getQuestions(10, Difficulty.EASY, 1) } doReturn
                    Question(listOf(mockQuestionData))
        }

        val impl = GetQuestionsUseCaseImpl(dispatcher, questionRepository, questionSettingsRepository)
        val result = impl.invoke()

        assert(result.size == 1)
        assert(result[0].text == "aa")
        assert(result[0].answers.contains(AnswerData("bb", true, true)))

        verify(questionRepository).getCategoryCode(Category.GENERAL)
        verify(questionSettingsRepository).getDifficulty()
        verify(questionSettingsRepository).getCategory()
        verify(questionSettingsRepository).getGameMode()
        verify(questionRepository).getQuestions(10, Difficulty.EASY, 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getQuestionsUseCaseImpl_success_challengeGameMode() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository> {
            onBlocking { getDifficulty() } doReturn Difficulty.EASY
            onBlocking { getCategory() } doReturn Category.GENERAL
            onBlocking { getGameMode() } doReturn GameMode.CHALLENGE
        }
        val mockQuestionData = QuestionData(
            text = "aa",
            answers = listOf(AnswerData("bb", true, true))
        )
        val questionRepository = mock<QuestionRepository>() {
            onBlocking { getCategoryCode(Category.GENERAL) } doReturn 1
            onBlocking { getQuestions(15, Difficulty.EASY, 1) } doReturn
                    Question(listOf(mockQuestionData))
        }

        val impl = GetQuestionsUseCaseImpl(dispatcher, questionRepository, questionSettingsRepository)
        val result = impl.invoke()

        assert(result.size == 1)
        assert(result[0].text == "aa")
        assert(result[0].answers.contains(AnswerData("bb", true, true)))

        verify(questionRepository).getCategoryCode(Category.GENERAL)
        verify(questionSettingsRepository).getDifficulty()
        verify(questionSettingsRepository).getCategory()
        verify(questionSettingsRepository).getGameMode()
        verify(questionRepository).getQuestions(15, Difficulty.EASY, 1)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getQuestionsUseCaseImpl_success_expertGameMode() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository> {
            onBlocking { getDifficulty() } doReturn Difficulty.EASY
            onBlocking { getCategory() } doReturn Category.GENERAL
            onBlocking { getGameMode() } doReturn GameMode.EXPERT
        }
        val mockQuestionData = QuestionData(
            text = "aa",
            answers = listOf(AnswerData("bb", true, true))
        )
        val questionRepository = mock<QuestionRepository>() {
            onBlocking { getCategoryCode(Category.GENERAL) } doReturn 1
            onBlocking { getQuestions(25, Difficulty.EASY, 1) } doReturn
                    Question(listOf(mockQuestionData))
        }

        val impl = GetQuestionsUseCaseImpl(dispatcher, questionRepository, questionSettingsRepository)
        val result = impl.invoke()

        assert(result.size == 1)
        assert(result[0].text == "aa")
        assert(result[0].answers.contains(AnswerData("bb", true, true)))

        verify(questionRepository).getCategoryCode(Category.GENERAL)
        verify(questionSettingsRepository).getDifficulty()
        verify(questionSettingsRepository).getCategory()
        verify(questionSettingsRepository).getGameMode()
        verify(questionRepository).getQuestions(25, Difficulty.EASY, 1)
    }
}