package ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.questions.api.usecase.GetSavedQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataUiModelMapper
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.TrainingQuestionsScreenSideEffect
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.TrainingQuestionsScreenState

class TrainingQuestionsViewModel(
    private val getSavedQuestionsUseCase: GetSavedQuestionsUseCase,
    private val questionDataUiModelMapper: QuestionDataUiModelMapper,
): ViewModel(), ContainerHost<TrainingQuestionsScreenState, TrainingQuestionsScreenSideEffect> {

    override val container = container<TrainingQuestionsScreenState, TrainingQuestionsScreenSideEffect>(
        TrainingQuestionsScreenState()
    )

    fun getQuestions() = intent {
        try {
            val questions = getSavedQuestionsUseCase.invoke().shuffled().map {
                questionDataUiModelMapper.map(it)
            }
            reduce { state.copy(questions = questions) }
        } catch (ex: Exception) {

        }
    }

    fun updateChosenAnswers(pos: Int, chosenPos: Int) = intent {
        val value = state.questions[pos]
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
        val newQuestions = ArrayList(state.questions)
        newQuestions[pos] = question

        reduce { state.copy(questions = newQuestions) }
    }
}
