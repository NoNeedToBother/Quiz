package ru.kpfu.itis.paramonov.users.presentation.viewmodel

import ru.kpfu.itis.paramonov.users.api.usecase.GetFriendsUseCase
import ru.kpfu.itis.paramonov.users.domain.mapper.UserUiModelMapper
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.users.presentation.mvi.FriendsScreenState
import ru.kpfu.itis.paramonov.users.presentation.mvi.FriendsSideEffect

class FriendsViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val userUiModelMapper: UserUiModelMapper
) : ViewModel(), ContainerHost<FriendsScreenState, FriendsSideEffect> {

    override val container = container<FriendsScreenState, FriendsSideEffect>(FriendsScreenState())

    fun getFriends(max: Int) = intent {
        try {
            val friends = getFriendsUseCase.invoke(0, max).map(userUiModelMapper::map)
            reduce { state.copy(friends = friends) }
        } catch (ex: Throwable) {
            postSideEffect(FriendsSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun loadNextFriends(offset: Int, max: Int) = intent {
        try {
            val users = getFriendsUseCase.invoke(offset, max).map(userUiModelMapper::map)
            reduce { state.copy(pagedFriends = users) }
        } catch (ex: Throwable) {
            postSideEffect(FriendsSideEffect.ShowError(ex.message ?: ""))
        }
    }
}
