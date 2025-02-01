package ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel

abstract class BaseQuestionsViewModel: BaseViewModel() {

    protected val _questionsDataFlow = MutableStateFlow<QuestionDataResult?>(null)

    val questionsDataFlow: StateFlow<QuestionDataResult?> get() = _questionsDataFlow

    protected val _questionList = mutableListOf<MutableStateFlow<QuestionDataUiModel>>()

    fun getQuestionFlow(pos: Int): StateFlow<QuestionDataUiModel> {
        return _questionList[pos].asStateFlow()
    }

    abstract fun getQuestions()

    fun updateChosenAnswers(pos: Int, chosenPos: Int) {
        viewModelScope.launch {
            val value = _questionList[pos].value
            val answerListCopy = ArrayList<AnswerDataUiModel>()
            for (answerData in value.answers) {
                answerListCopy.add(answerData.copy())
            }
            val question = QuestionDataUiModel(value.text, answerListCopy).apply {
                difficulty = value.difficulty
                category = value.category
                gameMode = value.gameMode
            }
            for (answerPos in question.answers.indices) {
                val answer = question.answers[answerPos]
                if (answerPos == chosenPos) answer.chosen = answer.chosen.not()
                else answer.chosen = false
            }
            _questionList[pos].value = question
        }
    }

    sealed interface QuestionDataResult: Result {
        class Success(private val result: List<QuestionDataUiModel>): QuestionDataResult,
            Result.Success<List<QuestionDataUiModel>> {
            override fun getValue(): List<QuestionDataUiModel> = result
        }

        class Failure(private val ex: Throwable): QuestionDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}