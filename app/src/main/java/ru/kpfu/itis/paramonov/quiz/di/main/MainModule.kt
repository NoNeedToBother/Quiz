package ru.kpfu.itis.paramonov.quiz.di.main

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.quiz.domain.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.quiz.presentation.viewmodel.MainViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class MainModule {
    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(logoutUserUseCase: LogoutUserUseCase): ViewModel {
        return MainViewModel(logoutUserUseCase)
    }

    @Provides
    fun mainViewModel(activity: AppCompatActivity, viewModelFactory: ViewModelProvider.Factory): MainViewModel {
        return ViewModelProvider(activity, viewModelFactory)[MainViewModel::class.java]
    }
}