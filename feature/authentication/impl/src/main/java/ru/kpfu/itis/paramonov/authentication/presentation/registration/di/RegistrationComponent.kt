package ru.kpfu.itis.paramonov.authentication.presentation.registration.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.authentication.presentation.registration.RegisterScreen

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

    fun inject(fragment: RegisterScreen)
}