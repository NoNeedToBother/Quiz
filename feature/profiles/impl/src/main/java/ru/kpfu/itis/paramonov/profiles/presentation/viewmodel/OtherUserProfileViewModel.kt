package ru.kpfu.itis.paramonov.profiles.presentation.viewmodel

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserLastResultsUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.GetUserUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.GetFriendStatusUseCase
import ru.kpfu.itis.paramonov.profiles.api.usecase.friends.SendFriendRequestUseCase
import ru.kpfu.itis.paramonov.profiles.domain.mapper.FriendStatusUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.ResultUiModelMapper
import ru.kpfu.itis.paramonov.profiles.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.OtherUserProfileScreenSideEffect
import ru.kpfu.itis.paramonov.profiles.presentation.mvi.OtherUserProfileScreenState

class OtherUserProfileViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getFriendStatusUseCase: GetFriendStatusUseCase,
    private val getUserLastResultsUseCase: GetUserLastResultsUseCase,
    private val userUiModelMapper: UserUiModelMapper,
    private val resultUiModelMapper: ResultUiModelMapper,
    private val friendStatusUiModelMapper: FriendStatusUiModelMapper,
    private val resourceManager: ResourceManager
): ViewModel(), ContainerHost<OtherUserProfileScreenState, OtherUserProfileScreenSideEffect> {

    override val container = container<OtherUserProfileScreenState, OtherUserProfileScreenSideEffect>(
        OtherUserProfileScreenState()
    )

    fun getUser(id: String) = intent {
        try {
            val user = userUiModelMapper.map(getUserUseCase.invoke(id))
            reduce { state.copy(user = user) }
        } catch (ex: Throwable) {
            postSideEffect(OtherUserProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.incorrect_other_user_data),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun sendFriendRequest(id: String) = intent {
        try {
            sendFriendRequestUseCase.invoke(id)
            reduce { state.copy(friendRequestSent = true) }
        } catch (ex: Throwable) {
            reduce { state.copy(friendRequestSent = false) }
        }
    }

    fun checkFriendStatus(id: String) = intent {
        try {
            val friendStatus = friendStatusUiModelMapper.map(
                getFriendStatusUseCase.invoke(id)
            )
            reduce { state.copy(friendStatus = friendStatus) }
        } catch (ex: Throwable) {
            postSideEffect(OtherUserProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.get_info_fail),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun getLastResults(max: Int, id: String) = intent {
        try {
            val results = getUserLastResultsUseCase.invoke(max, id).map { res -> resultUiModelMapper.map(res) }
            postSideEffect(OtherUserProfileScreenSideEffect.ResultsReceived(results))
        } catch (ex: Throwable) {
            postSideEffect(OtherUserProfileScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.get_results_fail),
                message = ex.message ?:
                    resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }
}