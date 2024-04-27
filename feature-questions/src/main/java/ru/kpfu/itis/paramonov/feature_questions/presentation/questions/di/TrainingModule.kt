package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetSavedQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.TrainingQuestionsViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class TrainingModule {

    @Provides
    fun trainingQuestionsViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): TrainingQuestionsViewModel {
        return ViewModelProvider(fragment, factory)[TrainingQuestionsViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(TrainingQuestionsViewModel::class)
    fun provideTrainingQuestionsViewModel(
        getSavedQuestionsUseCase: GetSavedQuestionsUseCase
    ): ViewModel {
        return TrainingQuestionsViewModel(getSavedQuestionsUseCase)
    }

}