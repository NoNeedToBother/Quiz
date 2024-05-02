package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.feature_users.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.ProfileSettingsDialogFragment
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val saveUserSettingsUseCase: SaveUserSettingsUseCase,
    private val changeCredentialsUseCase: ChangeCredentialsUseCase,
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

    fun changeCredentials(email: String?, password: String?) {
        viewModelScope.launch {
            changeCredentialsUseCase.invoke(email = email, password = password)
            logout()
        }
    }

    fun saveProfilePicture(uri: Uri) {
        viewModelScope.launch {
            saveProfilePictureUseCase.invoke(uri)
        }
    }

    fun saveUserSettings(settings: Map<String, String>) {
        val map = mutableMapOf<String, String>()
        for (setting in settings) {
            val key = getKey(setting.key) ?: ""
            map[key] = setting.value
        }
        viewModelScope.launch {
            saveUserSettingsUseCase.invoke(settings)
        }
    }

    private fun getKey(dialogKey: String): String? {
        return when(dialogKey) {
            ProfileSettingsDialogFragment.USERNAME_KEY -> SaveUserSettingsUseCase.USERNAME_KEY
            ProfileSettingsDialogFragment.INFO_KEY -> SaveUserSettingsUseCase.INFO_KEY
            else -> null
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