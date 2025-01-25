package ru.kpfu.itis.paramonov.feature_profiles.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.core.utils.emitException
import ru.kpfu.itis.paramonov.feature_profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetCurrentUserLastResultsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.ConfirmCredentialsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.SubscribeToProfileUpdatesUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.AcceptFriendRequestUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.DenyFriendRequestUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.friends.GetFriendRequestsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.feature_profiles.domain.usecase.profile_settings.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.feature_profiles.presentation.fragments.dialogs.ProfileSettingsDialogFragment
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val saveProfilePictureUseCase: SaveProfilePictureUseCase,
    private val saveUserSettingsUseCase: SaveUserSettingsUseCase,
    private val changeCredentialsUseCase: ChangeCredentialsUseCase,
    private val confirmCredentialsUseCase: ConfirmCredentialsUseCase,
    private val getFriendRequestsUseCase: GetFriendRequestsUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val denyFriendRequestUseCase: DenyFriendRequestUseCase,
    private val subscribeToProfileUpdatesUseCase: SubscribeToProfileUpdatesUseCase,
    private val getCurrentUserLastResultsUseCase: GetCurrentUserLastResultsUseCase,
    private val authenticationRouter: AuthenticationRouter
): BaseProfileViewModel() {

    private val _confirmCredentialsFlow = MutableStateFlow<Boolean?>(null)

    val confirmCredentialsFlow: StateFlow<Boolean?> get() = _confirmCredentialsFlow

    private val _changeUserDataErrorFlow = MutableStateFlow<Throwable?>(null)

    val changeUserDataErrorFlow: StateFlow<Throwable?> get() = _changeUserDataErrorFlow

    private val _processingCredentialEvents = MutableStateFlow(false)

    val processingCredentialEvents: StateFlow<Boolean> get() = _processingCredentialEvents

    private val _friendRequestsDataFlow = MutableStateFlow<FriendRequestResult?>(null)

    val friendRequestsDataFlow: StateFlow<FriendRequestResult?> get() = _friendRequestsDataFlow

    fun getCurrentUser() {
        viewModelScope.launch {
            try {
                val user = getCurrentUserUseCase.invoke()
                _userDataFlow.value = UserDataResult.Success(user)
            } catch (ex: IncorrectUserDataException) {
                _userDataFlow.emitException(UserDataResult.Failure(ex))
                authenticationRouter.goToSignIn()
            } catch (ex: Throwable) {
                _userDataFlow.emitException(UserDataResult.Failure(ex))
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
                _changeUserDataErrorFlow.emitException(ex)
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
            try {
                saveProfilePictureUseCase.invoke(uri)
            } catch (ex: Throwable) {
                _changeUserDataErrorFlow.emitException(ex)
            }
        }
    }

    fun saveUserSettings(settings: Map<String, String>) {
        val map = mutableMapOf<String, String>()
        for (setting in settings) {
            val key = getProfileSettingsUpdateKey(setting.key) ?: ""
            map[key] = setting.value
        }
        viewModelScope.launch {
            try {
                saveUserSettingsUseCase.invoke(settings)
            } catch (ex: Throwable) {
                _changeUserDataErrorFlow.emitException(ex)
            }
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

    fun getFriendRequests() {
        viewModelScope.launch {
            try {
                val requests = getFriendRequestsUseCase.invoke()
                _friendRequestsDataFlow.value = FriendRequestResult.Success(requests)
            } catch (ex: Throwable) {
                _friendRequestsDataFlow.value = FriendRequestResult.Failure(ex)
            } finally {
                _friendRequestsDataFlow.value = null
            }
        }
    }

    fun acceptFriendRequest(id: String) {
        viewModelScope.launch {
            try {
                acceptFriendRequestUseCase.invoke(id)
            } catch (_: Throwable) {}
        }
    }

    fun denyFriendRequest(id: String) {
        viewModelScope.launch {
            try {
                denyFriendRequestUseCase.invoke(id)
            } catch (_: Throwable) {}
        }
    }

    fun subscribeToProfileUpdates() {
        viewModelScope.launch {
            subscribeToProfileUpdatesUseCase.invoke().collect {
                _userDataFlow.value = UserDataResult.Success(it)
            }
        }
    }

    fun getLastResults(max: Int) {
        viewModelScope.launch {
            try {
                val results = getCurrentUserLastResultsUseCase.invoke(max)
                _resultsDataFlow.value = LastResultsDataResult.Success(results)
            } catch (ex: Throwable) {
                _resultsDataFlow.emitException(LastResultsDataResult.Failure(ex))
            } finally {
                _resultsDataFlow.value = null
            }
        }
    }

    sealed interface FriendRequestResult: Result {
        class Success(private val result: List<UserModel>): Result.Success<List<UserModel>>, FriendRequestResult {
            override fun getValue(): List<UserModel> = result
        }

        class Failure(private val ex: Throwable): Result.Failure, FriendRequestResult {
            override fun getException(): Throwable = ex
        }
    }
}