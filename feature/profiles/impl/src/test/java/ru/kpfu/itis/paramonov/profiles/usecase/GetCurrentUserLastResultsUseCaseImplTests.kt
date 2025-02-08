package ru.kpfu.itis.paramonov.profiles.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.core.utils.DateTime
import ru.kpfu.itis.paramonov.profiles.api.model.Result
import ru.kpfu.itis.paramonov.profiles.api.model.User
import ru.kpfu.itis.paramonov.profiles.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.profiles.domain.usecase.GetCurrentUserLastResultsUseCaseImpl

class GetCurrentUserLastResultsUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentUserLastResultsUseCaseImpl_success_someResults() = runTest {
        val dispatcher = UnconfinedTestDispatcher()

        val mockedResults = listOf(
            Result(
                id = "111",
                user = User("222", "", "", "", "",
                    emptyList(), emptyList()
                ),
                time = 2,
                correct = 1,
                total = 1,
                score = 1.0,
                date = DateTime(1, 1, 1, 1, 1, 1),
                difficulty = Difficulty.EASY,
                category = Category.GENERAL,
                gameMode = GameMode.BLITZ
            ),
            Result(
                id = "111",
                user = User("222", "", "", "", "",
                    emptyList(), emptyList()
                ),
                time = 2,
                correct = 1,
                total = 1,
                score = 1.0,
                date = DateTime(1, 1, 1, 1, 1, 1),
                difficulty = Difficulty.EASY,
                category = Category.GENERAL,
                gameMode = GameMode.BLITZ
            ),
        )

        val resultRepository = mock<ResultRepository> {
            onBlocking { getLastResults(anyInt()) } doReturn mockedResults
        }

        val impl = GetCurrentUserLastResultsUseCaseImpl(resultRepository, dispatcher)
        val results = impl.invoke(10)

        verify(resultRepository).getLastResults(10)
        assert(results.size == 2)
        results.forEach {
            assert(it.id == "111")
            assert(it.user.id == "222")
            assert(it.time == 2)
            assert(it.correct == 1)
            assert(it.total == 1)
            assert(it.score == 1.0)
            assert(it.difficulty == Difficulty.EASY)
            assert(it.category == Category.GENERAL)
            assert(it.gameMode == GameMode.BLITZ)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentUserLastResultsUseCaseImpl_success_emptyList() = runTest {
        val dispatcher = UnconfinedTestDispatcher()

        val resultRepository = mock<ResultRepository> {
            onBlocking { getLastResults(anyInt()) } doReturn emptyList()
        }

        val impl = GetCurrentUserLastResultsUseCaseImpl(resultRepository, dispatcher)
        val results = impl.invoke(10)

        verify(resultRepository).getLastResults(10)
        assert(results.isEmpty())
    }
}