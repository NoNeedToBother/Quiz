package ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.feature_users.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.profile_settings.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.profile_settings.ConfirmCredentialsUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.profile_settings.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.feature_users.domain.usecase.profile_settings.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs.ProfileSettingsDialogFragment
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val saveUserSettingsUseCase: SaveUserSettingsUseCase,
    private val changeCredentialsUseCase: ChangeCredentialsUseCase,
    private val confirmCredentialsUseCase: ConfirmCredentialsUseCase,
    private val authenticationRouter: AuthenticationRouter
): BaseProfileViewModel() {

    private val _confirmCredentialsFlow = MutableStateFlow<Boolean?>(null)

    val confirmCredentialsFlow: StateFlow<Boolean?> get() = _confirmCredentialsFlow

    private val _changeCredentialsErrorFlow = MutableStateFlow<Throwable?>(null)

    val changeCredentialsErrorFlow: StateFlow<Throwable?> get() = _changeCredentialsErrorFlow

    private val _processingCredentialEvents = MutableStateFlow(false)

    val processingCredentialEvents: StateFlow<Boolean> get() = _processingCredentialEvents

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
            _processingCredentialEvents.value = true
            try {
                changeCredentialsUseCase.invoke(email = email, password = password)
                logout()
            } catch (ex: Throwable) {
                _changeCredentialsErrorFlow.value = ex
                _changeCredentialsErrorFlow.value = null
            }
            _processingCredentialEvents.value = false
        }
    }

    fun confirmCredentials(email: String, password: String) {
        viewModelScope.launch {
            _processingCredentialEvents.value = true
            try {
                confirmCredentialsUseCase.invoke(email = email, password = password)
                _confirmCredentialsFlow.value = true
            } catch (ex: Throwable) {
                _confirmCredentialsFlow.value = false
            }
            _confirmCredentialsFlow.value = null
            _processingCredentialEvents.value = false
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
            val key = getProfileSettingsUpdateKey(setting.key) ?: ""
            map[key] = setting.value
        }
        viewModelScope.launch {
            saveUserSettingsUseCase.invoke(settings)
        }
    }

    private fun getProfileSettingsUpdateKey(dialogKey: String): String? {
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