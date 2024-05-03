package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.FriendsLeaderboardFragment

@Subcomponent(
    modules = [
        FriendsLeaderboardModule::class
    ]
)
@ScreenScope
interface FriendsLeaderboardComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): FriendsLeaderboardComponent
    }

    fun inject(fragment: FriendsLeaderboardFragment)
}