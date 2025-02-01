package ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.questions.api.usecase.GetSavedQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataUiModelMapper

class TrainingQuestionsViewModel(
    private val getSavedQuestionsUseCase: GetSavedQuestionsUseCase,
    private val questionDataUiModelMapper: QuestionDataUiModelMapper,
): BaseQuestionsViewModel() {

    override fun getQuestions() {
        viewModelScope.launch {
            try {
                val questionData = getSavedQuestionsUseCase.invoke().shuffled()

                for (pos in questionData.indices) {
                    _questionList.add(pos,
                        MutableStateFlow(questionDataUiModelMapper.map(questionData[pos]))
                    )
                }
                _questionsDataFlow.value = QuestionDataResult.Success(
                    questionData.map { question -> questionDataUiModelMapper.map(question) }
                )
            } catch (ex: Exception) {
                _questionsDataFlow.value = QuestionDataResult.Failure(ex)
            }

            _questionsDataFlow.value = null
        }
    }
}