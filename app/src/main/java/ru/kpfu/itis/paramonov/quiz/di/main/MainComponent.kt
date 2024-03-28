package ru.kpfu.itis.paramonov.quiz.di.main

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.quiz.presentation.ui.MainActivity

@Component(
    dependencies = [
        MainDependencies::class
    ]
)
@ScreenScope
interface MainComponent {

    companion object {

        fun init(activity: AppCompatActivity, deps: MainDependencies): MainComponent {
            return DaggerMainComponent.factory().create(activity, deps)
        }
    }

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: AppCompatActivity,
            deps: MainDependencies
        ): MainComponent
    }

    fun inject(mainActivity: MainActivity)
}