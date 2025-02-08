package ru.kpfu.itis.paramonov.users.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.users.api.model.User
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository
import ru.kpfu.itis.paramonov.users.domain.usecase.SearchUsersUseCaseImpl

class SearchUsersUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendsUseCaseImpl_success_someUsers() = runTest {
        val dispatcher = UnconfinedTestDispatcher()

        val id = "id"
        val username = "admin"
        val profilePictureUrl = "url"
        val info = "some info"
        val dateRegistered = "01.01.1970"
        val friendIdList = listOf("111")
        val friendRequestFromList = listOf("222")

        val mockedUser = User(
            id = id,
            username = username,
            profilePictureUrl = profilePictureUrl,
            info = info,
            dateRegistered = dateRegistered,
            friendIdList = friendIdList,
            friendRequestFromList = friendRequestFromList
        )
        val mockedUsers = listOf(mockedUser, mockedUser)
        val userRepository = mock<UserRepository> {
            onBlocking { findByUsername(anyString(), anyInt(), anyString()) } doReturn mockedUsers
        }

        val impl = SearchUsersUseCaseImpl(dispatcher, userRepository)
        val users = impl.invoke("a", 1, "b")

        verify(userRepository).findByUsername("a", 1, "b")
        assertEquals(2, users.size)
        users.forEach { user ->
            assert(user.id == id)
            assert(user.username == username)
            assert(user.profilePictureUrl == profilePictureUrl)
            assert(user.dateRegistered == dateRegistered)
            assert(user.friendIdList == friendIdList)
            assert(user.friendRequestFromList == friendRequestFromList)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendsUseCaseImpl_success_emptyUsers() = runTest {
        val dispatcher = UnconfinedTestDispatcher()

        val userRepository = mock<UserRepository> {
            onBlocking { findByUsername(anyString(), anyInt(), anyString()) } doReturn emptyList()
        }

        val impl = SearchUsersUseCaseImpl(dispatcher, userRepository)
        val users = impl.invoke("a", 1, "b")

        verify(userRepository).findByUsername("a", 1, "b")
        assert(users.isEmpty())
    }

}