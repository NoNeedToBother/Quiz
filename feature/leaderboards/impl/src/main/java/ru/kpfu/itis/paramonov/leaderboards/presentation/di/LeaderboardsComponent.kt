package ru.kpfu.itis.paramonov.leaderboards.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.leaderboards.presentation.fragments.LeaderboardScreen

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

    fun inject(fragment: LeaderboardScreen)
}