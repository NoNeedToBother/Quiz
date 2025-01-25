package ru.kpfu.itis.paramonov.feature_profiles.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.core.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetUserLastResultsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.SendFriendRequestUseCase
import ru.kpfu.itis.paramonov.feature_profiles.presentation.viewmodel.OtherUserProfileViewModel

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
    @IntoMap
    @ViewModelKey(OtherUserProfileViewModel::class)
    fun provideOtherUserProfileViewModel(
        getUserUseCase: GetUserUseCase,
        sendFriendRequestUseCase: SendFriendRequestUseCase,
        getFriendStatusUseCase: GetFriendStatusUseCase,
        getUserLastResultsUseCase: GetUserLastResultsUseCase
    ): ViewModel {
        return OtherUserProfileViewModel(
            getUserUseCase = getUserUseCase,
            sendFriendRequestUseCase = sendFriendRequestUseCase,
            getFriendStatusUseCase = getFriendStatusUseCase,
            getUserLastResultsUseCase = getUserLastResultsUseCase
        )
    }
}