package ru.kpfu.itis.paramonov.questions.presentation.settings.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.questions.presentation.settings.fragments.QuestionSettingsFragment

@Subcomponent(
    modules = [
        QuestionSettingsModule::class
    ]
)
@ScreenScope
interface QuestionSettingsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): QuestionSettingsComponent
    }

    fun inject(fragment: QuestionSettingsFragment)
}