package ru.kpfu.itis.paramonov.questions.presentation.questions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.QuestionsViewModel
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import ru.kpfu.itis.paramonov.navigation.QuestionsRouter
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveResultsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataUiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveQuestionsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveResultsUseCaseImpl

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
    fun getQuestionsUseCase(impl: GetQuestionsUseCaseImpl): GetQuestionsUseCase = impl

    @Provides
    fun saveQuestionsUseCase(impl: SaveQuestionsUseCaseImpl): SaveQuestionsUseCase = impl

    @Provides
    fun saveResultsUseCase(impl: SaveResultsUseCaseImpl): SaveResultsUseCase = impl

    @Provides
    fun getQuestionSettingsUseCase(impl: GetQuestionSettingsUseCaseImpl): GetQuestionSettingsUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(QuestionsViewModel::class)
    fun provideQuestionsViewModel(
        getQuestionsUseCase: GetQuestionsUseCase,
        saveQuestionsUseCase: SaveQuestionsUseCase,
        saveResultsUseCase: SaveResultsUseCase,
        getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
        questionDataUiModelMapper: QuestionDataUiModelMapper,
        questionDataApiModelMapper: QuestionDataApiModelMapper,
        mainMenuRouter: MainMenuRouter,
        questionsRouter: QuestionsRouter
    ): ViewModel {
        return QuestionsViewModel(
            getQuestionsUseCase = getQuestionsUseCase,
            saveQuestionsUseCase = saveQuestionsUseCase,
            saveResultsUseCase = saveResultsUseCase,
            getQuestionSettingsUseCase = getQuestionSettingsUseCase,
            mainMenuRouter = mainMenuRouter,
            questionsRouter = questionsRouter,
            questionDataUiModelMapper = questionDataUiModelMapper,
            questionDataApiModelMapper = questionDataApiModelMapper
        )
    }
}