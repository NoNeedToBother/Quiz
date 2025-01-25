package ru.kpfu.itis.paramonov.feature_users.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetFriendsUseCase
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.FriendsViewModel
import ru.kpfu.itis.paramonov.navigation.UserRouter

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class FriendsModule {

    @Provides
    fun friendsViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): FriendsViewModel {
        return ViewModelProvider(fragment, factory)[FriendsViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    fun provideFriendsViewModel(
        getFriendsUseCase: GetFriendsUseCase,
        userRouter: UserRouter
    ): ViewModel {
        return FriendsViewModel(getFriendsUseCase, userRouter)
    }
}