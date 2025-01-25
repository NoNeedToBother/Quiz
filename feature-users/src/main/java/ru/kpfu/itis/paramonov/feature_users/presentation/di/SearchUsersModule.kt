package ru.kpfu.itis.paramonov.feature_users.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.SearchUsersUseCase
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.SearchUsersViewModel
import ru.kpfu.itis.paramonov.navigation.UserRouter

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
    @IntoMap
    @ViewModelKey(SearchUsersViewModel::class)
    fun provideSearchUsersViewModel(
        searchUsersUseCase: SearchUsersUseCase,
        userRouter: UserRouter
    ): ViewModel {
        return SearchUsersViewModel(searchUsersUseCase, userRouter)
    }
}