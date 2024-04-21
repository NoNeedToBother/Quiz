package ru.kpfu.itis.paramonov.quiz.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseViewModel
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator

class MainMenuViewModel(private val navigator: Navigator): BaseViewModel() {

    fun goToQuestionSettings() {
        viewModelScope.launch {
            navigator.goToQuestionSettings()
        }
    }

    fun goToQuestions() {
        viewModelScope.launch {
            navigator.goToQuestion()
        }
    }
}