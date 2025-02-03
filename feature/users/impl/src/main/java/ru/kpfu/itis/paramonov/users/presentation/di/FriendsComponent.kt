package ru.kpfu.itis.paramonov.users.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.users.presentation.fragments.FriendsScreen

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

    fun inject(fragment: FriendsScreen)
}