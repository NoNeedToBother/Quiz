package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments.QuestionFragment
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments.QuestionsFragment

@Subcomponent(
    modules = [
        QuestionModule::class
    ]
)
@ScreenScope
interface QuestionsComponent {
    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): QuestionsComponent
    }

    fun inject(fragment: QuestionsFragment)

    fun inject(fragment: QuestionFragment)
}