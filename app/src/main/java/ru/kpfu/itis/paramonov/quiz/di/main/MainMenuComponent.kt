package ru.kpfu.itis.paramonov.quiz.di.main

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.FeatureScope
import ru.kpfu.itis.paramonov.quiz.presentation.ui.screens.MainMenuFragment

@Subcomponent
@FeatureScope
interface MainMenuComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): MainMenuComponent
    }

    fun inject(fragment: MainMenuFragment)
}