package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerData
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionData
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionUiModel
import java.util.ArrayList
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

    fun updateChosenAnswers(pos: Int, chosenPos: Int) {
        viewModelScope.launch {
            val value = _questionList[pos].value
            val answerListCopy = ArrayList<AnswerData>()
            for (answerData in value.answers) {
                answerListCopy.add(answerData.copy())
            }
            val question = QuestionData(
                value.text, answerListCopy
            )
            for (answerPos in question.answers.indices) {
                val answer = question.answers[answerPos]
                if (answerPos == chosenPos) answer.chosen = answer.chosen.not()
                else answer.chosen = false
            }
            _questionList[pos].value = question
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