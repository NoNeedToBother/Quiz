package ru.kpfu.itis.paramonov.users.presentation.viewmodel

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.users.R
import ru.kpfu.itis.paramonov.users.api.usecase.SearchUsersUseCase
import ru.kpfu.itis.paramonov.users.domain.mapper.UserUiModelMapper
import ru.kpfu.itis.paramonov.users.presentation.mvi.SearchUsersScreenSideEffect
import ru.kpfu.itis.paramonov.users.presentation.mvi.SearchUsersScreenState

class SearchUsersViewModel(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val userUiModelMapper: UserUiModelMapper,
    private val resourceManager: ResourceManager
): ViewModel(), ContainerHost<SearchUsersScreenState, SearchUsersScreenSideEffect> {

    override val container = container<SearchUsersScreenState, SearchUsersScreenSideEffect>(SearchUsersScreenState())

    fun searchUsers(username: String, max: Int, lastId: String?) = intent {
        try {
            val users = searchUsersUseCase.invoke(username, max, lastId).map { user -> userUiModelMapper.map(user) }
            reduce { state.copy(users = users) }
        } catch (ex: Throwable) {
            postSideEffect(SearchUsersScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.search_users_fail),
                message = ex.message
                    ?: resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }

    fun loadNextUsers(username: String, max: Int, lastId: String) = intent {
        try {
            val users = searchUsersUseCase.invoke(username, max, lastId).map { user -> userUiModelMapper.map(user) }
            if (users.isNotEmpty()) {
                val new = ArrayList(state.users)
                new.addAll(users)
                reduce { state.copy(users = new.distinctBy { it.id }) }
            }
        } catch (ex: Throwable) {
            postSideEffect(SearchUsersScreenSideEffect.ShowError(
                title = resourceManager.getString(R.string.search_users_fail),
                message = ex.message
                    ?: resourceManager.getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
            ))
        }
    }
}
