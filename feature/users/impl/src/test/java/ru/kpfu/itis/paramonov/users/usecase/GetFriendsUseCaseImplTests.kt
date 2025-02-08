package ru.kpfu.itis.paramonov.users.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.users.api.model.User
import ru.kpfu.itis.paramonov.users.api.repository.UserRepository
import ru.kpfu.itis.paramonov.users.domain.usecase.GetFriendsUseCaseImpl

class GetFriendsUseCaseImplTests {

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
            onBlocking { getFriends(anyInt(), anyInt()) } doReturn mockedUsers
        }

        val impl = GetFriendsUseCaseImpl(dispatcher, userRepository)
        val users = impl.invoke(1, 1)

        verify(userRepository).getFriends(1, 1)
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
            onBlocking { getFriends(anyInt(), anyInt()) } doReturn emptyList()
        }

        val impl = GetFriendsUseCaseImpl(dispatcher, userRepository)
        val users = impl.invoke(1, 1)

        verify(userRepository).getFriends(1, 1)
        assert(users.isEmpty())
    }
}