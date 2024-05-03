package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.FriendsLeaderboardViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class FriendsLeaderboardModule {

    @Provides
    fun friendsLeaderboardViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): FriendsLeaderboardViewModel {
        return ViewModelProvider(fragment, factory)[FriendsLeaderboardViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(FriendsLeaderboardViewModel::class)
    fun provideFriendsLeaderboardViewModel(): ViewModel {
        return FriendsLeaderboardViewModel()
    }
}