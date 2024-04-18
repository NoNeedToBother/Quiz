package ru.kpfu.itis.paramonov.quiz.di.main

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.quiz.presentation.ui.MainActivity
import ru.kpfu.itis.paramonov.quiz.presentation.ui.fragments.MainMenuFragment

@Component(
    dependencies = [
        MainFirebaseDependencies::class,
        MainDependencies::class,
    ],
    modules = [
        MainModule::class
    ]
)
@ScreenScope
interface MainComponent {

    companion object {
        fun init(activity: AppCompatActivity,
                 deps: MainDependencies,
                 firebaseDeps: MainFirebaseDependencies): MainComponent {
            return DaggerMainComponent.factory().create(activity, deps, firebaseDeps)
        }
    }

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: AppCompatActivity,
            mainDeps: MainDependencies,
            mainFirebaseDeps: MainFirebaseDependencies
        ): MainComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(mainMenuFragment: MainMenuFragment)
}