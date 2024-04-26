package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import java.util.Timer
import java.util.TimerTask
import kotlin.collections.ArrayList

class QuestionsViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveQuestionsUseCase: SaveQuestionsUseCase
): BaseViewModel() {

    private val _questionsDataFlow = MutableStateFlow<QuestionDataResult?>(null)

    val questionsDataFlow: StateFlow<QuestionDataResult?> get() = _questionsDataFlow

    private val _questionList = mutableListOf<MutableStateFlow<QuestionDataUiModel>>()

    private val _currentTimeFlow = MutableStateFlow(0)

    private var timer: Timer? = null

    val currentTimeFlow: StateFlow<Int> get() = _currentTimeFlow

    fun getQuestionFlow(pos: Int): StateFlow<QuestionDataUiModel> {
        return _questionList[pos].asStateFlow()
    }

    fun getQuestions() {
        viewModelScope.launch {
            try {
                val questionData = getQuestionsUseCase.invoke()

                for (pos in questionData.indices) {
                    _questionList.add(pos,
                        MutableStateFlow(questionData[pos])
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

    fun saveQuestions() {
        viewModelScope.launch {
            val questions = ArrayList<QuestionDataUiModel>()
            for (question in _questionList) {
                questions.add(question.value)
            }
            saveQuestionsUseCase.invoke(questions)
        }
    }

    sealed interface QuestionDataResult: Result {
        class Success(private val result: List<QuestionDataUiModel>): QuestionDataResult, Result.Success<List<QuestionDataUiModel>> {
            override fun getValue(): List<QuestionDataUiModel> = result
        }

        class Failure(private val ex: Throwable): QuestionDataResult, Result.Failure {
            override fun getException(): Throwable = ex
        }
    }
}