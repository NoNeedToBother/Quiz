package ru.kpfu.itis.paramonov.feature_users.presentation.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.ProfileFragment

@Subcomponent(
    modules = [
        ProfileModule::class
    ]
)
@ScreenScope
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): ProfileComponent
    }

    fun inject(fragment: ProfileFragment)
}