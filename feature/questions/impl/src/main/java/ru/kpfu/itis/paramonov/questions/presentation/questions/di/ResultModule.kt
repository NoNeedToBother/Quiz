package ru.kpfu.itis.paramonov.questions.presentation.questions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.questions.domain.usecase.GetMaxScoreUseCaseImpl
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.ResultViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class ResultModule {

    @Provides
    fun questionsViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): ResultViewModel {
        return ViewModelProvider(fragment, factory)[ResultViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(ResultViewModel::class)
    fun provideQuestionsViewModel(
        getMaxScoreUseCase: GetMaxScoreUseCaseImpl
    ): ViewModel {
        return ResultViewModel(
            getMaxScoreUseCase = getMaxScoreUseCase
        )
    }
}