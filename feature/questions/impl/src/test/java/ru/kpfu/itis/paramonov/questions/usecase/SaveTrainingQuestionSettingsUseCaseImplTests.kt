package ru.kpfu.itis.paramonov.questions.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveTrainingQuestionSettingsUseCaseImpl

class SaveTrainingQuestionSettingsUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveTrainingQuestionSettingsUseCaseImpl_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val questionSettingsRepository = mock<QuestionSettingsRepository>()
        doNothing().`when`(questionSettingsRepository).saveLimit(anyInt())

        val impl = SaveTrainingQuestionSettingsUseCaseImpl(dispatcher, questionSettingsRepository)
        impl.invoke(10)

        verify(questionSettingsRepository).saveLimit(10)
    }
}