package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.QuestionsFragment

@Subcomponent(
    modules = [
        QuestionModule::class
    ]
)
@ScreenScope
interface QuestionComponent {
    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): QuestionComponent
    }

    fun inject(fragment: QuestionsFragment)
}