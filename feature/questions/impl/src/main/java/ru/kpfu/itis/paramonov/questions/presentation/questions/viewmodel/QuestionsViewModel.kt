package ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.api.usecase.GetMaxScoreUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveResultsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataUiModelMapper
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.AnswerDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.ResultDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.QuestionsScreenSideEffect
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.QuestionsScreenState
import java.util.Timer
import java.util.TimerTask

class QuestionsViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveQuestionsUseCase: SaveQuestionsUseCase,
    private val saveResultsUseCase: SaveResultsUseCase,
    private val getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
    private val getMaxScoreUseCase: GetMaxScoreUseCase,
    private val questionDataUiModelMapper: QuestionDataUiModelMapper,
    private val questionDataApiModelMapper: QuestionDataApiModelMapper,
    private val resourceManager: ResourceManager,
): ViewModel(), ContainerHost<QuestionsScreenState, QuestionsScreenSideEffect> {

    override val container = container<QuestionsScreenState, QuestionsScreenSideEffect>(QuestionsScreenState())

    private var timer: Timer? = null

    fun getMaxScore() = intent {
        val maxScore = getMaxScoreUseCase.invoke()
        reduce { state.copy(maxScore = maxScore) }
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

    fun getQuestions() = intent {
        try {
            val questions = getQuestionsUseCase.invoke().map {
                questionDataUiModelMapper.map(it)
            }

            reduce { state.copy(questions = questions) }
            saveQuestions()
            startClockTicking()
        } catch (ex: Throwable) {
            postSideEffect(QuestionsScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.get_questions_fail_msg),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun onQuestionsEnd() = intent {
        try {
            val time = state.time
            var correct = 0
            var total = 0
            val settings = getQuestionSettingsUseCase.invoke()
            for (question in state.questions) {
                total++
                for (answer in question.answers) {
                    if (answer.correct && answer.chosen) correct++
                }
            }
            val score = saveResultsUseCase.invoke(
                difficulty = settings.difficulty,
                category = settings.category,
                gameMode = settings.gameMode,
                time = time, correct = correct, total = total
            )
            val resultData = ResultDataUiModel(
                difficulty = settings.difficulty.name.normalizeEnumName(),
                category = settings.category.name.normalizeEnumName(),
                gameMode = settings.gameMode.name.normalizeEnumName(),
                time = time,
                correct = correct,
                total = total,
                score = score
            )
            reduce { state.copy(result = resultData) }
        } catch (ex: Throwable) {
            postSideEffect(QuestionsScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.save_results_fail),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    private fun startClockTicking() = intent {
        if (timer == null) {
            timer = Timer()
            var time = 0
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    viewModelScope.launch {
                        reduce { state.copy(time = time) }
                    }
                    time++
                }
            }, 0, ONE_SECOND_MILLIS)
        }
    }

    fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    override fun onCleared() {
        stopTimer()
        super.onCleared()
    }

    private fun saveQuestions() = intent {
        saveQuestionsUseCase.invoke(state.questions.map { question -> questionDataApiModelMapper.map(question) })
    }

    companion object {
        private const val ONE_SECOND_MILLIS = 1000L
    }
}
