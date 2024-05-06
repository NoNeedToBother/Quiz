package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.fragments.TrainingQuestionSettingsFragment

@Subcomponent(
    modules = [
        TrainingQuestionSettingsModule::class
    ]
)
@ScreenScope
interface TrainingQuestionSettingsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): TrainingQuestionSettingsComponent
    }

    fun inject(fragment: TrainingQuestionSettingsFragment)

}