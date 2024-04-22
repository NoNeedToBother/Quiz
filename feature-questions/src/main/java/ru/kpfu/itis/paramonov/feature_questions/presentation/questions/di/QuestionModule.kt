package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_questions.domain.usecase.GetQuestionsUseCase
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel

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
        getQuestionsUseCase: GetQuestionsUseCase
    ): ViewModel {
        return QuestionsViewModel(getQuestionsUseCase)
    }
}