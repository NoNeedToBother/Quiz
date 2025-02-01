package ru.kpfu.itis.paramonov.users.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.users.api.usecase.GetFriendsUseCase
import ru.kpfu.itis.paramonov.users.presentation.viewmodel.FriendsViewModel
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.users.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.users.domain.usecase.GetFriendsUseCaseImpl

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
    fun getFriendsUseCase(impl: GetFriendsUseCaseImpl): GetFriendsUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(FriendsViewModel::class)
    fun provideFriendsViewModel(
        getFriendsUseCase: GetFriendsUseCase,
        userRouter: UserRouter,
        userUiModelMapper: UserUiModelMapper
    ): ViewModel {
        return FriendsViewModel(getFriendsUseCase, userRouter, userUiModelMapper)
    }
}