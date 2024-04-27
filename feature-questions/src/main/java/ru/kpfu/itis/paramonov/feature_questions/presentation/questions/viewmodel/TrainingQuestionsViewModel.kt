package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetSavedQuestionsUseCase

class TrainingQuestionsViewModel(
    private val getSavedQuestionsUseCase: GetSavedQuestionsUseCase
): BaseQuestionsViewModel() {

    override fun getQuestions() {
        viewModelScope.launch {
            try {
                val questionData = getSavedQuestionsUseCase.invoke().shuffled()

                for (pos in questionData.indices) {
                    _questionList.add(pos,
                        MutableStateFlow(questionData[pos])
                    )
                }
                _questionsDataFlow.value = QuestionDataResult.Success(questionData)
            } catch (ex: Exception) {
                _questionsDataFlow.value = QuestionDataResult.Failure(ex)
            }

            _questionsDataFlow.value = null
        }
    }
}