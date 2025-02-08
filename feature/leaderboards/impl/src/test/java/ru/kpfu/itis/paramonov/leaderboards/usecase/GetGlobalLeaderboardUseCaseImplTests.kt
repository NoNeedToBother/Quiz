package ru.kpfu.itis.paramonov.leaderboards.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.core.utils.DateTime
import ru.kpfu.itis.paramonov.leaderboards.api.model.Result
import ru.kpfu.itis.paramonov.leaderboards.api.model.User
import ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.leaderboards.domain.usecase.GetGlobalLeaderboardUseCaseImpl

class GetGlobalLeaderboardUseCaseImplTests {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getFriendsLeaderboardUseCaseImpl_success() = runTest {
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
            onBlocking { getGlobalResults(GameMode.BLITZ, null, null, 2, null) } doReturn mockedResults
        }

        val impl = GetGlobalLeaderboardUseCaseImpl(dispatcher, resultRepository)
        val results = impl.invoke(GameMode.BLITZ, null, null, 2, null)
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
    fun getFriendsLeaderboardUseCaseImpl_someIdsMissing() = runTest {
        val dispatcher = UnconfinedTestDispatcher()
        val mockedResults = listOf(
            Result(
                id = "",
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
            onBlocking { getGlobalResults(GameMode.BLITZ, null, null, 2, null) } doReturn mockedResults
        }

        val impl = GetGlobalLeaderboardUseCaseImpl(dispatcher, resultRepository)
        val results = impl.invoke(GameMode.BLITZ, null, null, 2, null)
        assert(results.size == 1)
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
}