package ru.kpfu.itis.paramonov.feature_users.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.SearchUsersFragment

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

    fun inject(fragment: SearchUsersFragment)
}