package ru.kpfu.itis.paramonov.profiles.usecase.friends

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.api.model.FriendStatus
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.GetFriendStatusUseCaseImpl

class GetFriendStatusUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendStatusUseCaseImpl_noUser() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn null
        }

        val impl = GetFriendStatusUseCaseImpl(userRepository, dispatcher, resourceManager)
        val exception = assertThrows(IncorrectUserDataException::class.java) {
            runBlocking { impl.invoke("1") }
        }

        assertEquals("Error", exception.message)
        verify(userRepository).getCurrentUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendStatusUseCaseImpl_noOtherUser() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
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
            onBlocking { getUser("1") } doReturn null
        }

        val impl = GetFriendStatusUseCaseImpl(userRepository, dispatcher, resourceManager)
        val exception = assertThrows(IncorrectUserDataException::class.java) {
            runBlocking { impl.invoke("1") }
        }

        assertEquals("Error", exception.message)
        verify(userRepository).getCurrentUser()
        verify(userRepository).getUser("1")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendStatusUseCaseImpl_success_sameUser() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
        val mockedUser = User(
            id = "1",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = emptyList(),
            friendRequestFromList = emptyList()
        )
        val mockedOtherUser = User(
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
            onBlocking { getUser("1") } doReturn mockedOtherUser
        }

        val impl = GetFriendStatusUseCaseImpl(userRepository, dispatcher, resourceManager)
        val result = impl.invoke("1")

        assertEquals(result, FriendStatus.SAME_USER)
        verify(userRepository).getUser("1")
        verify(userRepository).getCurrentUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendStatusUseCaseImpl_success_friend() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
        val mockedUser = User(
            id = "1",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = listOf("2"),
            friendRequestFromList = emptyList()
        )
        val mockedOtherUser = User(
            id = "2",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = listOf("1"),
            friendRequestFromList = emptyList()
        )
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn mockedUser
            onBlocking { getUser("2") } doReturn mockedOtherUser
        }

        val impl = GetFriendStatusUseCaseImpl(userRepository, dispatcher, resourceManager)
        val result = impl.invoke("2")

        assertEquals(result, FriendStatus.FRIEND)
        verify(userRepository).getUser("2")
        verify(userRepository).getCurrentUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendStatusUseCaseImpl_success_requestSent() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
        val mockedUser = User(
            id = "1",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = emptyList(),
            friendRequestFromList = emptyList()
        )
        val mockedOtherUser = User(
            id = "2",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = emptyList(),
            friendRequestFromList = listOf("1")
        )
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn mockedUser
            onBlocking { getUser("2") } doReturn mockedOtherUser
        }

        val impl = GetFriendStatusUseCaseImpl(userRepository, dispatcher, resourceManager)
        val result = impl.invoke("2")

        assertEquals(result, FriendStatus.REQUEST_SENT)
        verify(userRepository).getUser("2")
        verify(userRepository).getCurrentUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendStatusUseCaseImpl_success_notFriend() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
        val mockedUser = User(
            id = "1",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = emptyList(),
            friendRequestFromList = emptyList()
        )
        val mockedOtherUser = User(
            id = "2",
            username = "1",
            profilePictureUrl = "1",
            info = "1",
            dateRegistered = "1",
            friendIdList = emptyList(),
            friendRequestFromList = emptyList()
        )
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn mockedUser
            onBlocking { getUser("2") } doReturn mockedOtherUser
        }

        val impl = GetFriendStatusUseCaseImpl(userRepository, dispatcher, resourceManager)
        val result = impl.invoke("2")

        assertEquals(result, FriendStatus.NOT_FRIEND)
        verify(userRepository).getUser("2")
        verify(userRepository).getCurrentUser()
    }

}