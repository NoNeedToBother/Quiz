package ru.kpfu.itis.paramonov.feature_users.presentation.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelKey
import ru.kpfu.itis.paramonov.common_android.ui.di.viewmodel.ViewModelModule
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.OtherUserProfileViewModel

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class OtherUserProfileModule {

    @Provides
    fun profileViewModel(fragment: Fragment, factory: ViewModelProvider.Factory): OtherUserProfileViewModel {
        return ViewModelProvider(fragment, factory)[OtherUserProfileViewModel::class.java]
    }

    @Provides
    @IntoMap
    @ViewModelKey(OtherUserProfileViewModel::class)
    fun provideQuestionSettingsViewModel(
        getUserUseCase: GetUserUseCase
    ): ViewModel {
        return OtherUserProfileViewModel(
            getUserUseCase = getUserUseCase
        )
    }
}