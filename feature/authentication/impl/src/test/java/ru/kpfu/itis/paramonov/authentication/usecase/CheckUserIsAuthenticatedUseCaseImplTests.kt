package ru.kpfu.itis.paramonov.authentication.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.authentication.domain.usecase.CheckUserIsAuthenticatedUseCaseImpl

class CheckUserIsAuthenticatedUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testCheckUserIsAuthenticatedUseCaseImpl_noUser() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val authenticationRepository = mock<AuthenticationRepository> {
            onBlocking { checkUserIsAuthenticated() } doReturn null
        }

        val impl = CheckUserIsAuthenticatedUseCaseImpl(dispatcher, authenticationRepository)
        val user = impl.invoke()

        assert(user == null)
        verify(authenticationRepository).checkUserIsAuthenticated()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testCheckUserIsAuthenticatedUseCaseImpl_someUser() = runTest {
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

        val authenticationRepository = mock<AuthenticationRepository> {
            onBlocking { checkUserIsAuthenticated() } doReturn mockedUser
        }

        val impl = CheckUserIsAuthenticatedUseCaseImpl(dispatcher, authenticationRepository)
        val user = impl.invoke()

        assert(user != null)
        assert(user!!.id == id)
        assert(user.username == username)
        assert(user.profilePictureUrl == profilePictureUrl)
        assert(user.dateRegistered == dateRegistered)
        assert(user.friendIdList == friendIdList)
        assert(user.friendRequestFromList == friendRequestFromList)

        verify(authenticationRepository).checkUserIsAuthenticated()
    }
}