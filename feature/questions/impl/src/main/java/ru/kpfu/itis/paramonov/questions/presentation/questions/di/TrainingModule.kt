package ru.kpfu.itis.paramonov.questions.presentation.questions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.questions.api.usecase.GetSavedQuestionsUseCase
import ru.kpfu.itis.paramonov.questions.domain.mapper.QuestionDataUiModelMapper
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetSavedQuestionsUseCaseImpl
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.TrainingQuestionsViewModel

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
    fun getSavedQuestionsUseCase(impl: GetSavedQuestionsUseCaseImpl): GetSavedQuestionsUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(TrainingQuestionsViewModel::class)
    fun provideTrainingQuestionsViewModel(
        getSavedQuestionsUseCase: GetSavedQuestionsUseCase,
        questionDataUiModelMapper: QuestionDataUiModelMapper
    ): ViewModel {
        return TrainingQuestionsViewModel(
            getSavedQuestionsUseCase = getSavedQuestionsUseCase,
            questionDataUiModelMapper = questionDataUiModelMapper
        )
    }

}