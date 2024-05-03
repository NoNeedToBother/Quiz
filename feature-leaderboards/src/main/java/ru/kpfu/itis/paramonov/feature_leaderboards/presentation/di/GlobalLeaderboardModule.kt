package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.GlobalLeaderboardViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class GlobalLeaderboardModule {

    @Provides
    fun globalLeaderboardViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): GlobalLeaderboardViewModel {
        return ViewModelProvider(fragment, factory)[GlobalLeaderboardViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(GlobalLeaderboardViewModel::class)
    fun provideGlobalLeaderboardViewModel(): ViewModel {
        return GlobalLeaderboardViewModel()
    }
}