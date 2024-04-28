package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewmodel.QuestionSettingsViewModel

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
    @IntoMap
    @ViewModelKey(QuestionSettingsViewModel::class)
    fun provideQuestionSettingsViewModel(
        getQuestionSettingsUseCase: GetQuestionSettingsUseCase,
        saveQuestionSettingsUseCase: SaveQuestionSettingsUseCase
        ): ViewModel {
        return QuestionSettingsViewModel(getQuestionSettingsUseCase, saveQuestionSettingsUseCase)
    }
}