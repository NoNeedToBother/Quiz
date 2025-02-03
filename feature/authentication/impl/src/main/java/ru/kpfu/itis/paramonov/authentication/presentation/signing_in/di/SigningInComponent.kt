package ru.kpfu.itis.paramonov.authentication.presentation.signing_in.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.kpfu.itis.paramonov.core.di.scopes.ScreenScope
import ru.kpfu.itis.paramonov.authentication.presentation.signing_in.SignInScreen

@Subcomponent(
    modules = [
        SigningInModule::class
    ]
)
@ScreenScope
interface SigningInComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): SigningInComponent
    }

    fun inject(fragment: SignInScreen)
}