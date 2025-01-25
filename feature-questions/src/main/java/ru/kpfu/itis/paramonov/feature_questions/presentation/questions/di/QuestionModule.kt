package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveResultsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class QuestionModule {
    @Provides
    fun questionsViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): QuestionsViewModel {
        return ViewModelProvider(fragment, factory)[QuestionsViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(QuestionsViewModel::class)
    fun provideQuestionsViewModel(
        getQuestionsUseCase: GetQuestionsUseCase,
        saveQuestionsUseCase: SaveQuestionsUseCase,
        saveResultsUseCase: SaveResultsUseCase,
        getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
        mainMenuRouter: MainMenuRouter,
        questionsRouter: QuestionsRouter
    ): ViewModel {
        return QuestionsViewModel(
            getQuestionsUseCase = getQuestionsUseCase,
            saveQuestionsUseCase = saveQuestionsUseCase,
            saveResultsUseCase = saveResultsUseCase,
            getQuestionSettingsUseCase = getQuestionSettingsUseCase,
            mainMenuRouter = mainMenuRouter,
            questionsRouter = questionsRouter)
    }
}