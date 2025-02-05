package ru.kpfu.itis.paramonov.users.presentation.viewmodel

import ru.kpfu.itis.paramonov.users.api.usecase.GetFriendsUseCase
import ru.kpfu.itis.paramonov.users.domain.mapper.UserUiModelMapper
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.users.presentation.mvi.FriendsScreenState
import ru.kpfu.itis.paramonov.users.presentation.mvi.FriendsScreenSideEffect

class FriendsViewModel(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val userUiModelMapper: UserUiModelMapper
): ViewModel(), ContainerHost<FriendsScreenState, FriendsScreenSideEffect> {

    override val container = container<FriendsScreenState, FriendsScreenSideEffect>(FriendsScreenState())

    fun getFriends(max: Int) = intent {
        try {
            val friends = getFriendsUseCase.invoke(0, max).map(userUiModelMapper::map)
            reduce { state.copy(friends = friends) }
        } catch (ex: Throwable) {
            postSideEffect(FriendsScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }

    fun loadNextFriends(offset: Int, max: Int) = intent {
        try {
            val users = getFriendsUseCase.invoke(offset, max).map(userUiModelMapper::map)
            val new = ArrayList(state.friends)
            new.addAll(users)
            reduce { state.copy(friends = new.distinctBy { it.id }) }
        } catch (ex: Throwable) {
            postSideEffect(FriendsScreenSideEffect.ShowError(ex.message ?: ""))
        }
    }
}
