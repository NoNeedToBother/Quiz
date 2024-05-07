package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveResultsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments.ResultFragment
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import java.util.Timer
import java.util.TimerTask
import kotlin.collections.ArrayList

class QuestionsViewModel(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveQuestionsUseCase: SaveQuestionsUseCase,
    private val saveResultsUseCase: SaveResultsUseCase,
    private val getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
    private val mainMenuRouter: MainMenuRouter,
    private val questionsRouter: QuestionsRouter
): BaseQuestionsViewModel() {

    private val _currentTimeFlow = MutableStateFlow(0)

    private var timer: Timer? = null

    val currentTimeFlow: StateFlow<Int> get() = _currentTimeFlow

    private val _resultProceedingFlow = MutableStateFlow(false)
    val resultProceedingFlow: StateFlow<Boolean> get() = _resultProceedingFlow

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

    fun onQuestionsEnd() {
        viewModelScope.launch {
            _resultProceedingFlow.value = true
            try {
                val time = _currentTimeFlow.value
                var correct = 0
                var total = 0
                val settings = getQuestionSettingsUseCase.invoke()
                for (questionFlow in _questionList) {
                    val data = questionFlow.value
                    total++
                    for (answer in data.answers) {
                        if (answer.correct && answer.chosen) correct++
                    }
                }
                val score = saveResultsUseCase.invoke(
                    difficultyUiModel = settings.difficulty,
                    categoryUiModel = settings.category,
                    gameModeUiModel = settings.gameMode,
                    time = time, correct = correct, total = total
                )
                mainMenuRouter.popToMainMenu()
                questionsRouter.goToQuestionResults(
                    ResultFragment.ARGS_DIFFICULTY_KEY to settings.difficulty.name.normalizeEnumName(),
                    ResultFragment.ARGS_CATEGORY_KEY to settings.category.name.normalizeEnumName(),
                    ResultFragment.ARGS_GAME_MODE_KEY to settings.gameMode.name.normalizeEnumName(),
                    ResultFragment.ARGS_TIME_KEY to time,
                    ResultFragment.ARGS_CORRECT_AMOUNT_KEY to correct,
                    ResultFragment.ARGS_TOTAL_AMOUNT_KEY to total,
                    ResultFragment.ARGS_SCORE_KEY to score
                )
            } catch (ex: Throwable) {
                _questionsDataFlow.value = QuestionDataResult.Failure(ex)
            } finally {
                _resultProceedingFlow.value = false
            }
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

    private fun saveQuestions() {
        viewModelScope.launch {
            val questions = ArrayList<QuestionDataUiModel>()
            for (question in _questionList) {
                questions.add(question.value)
            }
            saveQuestionsUseCase.invoke(questions)
        }
    }
}