package ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.common.scopes.ScreenScope
import ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.RegisterFragment

@Subcomponent(
    modules = [
        RegistrationModule::class
    ]
)
@ScreenScope
interface RegistrationComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): RegistrationComponent
    }

    fun inject(fragment: RegisterFragment)
}