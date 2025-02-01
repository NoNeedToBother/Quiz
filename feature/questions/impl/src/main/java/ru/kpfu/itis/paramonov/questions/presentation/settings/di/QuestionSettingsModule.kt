package ru.kpfu.itis.paramonov.questions.presentation.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.questions.api.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.api.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsApiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionSettingsUiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetQuestionSettingsUseCaseImpl
import ru.kpfu.itis.paramonov.questions.domain.usecase.SaveQuestionSettingsUseCaseImpl
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
    @IntoMap
    @ViewModelKey(QuestionSettingsViewModel::class)
    fun provideQuestionSettingsViewModel(
        getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
        saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase,
        questionSettingsUiModelMapper: QuestionSettingsUiModelMapper,
        questionSettingsApiModelMapper: QuestionSettingsApiModelMapper
    ): ViewModel {
        return QuestionSettingsViewModel(
            getQuestionSettingsUseCase = getQuestionSettingsUseCase,
            saveQuestionSettingsUseCase = saveQuestionSettingsUseCase,
            questionSettingsUiModelMapper = questionSettingsUiModelMapper,
            questionSettingsApiModelMapper = questionSettingsApiModelMapper
        )
    }
}