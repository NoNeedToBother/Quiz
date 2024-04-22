package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionUiModel

class QuestionsViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase
): BaseViewModel() {

    private val _questionsDataFlow = MutableStateFlow<QuestionDataResult?>(null)

    val questionsDataFlow: StateFlow<QuestionDataResult?> get() = _questionsDataFlow

    fun getQuestions() {
        viewModelScope.launch {
            try {
                val questions = getQuestionsUseCase.invoke()
                _questionsDataFlow.value = QuestionDataResult.Success(questions)
            } catch (ex: Throwable) {
                _questionsDataFlow.value = QuestionDataResult.Failure(ex)
            }

            _questionsDataFlow.value = null
        }
    }

    sealed interface QuestionDataResult: Result {
        class Success(private val result: QuestionUiModel): QuestionDataResult, Result.Success<QuestionUiModel> {
            override fun getValue(): QuestionUiModel = result
        }

        class Failure(private val ex: Throwable): QuestionDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}