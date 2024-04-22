package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel

class QuestionViewModel: BaseViewModel() {

    private val _answerDataFlow = MutableStateFlow<List<AnswerData>>(listOf())

    val answerDataFlow: StateFlow<List<AnswerData>> get() = _answerDataFlow


    fun getShuffledQuestions(answer: String, incorrectAnswers: List<String>) {
        if (_answerDataFlow.value.isEmpty()) {
            val shuffledAnswers = shuffleAnswers(answer, incorrectAnswers)
            _answerDataFlow.value = shuffledAnswers.map {
                AnswerData(it, false)
            }
        }
    }

    private fun shuffleAnswers(answer: String, incorrectAnswers: List<String>): List<String> {
        val allAnswers = ArrayList(incorrectAnswers)
        allAnswers.add(answer)
        return allAnswers.shuffled()
    }

    data class AnswerData(
        val answer: String,
        val chosen: Boolean
    )
}