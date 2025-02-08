package ru.kpfu.itis.paramonov.profiles.usecase.friends

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.junit.Assert.*
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.GetFriendRequestsUseCaseImpl

class GetFriendRequestsUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendRequestsUseCaseImpl_userPresent_someRequests() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager>()

        val id = "id"
        val username = "admin"
        val profilePictureUrl = "url"
        val info = "some info"
        val dateRegistered = "01.01.1970"
        val friendIdList = listOf("111")
        val friendRequestFromList = listOf("333")

        val mockedUser = User(
            id = "1",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = emptyList(),
            friendRequestFromList = listOf("222")
        )
        val mockedUserRequest = User(
            id = id,
            username = username,
            profilePictureUrl = profilePictureUrl,
            info = info,
            dateRegistered = dateRegistered,
            friendIdList = friendIdList,
            friendRequestFromList = friendRequestFromList
        )
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn mockedUser
            onBlocking { getUser("222") } doReturn mockedUserRequest
        }

        val impl = GetFriendRequestsUseCaseImpl(dispatcher, userRepository, resourceManager)
        val requests = impl.invoke()

        verify(userRepository).getCurrentUser()
        verify(userRepository).getUser("222")

        assertEquals(1, requests.size)
        requests.forEach { user ->
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
    fun getFriendRequestsUseCaseImpl_userPresent_emptyRequests() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager>()

        val mockedUser = User(
            id = "1",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = emptyList(),
            friendRequestFromList = emptyList()
        )
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn mockedUser
        }

        val impl = GetFriendRequestsUseCaseImpl(dispatcher, userRepository, resourceManager)
        val requests = impl.invoke()

        assert(requests.isEmpty())
        verify(userRepository).getCurrentUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendRequestsUseCaseImpl_noUser() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }

        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn null
        }

        val impl = GetFriendRequestsUseCaseImpl(dispatcher, userRepository, resourceManager)
        val exception = assertThrows(IncorrectUserDataException::class.java) {
            runBlocking { impl.invoke() }
        }

        assertEquals("Error", exception.message)
        verify(userRepository).getCurrentUser()
    }
}