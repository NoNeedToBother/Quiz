package ru.kpfu.itis.paramonov.questions.presentation.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.GetTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetTrainingQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveTrainingQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.questions.presentation.settings.viewmodel.QuestionSettingsViewModel

@Module(
    includes = [
      ViewModelModule::class
    ]
)
class QuestionSettingsModule {

    @Provides
    fun questionSettingsViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): QuestionSettingsViewModel {
        return ViewModelProvider(fragment, factory)[QuestionSettingsViewModel::class.java]
    }

    @Provides
    fun getQuestionSettingsUseCase(impl: GetQuestionSettingsUseCaseImpl): GetQuestionSettingsUseCase = impl

    @Provides
    fun saveQuestionSettingsUseCase(impl: SaveQuestionSettingsUseCaseImpl): SaveQuestionSettingsUseCase = impl

    @Provides
    fun saveTrainingQuestionSettingsUseCase(impl: SaveTrainingQuestionSettingsUseCaseImpl): SaveTrainingQuestionSettingsUseCase = impl

    @Provides
    fun getTrainingQuestionSettingsUseCase(impl: GetTrainingQuestionSettingsUseCaseImpl): GetTrainingQuestionSettingsUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(QuestionSettingsViewModel::class)
    fun provideQuestionSettingsViewModel(
        getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
        saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase,
        saveTrainingQuestionSettingsUseCase: SaveTrainingQuestionSettingsUseCase,
        getTrainingQuestionSettingsUseCase: GetTrainingQuestionSettingsUseCase,
        questionSettingsUiModelMapper: QuestionSettingsUiModelMapper,
        questionSettingsApiModelMapper: QuestionSettingsApiModelMapper,
        resourceManager: ResourceManager
    ): ViewModel {
        return QuestionSettingsViewModel(
            getQuestionSettingsUseCase = getQuestionSettingsUseCase,
            saveQuestionSettingsUseCase = saveQuestionSettingsUseCase,
            questionSettingsUiModelMapper = questionSettingsUiModelMapper,
            questionSettingsApiModelMapper = questionSettingsApiModelMapper,
            saveTrainingQuestionSettingsUseCase = saveTrainingQuestionSettingsUseCase,
            getTrainingQuestionSettingsUseCase = getTrainingQuestionSettingsUseCase,
            resourceManager = resourceManager
        )
    }
}