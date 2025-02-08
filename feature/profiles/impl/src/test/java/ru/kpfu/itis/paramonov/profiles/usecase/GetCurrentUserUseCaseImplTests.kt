package ru.kpfu.itis.paramonov.profiles.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.domain.exception.IncorrectUserDataException
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetCurrentUserUseCaseImpl

class GetCurrentUserUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentUserUseCaseImplTests_noUser() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn null
        }

        val impl = GetCurrentUserUseCaseImpl(dispatcher, userRepository, resourceManager)
        val exception = assertThrows(IncorrectUserDataException::class.java) {
            runBlocking { impl.invoke() }
        }

        assertEquals("Error", exception.message)
        verify(resourceManager, times(2)).getString(anyInt())
        verify(userRepository).getCurrentUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentUserUseCaseImplTests_error() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager> {
            onBlocking { getString(anyInt()) } doReturn "Error"
        }
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doThrow RuntimeException()
        }

        val impl = GetCurrentUserUseCaseImpl(dispatcher, userRepository, resourceManager)
        val exception = assertThrows(IncorrectUserDataException::class.java) {
            runBlocking { impl.invoke() }
        }

        assertEquals("Error", exception.message)
        verify(resourceManager).getString(anyInt())
        verify(userRepository).getCurrentUser()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentUserUseCaseImplTests_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val resourceManager = mock<ResourceManager>()
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
        val userRepository = mock<UserRepository> {
            onBlocking { getCurrentUser() } doReturn mockedUser
        }

        val impl = GetCurrentUserUseCaseImpl(dispatcher, userRepository, resourceManager)
        val user = impl.invoke()

        verify(userRepository).getCurrentUser()
        assert(user.id == id)
        assert(user.username == username)
        assert(user.profilePictureUrl == profilePictureUrl)
        assert(user.dateRegistered == dateRegistered)
        assert(user.friendIdList == friendIdList)
        assert(user.friendRequestFromList == friendRequestFromList)
    }
}