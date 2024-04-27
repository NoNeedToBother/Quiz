package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import java.util.Timer
import java.util.TimerTask
import kotlin.collections.ArrayList

class QuestionsViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveQuestionsUseCase: SaveQuestionsUseCase
): BaseQuestionsViewModel() {

    private val _currentTimeFlow = MutableStateFlow(0)

    private var timer: Timer? = null

    val currentTimeFlow: StateFlow<Int> get() = _currentTimeFlow

    override fun getQuestions() {
        viewModelScope.launch {
            try {
                val questionData = getQuestionsUseCase.invoke()

                for (pos in questionData.indices) {
                    _questionList.add(pos,
                        MutableStateFlow(questionData[pos])
                    )
                }

                _questionsDataFlow.value = QuestionDataResult.Success(questionData)
                saveQuestions()
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

    fun saveQuestions() {
        viewModelScope.launch {
            val questions = ArrayList<QuestionDataUiModel>()
            for (question in _questionList) {
                questions.add(question.value)
            }
            saveQuestionsUseCase.invoke(questions)
        }
    }
}