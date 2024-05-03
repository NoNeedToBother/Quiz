package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.LeaderboardFragment
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.LeaderboardsViewPagerFragment

@Subcomponent(
    modules = [
        LeaderboardsModule::class
    ]
)
@ScreenScope
interface LeaderboardsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): LeaderboardsComponent
    }

    fun inject(fragment: LeaderboardsViewPagerFragment)

    fun inject(fragment: LeaderboardFragment)
}