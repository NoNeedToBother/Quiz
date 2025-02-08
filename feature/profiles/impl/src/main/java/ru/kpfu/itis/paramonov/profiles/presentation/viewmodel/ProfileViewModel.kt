package ru.kpfu.itis.paramonov.profiles.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.settings.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.settings.ConfirmCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.SubscribeToProfileUpdatesUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.AcceptFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.DenyFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendRequestsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.settings.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.settings.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.ProfileScreenSideEffect
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.ProfileScreenState
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.INFO_UPDATE_KEY
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.USERNAME_UPDATE_KEY

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
    private val userUiModelMapper: UserUiModelMapper,
    private val resultUiModelMapper: ResultUiModelMapper,
    private val usernameValidator: UsernameValidator,
    private val passwordValidator: PasswordValidator,
    private val resourceManager: ResourceManager
): ViewModel(), ContainerHost<ProfileScreenState, ProfileScreenSideEffect> {

    override val container = container<ProfileScreenState, ProfileScreenSideEffect>(ProfileScreenState())

    fun getCurrentUser() = intent {
        try {
            val user = userUiModelMapper.map(getCurrentUserUseCase.invoke())
            reduce { state.copy(user = user) }
        } catch (ex: IncorrectUserDataException) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.incorrect_user_data),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
            postSideEffect(ProfileScreenSideEffect.GoToSignIn)
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.incorrect_user_data),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun changeCredentials(email: String?, password: String?) = intent {
        reduce { state.copy(processingCredentials = true) }
        try {
            changeCredentialsUseCase.invoke(email = email, password = password)
            logout()
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.dialog_incorrect_credentials),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
        reduce { state.copy(processingCredentials = false) }
    }

    fun confirmCredentials(email: String, password: String) = intent {
        reduce { state.copy(processingCredentials = true) }
        try {
            confirmCredentialsUseCase.invoke(email = email, password = password)
            postSideEffect(ProfileScreenSideEffect.CredentialsConfirmed)
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.credentials_change_failed),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
        reduce { state.copy(processingCredentials = false) }
    }

    fun saveProfilePicture(uri: Uri) = intent {
        try {
            saveProfilePictureUseCase.invoke(uri)
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.save_photo_fail),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun onProfilePictureChosen(uri: Uri) = intent {
        postSideEffect(ProfileScreenSideEffect.ProfilePictureConfirmed(uri))
    }

    fun saveUserSettings(settings: Map<String, String>) = intent {
        val map = mutableMapOf<String, String>()
        for (setting in settings) {
            val key = getProfileSettingsUpdateKey(setting.key) ?: ""
            map[key] = setting.value
        }
        try {
            saveUserSettingsUseCase.invoke(settings)
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.dialog_incorrect_user_data),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    private fun getProfileSettingsUpdateKey(dialogKey: String): String? {
        return when(dialogKey) {
            USERNAME_UPDATE_KEY -> USERNAME_KEY
            INFO_UPDATE_KEY -> INFO_KEY
            else -> null
        }
    }

    fun logout() = intent {
        logoutUserUseCase.invoke {
            postSideEffect(ProfileScreenSideEffect.GoToSignIn)
        }
    }

    fun getFriendRequests() = intent {
        try {
            val requests = getFriendRequestsUseCase.invoke().map { user -> userUiModelMapper.map(user) }
            postSideEffect(ProfileScreenSideEffect.FriendRequestsReceived(requests))
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.friend_request_error),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun acceptFriendRequest(id: String) = intent {
        try {
            acceptFriendRequestUseCase.invoke(id)
        } catch (_: Throwable) {}
    }

    fun denyFriendRequest(id: String) = intent {
        try {
            denyFriendRequestUseCase.invoke(id)
        } catch (_: Throwable) {}
    }

    fun subscribeToProfileUpdates() = intent {
        subscribeToProfileUpdatesUseCase.invoke().collectLatest {
            reduce { state.copy(user = userUiModelMapper.map(it)) }
        }
    }

    fun getLastResults(max: Int) = intent {
        try {
            val results = getCurrentUserLastResultsUseCase.invoke(max).map { res -> resultUiModelMapper.map(res) }
            postSideEffect(ProfileScreenSideEffect.ResultsReceived(results))
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.get_results_fail),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun validatePassword(password: String?) = intent {
        val result = if (password.isNullOrEmpty()) null
        else if (passwordValidator.validate(password)) null
        else resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.weak_password_msg)
        reduce { state.copy(passwordError = result) }
    }

    fun validateConfirmPassword(confirmPassword: String?) = intent {
        val result = if (confirmPassword.isNullOrEmpty()) null
        else if (passwordValidator.validate(confirmPassword)) null
        else resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.weak_password_msg)
        reduce { state.copy(confirmPasswordError = result) }
    }

    fun validateUsername(username: String?) = intent {
        val result = if (username.isNullOrEmpty()) null
        else if (usernameValidator.validate(username)) null
        else resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.invalid_username_msg)
        reduce { state.copy(usernameError = result) }
    }

    fun validateEmail(email: String?) = intent {
        val result = if (email.isNullOrEmpty()) null
        else if (email.contains("@")) null
        else resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.invalid_email)
        reduce { state.copy(emailError = result) }
    }

    fun clearErrors() = intent {
        reduce {
            state.copy(usernameError = null, passwordError = null,
                emailError = null, confirmPasswordError = null)
        }
    }

    companion object {
        const val USERNAME_KEY = "username"
        const val INFO_KEY = "info"
    }
}
