package ru.kpfu.itis.paramonov.users.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.users.api.usecase.SearchUsersUseCase
import ru.kpfu.itis.paramonov.users.presentation.viewmodel.SearchUsersViewModel
import ru.kpfu.itis.paramonov.navigation.UserRouter
import ru.kpfu.itis.paramonov.users.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.users.domain.usecase.SearchUsersUseCaseImpl

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class SearchUsersModule {

    @Provides
    fun searchUsersViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): SearchUsersViewModel {
        return ViewModelProvider(fragment, factory)[SearchUsersViewModel::class.java]
    }

    @Provides
    fun searchUsersUseCase(impl: SearchUsersUseCaseImpl): SearchUsersUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(SearchUsersViewModel::class)
    fun provideSearchUsersViewModel(
        searchUsersUseCase: SearchUsersUseCase,
        userUiModelMapper: UserUiModelMapper,
        userRouter: UserRouter
    ): ViewModel {
        return SearchUsersViewModel(
            searchUsersUseCase = searchUsersUseCase,
            userRouter = userRouter,
            userUiModelMapper = userUiModelMapper
        )
    }
}