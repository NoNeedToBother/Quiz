package ru.kpfu.itis.paramonov.questions.presentation.questions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.questions.presentation.questions.fragments.TrainingQuestionsScreen

@Subcomponent(
    modules = [
        TrainingModule::class
    ]
)
@ScreenScope
interface TrainingQuestionsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): TrainingQuestionsComponent
    }

    fun inject(fragment: TrainingQuestionsScreen)

}