package ru.kpfu.itis.paramonov.feature_users.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.FriendsFragment

@Subcomponent(
    modules = [
        FriendsModule::class
    ]
)
@ScreenScope
interface FriendsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): FriendsComponent
    }

    fun inject(fragment: FriendsFragment)
}