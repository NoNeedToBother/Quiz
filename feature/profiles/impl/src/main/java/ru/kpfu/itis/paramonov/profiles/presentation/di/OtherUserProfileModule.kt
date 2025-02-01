package ru.kpfu.itis.paramonov.profiles.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.SendFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.domain.mapper.FriendStatusUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetUserLastResultsUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetUserUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.GetFriendStatusUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.SendFriendRequestUseCaseImpl
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.OtherUserProfileViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class OtherUserProfileModule {

    @Provides
    fun otherUserProfileViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): OtherUserProfileViewModel {
        return ViewModelProvider(fragment, factory)[OtherUserProfileViewModel::class.java]
    }

    @Provides
    fun sendFriendRequestUseCase(impl: SendFriendRequestUseCaseImpl): SendFriendRequestUseCase = impl

    @Provides
    fun getUserUseCase(impl: GetUserUseCaseImpl): GetUserUseCase = impl

    @Provides
    fun getFriendStatusUseCase(impl: GetFriendStatusUseCaseImpl): GetFriendStatusUseCase = impl

    @Provides
    fun getUserLastResultsUseCase(impl: GetUserLastResultsUseCaseImpl): GetUserLastResultsUseCase = impl

    @Provides
    @IntoMap
    @ViewModelKey(OtherUserProfileViewModel::class)
    fun provideOtherUserProfileViewModel(
        getUserUseCase: GetUserUseCase,
        sendFriendRequestUseCase: SendFriendRequestUseCase,
        getFriendStatusUseCase: GetFriendStatusUseCase,
        getUserLastResultsUseCase: GetUserLastResultsUseCase,
        userUiModelMapper: UserUiModelMapper,
        resultUiModelMapper: ResultUiModelMapper,
        friendStatusUiModelMapper: FriendStatusUiModelMapper
    ): ViewModel {
        return OtherUserProfileViewModel(
            getUserUseCase = getUserUseCase,
            sendFriendRequestUseCase = sendFriendRequestUseCase,
            getFriendStatusUseCase = getFriendStatusUseCase,
            getUserLastResultsUseCase = getUserLastResultsUseCase,
            userUiModelMapper = userUiModelMapper,
            resultUiModelMapper = resultUiModelMapper,
            friendStatusUiModelMapper = friendStatusUiModelMapper
        )
    }
}