package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.SaveTrainingQuestionSettingsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewmodel.TrainingQuestionSettingsViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class TrainingQuestionSettingsModule {

    @Provides
    fun trainingQuestionSettingsViewModel(
        fragment: Fragment, factory: ViewModelProvider.Factory): TrainingQuestionSettingsViewModel {
        return ViewModelProvider(fragment, factory)[TrainingQuestionSettingsViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(TrainingQuestionSettingsViewModel::class)
    fun provideQuestionSettingsViewModel(
        saveTrainingQuestionSettingsUseCase: SaveTrainingQuestionSettingsUseCase,
        getTrainingQuestionSettingsUseCase: GetTrainingQuestionSettingsUseCase
    ): ViewModel {
        return TrainingQuestionSettingsViewModel(
            saveTrainingQuestionSettingsUseCase,
            getTrainingQuestionSettingsUseCase)
    }
}