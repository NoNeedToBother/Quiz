package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.GlobalLeaderboardFragment

@Subcomponent(
    modules = [
        GlobalLeaderboardModule::class
    ]
)
@ScreenScope
interface GlobalLeaderboardComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): GlobalLeaderboardComponent
    }

    fun inject(fragment: GlobalLeaderboardFragment)
}