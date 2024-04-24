package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionUiModel
import java.util.Timer
import java.util.TimerTask

class QuestionsViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase
): BaseViewModel() {

    private val _questionsDataFlow = MutableStateFlow<QuestionDataResult?>(null)

    val questionsDataFlow: StateFlow<QuestionDataResult?> get() = _questionsDataFlow

    private val _questionList = mutableListOf<MutableStateFlow<QuestionData>>()

    private val _currentTimeFlow = MutableStateFlow(0)

    private var timer: Timer? = null

    val currentTimeFlow: StateFlow<Int> get() = _currentTimeFlow

    fun getQuestionFlow(pos: Int): StateFlow<QuestionData> {
        return _questionList[pos].asStateFlow()
    }

    fun getQuestions() {
        viewModelScope.launch {
            try {
                val questionData = getQuestionsUseCase.invoke()

                for (pos in questionData.questions.indices) {
                    _questionList.add(pos,
                        MutableStateFlow(
                            QuestionData(questionData.questions[pos])
                        )
                    )
                }

                _questionsDataFlow.value = QuestionDataResult.Success(questionData)
                startClockTicking()

            } catch (ex: Throwable) {
                _questionsDataFlow.value = QuestionDataResult.Failure(ex)
            }

            _questionsDataFlow.value = null
        }
    }

    private fun startClockTicking() {
        viewModelScope.launch {
            if (timer == null) {
                timer = Timer()
                var time = 0
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        _currentTimeFlow.value = time
                        time++
                    }
                }, 0, 1000L)
            }
        }
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    sealed interface QuestionDataResult: Result {
        class Success(private val result: QuestionUiModel): QuestionDataResult, Result.Success<QuestionUiModel> {
            override fun getValue(): QuestionUiModel = result
        }

        class Failure(private val ex: Throwable): QuestionDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }

    class QuestionData(question: QuestionDataUiModel) {

        private var _answers: MutableList<AnswerData> = mutableListOf()

        val text: String

        init {
            val correct = question.answer
            val incorrect = question.incorrectAnswers
            _answers.add(AnswerData(correct, chosen = false, correct = true))
            for (incorrectAnswer in incorrect) {
                _answers.add(AnswerData(incorrectAnswer, chosen = false, correct = false))
            }
            _answers = ArrayList(_answers.shuffled())
            text = question.text
        }

        val answers: List<AnswerData> get() = _answers
    }
    data class AnswerData(
        val answer: String,
        val chosen: Boolean,
        val correct: Boolean
    )
}