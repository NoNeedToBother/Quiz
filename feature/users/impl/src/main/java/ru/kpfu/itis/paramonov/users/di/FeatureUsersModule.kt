package ru.kpfu.itis.paramonov.users.di

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.users.api.usecase.GetFriendsUseCase
import ru.kpfu.itis.paramonov.users.api.usecase.SearchUsersUseCase
import ru.kpfu.itis.paramonov.users.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.users.domain.usecase.GetFriendsUseCaseImpl
import ru.kpfu.itis.paramonov.users.domain.usecase.SearchUsersUseCaseImpl
import ru.kpfu.itis.paramonov.users.presentation.viewmodel.FriendsViewModel
import ru.kpfu.itis.paramonov.users.presentation.viewmodel.SearchUsersViewModel

val featureUsersModule = DI {
    bind<UserUiModelMapper>() with provider { UserUiModelMapper() }
    bind<GetFriendsUseCase>() with provider {
        GetFriendsUseCaseImpl(instance(), instance())
    }
    bind<SearchUsersUseCase>() with provider {
        SearchUsersUseCaseImpl(instance(), instance())
    }

    bindProvider {
        FriendsViewModel(
            getFriendsUseCase = instance(),
            userUiModelMapper = instance(),
            resourceManager = instance()
        )
    }

    bindProvider {
        SearchUsersViewModel(
            searchUsersUseCase = instance(),
            userUiModelMapper = instance(),
            resourceManager = instance()
        )
    }
}
