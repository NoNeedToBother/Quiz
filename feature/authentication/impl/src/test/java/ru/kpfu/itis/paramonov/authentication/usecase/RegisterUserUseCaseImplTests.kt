package ru.kpfu.itis.paramonov.authentication.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.authentication.api.model.User
import ru.kpfu.itis.paramonov.authentication.api.repository.AuthenticationRepository
import ru.kpfu.itis.paramonov.authentication.domain.usecase.RegisterUserUseCaseImpl

class RegisterUserUseCaseImplTests {

    @Test
    fun testRegisterUserUseCaseImpl_success() = runTest {
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
            onBlocking { registerUser(any(), any(), any(), any()) } doReturn mockedUser
        }

        val impl = RegisterUserUseCaseImpl(dispatcher, authenticationRepository)

        val email = "email@ee.ee"
        val password = "11111111"
        val confirmPassword = "11111111"
        val user = impl.invoke(username, email, password, confirmPassword)

        verify(authenticationRepository).registerUser(username, email, password, confirmPassword)

        assert(user.id == id)
        assert(user.username == username)
        assert(user.profilePictureUrl == profilePictureUrl)
        assert(user.dateRegistered == dateRegistered)
        assert(user.friendIdList == friendIdList)
        assert(user.friendRequestFromList == friendRequestFromList)
    }

}