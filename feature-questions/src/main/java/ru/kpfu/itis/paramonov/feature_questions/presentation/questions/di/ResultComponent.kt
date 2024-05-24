package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments.ResultFragment

@Subcomponent(
    modules = [
        ResultModule::class
    ]
)
interface ResultComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): ResultComponent
    }

    fun inject(fragment: ResultFragment)

}