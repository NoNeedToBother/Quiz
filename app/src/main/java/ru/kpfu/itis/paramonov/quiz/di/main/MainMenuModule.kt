package ru.kpfu.itis.paramonov.quiz.di.main

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import ru.kpfu.itis.paramonov.quiz.presentation.viewmodel.MainMenuViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class MainMenuModule {

    @Provides
    @IntoMap
    @ViewModelKey(MainMenuViewModel::class)
    fun provideMainMenuViewModel(navigator: Navigator): ViewModel {
        return MainMenuViewModel(navigator)
    }

    @Provides
    fun mainMenuViewModel(fragment: Fragment, viewModelFactory: ViewModelProvider.Factory): MainMenuViewModel {
        return ViewModelProvider(fragment, viewModelFactory)[MainMenuViewModel::class.java]
    }
}