package ru.kpfu.itis.paramonov.profiles.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.ChangeCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.ConfirmCredentialsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetCurrentUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.LogoutUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.SubscribeToProfileUpdatesUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.AcceptFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.DenyFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendRequestsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveProfilePictureUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveUserSettingsUseCase
import ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs.ProfileSettingsDialogFragment
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.ProfileScreenSideEffect
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.ProfileScreenState

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
    private val resultUiModelMapper: ResultUiModelMapper
): ViewModel(), ContainerHost<ProfileScreenState, ProfileScreenSideEffect> {

    override val container = container<ProfileScreenState, ProfileScreenSideEffect>(ProfileScreenState())

    fun getCurrentUser() = intent {
        try {
            val user = userUiModelMapper.map(getCurrentUserUseCase.invoke())
            reduce { state.copy(user = user) }
        } catch (ex: IncorrectUserDataException) {
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
            postSideEffect(ProfileScreenSideEffect.GoToSignIn)
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun changeCredentials(email: String?, password: String?) = intent {
        reduce { state.copy(processingCredentials = true) }
        try {
            changeCredentialsUseCase.invoke(email = email, password = password)
            logout()
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
        }
        reduce { state.copy(processingCredentials = false) }
    }

    fun confirmCredentials(email: String, password: String) = intent {
        reduce { state.copy(processingCredentials = true) }
        try {
            confirmCredentialsUseCase.invoke(email = email, password = password)
            postSideEffect(ProfileScreenSideEffect.CredentialsConfirmed)
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
        }
        reduce { state.copy(processingCredentials = false) }
    }

    fun saveProfilePicture(uri: Uri) = intent {
        try {
            saveProfilePictureUseCase.invoke(uri)
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
        }
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
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    private fun getProfileSettingsUpdateKey(dialogKey: String): String? {
        return when(dialogKey) {
            ProfileSettingsDialogFragment.USERNAME_KEY -> USERNAME_KEY
            ProfileSettingsDialogFragment.INFO_KEY -> INFO_KEY
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
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
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
        subscribeToProfileUpdatesUseCase.invoke().collect {
            reduce { state.copy(user = userUiModelMapper.map(it)) }
        }
    }

    fun getLastResults(max: Int) = intent {
        try {
            val results = getCurrentUserLastResultsUseCase.invoke(max).map { res -> resultUiModelMapper.map(res) }
            postSideEffect(ProfileScreenSideEffect.ResultsReceived(results))
        } catch (ex: Throwable) {
            postSideEffect(ProfileScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    companion object {
        const val USERNAME_KEY = "username"
        const val INFO_KEY = "info"
    }
}