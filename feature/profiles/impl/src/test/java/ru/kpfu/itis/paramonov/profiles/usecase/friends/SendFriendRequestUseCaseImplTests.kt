package ru.kpfu.itis.paramonov.profiles.usecase.friends

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.profiles.api.repository.FriendRepository
import ru.kpfu.itis.paramonov.profiles.domain.usecase.friends.SendFriendRequestUseCaseImpl

class SendFriendRequestUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sendFriendRequestUseCaseImpl_success() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val id = "1"
        val friendRepository = mock<FriendRepository> {
            onBlocking { sendFriendRequest(id) } doReturn Unit
        }

        val impl = SendFriendRequestUseCaseImpl(friendRepository, dispatcher)
        impl.invoke(id)

        verify(friendRepository).sendFriendRequest(id)
    }
}