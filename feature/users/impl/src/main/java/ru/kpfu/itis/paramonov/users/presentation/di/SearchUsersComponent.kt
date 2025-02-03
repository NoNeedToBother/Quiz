package ru.kpfu.itis.paramonov.users.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.users.presentation.ui.screens.SearchUsersScreen

@Subcomponent(
    modules = [
        SearchUsersModule::class
    ]
)
@ScreenScope
interface SearchUsersComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): SearchUsersComponent
    }

    fun inject(fragment: SearchUsersScreen)
}