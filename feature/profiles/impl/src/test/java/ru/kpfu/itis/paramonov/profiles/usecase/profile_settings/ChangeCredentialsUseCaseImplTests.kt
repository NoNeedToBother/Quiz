package ru.kpfu.itis.paramonov.profiles.usecase.profile_settings

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings.ChangeCredentialsUseCaseImpl

class ChangeCredentialsUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun changeCredentialsUseCaseImpl_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val userRepository = mock<UserRepository> {
            onBlocking { updateCredentials(anyString(), anyString()) } doReturn Unit
        }

        val impl = ChangeCredentialsUseCaseImpl(dispatcher, userRepository)
        impl.invoke("a", "b")

        verify(userRepository).updateCredentials("a", "b")
    }
}