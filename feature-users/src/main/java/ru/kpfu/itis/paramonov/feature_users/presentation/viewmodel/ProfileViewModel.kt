package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.feature_users.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val authenticationRouter: AuthenticationRouter
): BaseProfileViewModel() {

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val user = getCurrentUserUseCase.invoke()
                _userDataFlow.value = UserDataResult.Success(user)
            } catch (ex: IncorrectUserDataException) {
                _userDataFlow.value = UserDataResult.Failure(ex)
                authenticationRouter.goToSignIn()
            } catch (ex: Throwable) {
                _userDataFlow.value = UserDataResult.Failure(ex)
            }
        }
    }

    fun saveProfilePicture(uri: Uri) {
        viewModelScope.launch {
            saveProfilePictureUseCase.invoke(uri)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUserUseCase.invoke {
                authenticationRouter.goToSignIn()
            }
        }
    }
}