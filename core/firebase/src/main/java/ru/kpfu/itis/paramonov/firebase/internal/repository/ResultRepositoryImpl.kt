package ru.kpfu.itis.paramonov.firebase.internal.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.DateTimeParser
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.external.exceptions.ResultDataException
import ru.kpfu.itis.paramonov.firebase.internal.utils.waitResult
import ru.kpfu.itis.paramonov.firebase.external.model.Result
import ru.kpfu.itis.paramonov.firebase.external.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.repository.UserRepository
import kotlin.math.pow

internal class ResultRepositoryImpl(
    private val database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager,
    private val userRepository: UserRepository,
    private val dateTimeParser: DateTimeParser
): ResultRepository {
    override suspend fun getGlobalResults(
        gameMode: GameMode,
        difficulty: Difficulty?,
        category: Category?,
        max: Int,
        afterScore: Double?
    ): List<Result> {
        return withContext(dispatcher) {
            val query = getStandardQuery(
                gameMode = gameMode, difficulty = difficulty,
                category = category, afterScore = afterScore
            )
            executeResultQueryAndGetResult(query, max)
        }
    }

    override suspend fun getFriendsResults(
        gameMode: GameMode,
        difficulty: Difficulty?,
        category: Category?,
        max: Int,
        afterScore: Double?
    ): List<Result> {
        return withContext(dispatcher) {
            var query = getStandardQuery(
                gameMode = gameMode, difficulty = difficulty,
                category = category, afterScore = afterScore
            )
            val friendList = userRepository.getCurrentUser()?.friendIdList ?: listOf()
            if (friendList.isEmpty()) {
                listOf()
            }
            else {
                query = query.whereIn(DB_USER_ID_FIELD, friendList)
                executeResultQueryAndGetResult(query, max)
            }
        }
    }

    private suspend fun executeResultQueryAndGetResult(query: Query, max: Int): List<Result> {
        val task = query.orderBy(DB_SCORE_FIELD, Query.Direction.DESCENDING)
            .limit(max.toLong())
            .get()
            .waitResult()

        return if (task.isSuccessful) {
            val res = mutableListOf<Result>()
            val documents = task.result.documents
            for (doc in documents) {
                try {
                    res.add(doc.getResult())
                } catch (_: Throwable) {}
            }
            res
        } else {
            throw ResultDataException(
                resourceManager.getString(R.string.result_data_failed)
            )
        }
    }

    override suspend fun save(result: Result): Double {
        return withContext(dispatcher) {
            val resultDocument = database.collection(RESULTS_COLLECTION_NAME)
            val score = calculateScore(
                time = result.time, correct = result.correct,
                total = result.total, gameMode = result.gameMode
            )
            val values = mutableMapOf<String, Any>(
                DB_USER_ID_FIELD to result.user.id,
                DB_TIME_FIELD to result.time,
                DB_CORRECT_FIELD to result.correct,
                DB_TOTAL_FIELD to result.total,
                DB_SCORE_FIELD to score,
                DB_DIFFICULTY_FIELD to getDifficultyValForDatabase(result.difficulty),
                DB_CATEGORY_FIELD to getCategoryValForDatabase(result.category),
                DB_GAME_MODE_FIELD to getGameModeValForDatabase(result.gameMode),
                DB_DATE_FIELD to dateTimeParser.parseMillisToString(System.currentTimeMillis())
            )
            val id = resultDocument.add(values).waitResult().result.get().waitResult().result.id
            resultDocument.document(id).set(
                mapOf(DB_ID_FIELD to id), SetOptions.mergeFields(DB_ID_FIELD)
            )
            score
        }
    }

    override suspend fun getMaxScore(): Double {
        return MAX_SCORE.toDouble()
    }

    override suspend fun getLastResults(max: Int): List<Result> {
        return withContext(dispatcher) {
            val user = userRepository.getCurrentUser() ?:
                throw ResultDataException(resourceManager.getString(R.string.result_data_failed))
            getLastResults(max, user.id)
        }
    }

    override suspend fun getLastResults(max: Int, id: String): List<Result> {
        return withContext(dispatcher) {
            val task = database.collection(RESULTS_COLLECTION_NAME)
                .whereEqualTo(DB_USER_ID_FIELD, id)
                .get().waitResult()
            if (task.isSuccessful) {
                var results = mutableListOf<Result>()
                task.result.documents.forEach { doc ->
                    try {
                        results.add(doc.getResult())
                    } catch (_: Throwable) {}
                }
                results = results.sortedWith { r1, r2 ->
                    r1.date.compareTo(r2.date)
                }.toMutableList()
                if (results.size > max) {
                    results.subList(results.size - max, results.size)
                } else results
            } else throw ResultDataException(resourceManager.getString(R.string.result_data_failed))
        }
    }

    private fun getStandardQuery(
        gameMode: GameMode,
        difficulty: Difficulty?,
        category: Category?,
        afterScore: Double?
    ): Query {
        var query = database.collection(RESULTS_COLLECTION_NAME)
            .whereEqualTo(DB_GAME_MODE_FIELD, getGameModeValForDatabase(gameMode))
        category?.let {
            query = query.whereEqualTo(DB_CATEGORY_FIELD, getCategoryValForDatabase(category))
        }
        difficulty?.let {
            query = query.whereEqualTo(DB_DIFFICULTY_FIELD, getDifficultyValForDatabase(difficulty))
        }
        afterScore?.let {
            query = query.whereLessThan(DB_SCORE_FIELD, it)
        }
        return query
    }

    private fun getGameModeValForDatabase(gameMode: GameMode): String {
        return gameMode.name
    }
    private fun getDifficultyValForDatabase(difficulty: Difficulty): String {
        return difficulty.name
    }
    private fun getCategoryValForDatabase(category: Category): String {
        return category.name
    }

    private fun calculateScore(time: Int, correct: Int, total: Int, gameMode: GameMode): Double {
        val ratio = correct.toDouble() / total
        val ratioValue = Math.E.pow(ratio)
        val gameModeFactor =
            when(gameMode) {
                GameMode.BLITZ -> BLITZ_FACTOR
                GameMode.CHALLENGE -> CHALLENGE_FACTOR
                GameMode.EXPERT -> EXPERT_CHALLENGE
        }
        val timeValue = Math.E.pow((-1) * (time * gameModeFactor).pow(TIME_POW_FACTOR))

        return ratioValue * (timeValue + 1) / 2 / Math.E * MAX_SCORE
    }

    private suspend fun DocumentSnapshot.getResult(): Result {
        val id = data?.get(DB_ID_FIELD) as String
        val userId = data?.get(DB_USER_ID_FIELD) as String
        val time = this.get(DB_TIME_FIELD, Int::class.java) as Int
        val correct = this.get(DB_CORRECT_FIELD, Int::class.java) as Int
        val total = this.get(DB_TOTAL_FIELD, Int::class.java) as Int
        val score = this.get(DB_SCORE_FIELD, Double::class.java) as Double
        val difficulty = data?.get(DB_DIFFICULTY_FIELD) as String
        val category = data?.get(DB_CATEGORY_FIELD) as String
        val gameMode = data?.get(DB_GAME_MODE_FIELD) as String
        val date = data?.get(DB_DATE_FIELD) as String
        val user = userRepository.getUser(userId) ?: throw NullPointerException()
        return Result(
            id = id, user = user,
            time = time, correct = correct, total = total, score = score,
            difficulty = Difficulty.valueOf(difficulty),
            category = Category.valueOf(category),
            gameMode = GameMode.valueOf(gameMode),
            date = dateTimeParser.parseString(date)
        )
    }

    companion object {
        private const val RESULTS_COLLECTION_NAME = "results"
        private const val DB_ID_FIELD = "id"
        private const val DB_USER_ID_FIELD = "userId"
        private const val DB_TIME_FIELD = "time"
        private const val DB_CORRECT_FIELD = "correct"
        private const val DB_TOTAL_FIELD = "total"
        private const val DB_SCORE_FIELD = "score"
        private const val DB_DIFFICULTY_FIELD = "difficulty"
        private const val DB_CATEGORY_FIELD = "category"
        private const val DB_GAME_MODE_FIELD = "gameMode"
        private const val DB_DATE_FIELD = "date"

        private const val BLITZ_FACTOR = (1).toDouble() / 10 / 5
        private const val CHALLENGE_FACTOR = (1).toDouble() / 15 / 8
        private const val EXPERT_CHALLENGE = (1).toDouble() / 25 / 10
        private const val TIME_POW_FACTOR = 4

        private const val MAX_SCORE = 10
    }
}
