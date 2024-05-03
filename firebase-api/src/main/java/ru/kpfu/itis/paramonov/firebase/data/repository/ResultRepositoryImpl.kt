package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.model.data.GameMode
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.R
import ru.kpfu.itis.paramonov.firebase.data.exceptions.ResultDataException
import ru.kpfu.itis.paramonov.firebase.data.utils.waitResult
import ru.kpfu.itis.paramonov.firebase.domain.model.Result
import ru.kpfu.itis.paramonov.firebase.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.domain.repository.UserRepository
import kotlin.math.pow

class ResultRepositoryImpl(
    private val database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager,
    private val userRepository: UserRepository
): ResultRepository {
    override suspend fun get(
        gameMode: GameMode,
        difficulty: Difficulty?,
        category: Category?,
        max: Int,
        afterScore: Double?
    ): List<Result> {
        return withContext(dispatcher) {
            var query = database.collection(RESULTS_COLLECTION_NAME)
                .whereEqualTo(DB_GAME_MODE_FIELD, getGameModeValForDatabase(gameMode))
                .apply {
                    difficulty?.let {
                        whereEqualTo(DB_DIFFICULTY_FIELD, getDifficultyValForDatabase(difficulty))
                    }
                    category?.let {
                        whereEqualTo(DB_CATEGORY_FIELD, getCategoryValForDatabase(category))
                    }
                }
            afterScore?.let {
                query = query.whereLessThan(DB_SCORE_FIELD, it)
            }
            val task = query
                .orderBy(DB_SCORE_FIELD, Query.Direction.DESCENDING)
                .limit(max.toLong())
                .get().waitResult()


            if (task.isSuccessful) {
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
                DB_GAME_MODE_FIELD to getGameModeValForDatabase(result.gameMode)
            )
            resultDocument.add(values).waitResult()
            score
        }
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
        val gameModeFactor = when(gameMode) {
            GameMode.BLITZ -> BLITZ_FACTOR
        }
        val timeValue = Math.E.pow((-1) * (time * gameModeFactor).pow(4))

        return ratioValue * (timeValue + 1) * MAX_SCORE / (1 + Math.E)
    }

    private suspend fun DocumentSnapshot.getResult(): Result {
        val userId = data?.get(DB_USER_ID_FIELD) as String
        val time = this.get(DB_TIME_FIELD, Int::class.java) as Int
        val correct = this.get(DB_CORRECT_FIELD, Int::class.java) as Int
        val total = this.get(DB_TOTAL_FIELD, Int::class.java) as Int
        val score = this.get(DB_SCORE_FIELD, Double::class.java) as Double
        val difficulty = data?.get(DB_DIFFICULTY_FIELD) as String
        val category = data?.get(DB_CATEGORY_FIELD) as String
        val gameMode = data?.get(DB_GAME_MODE_FIELD) as String
        return Result(
            user = userRepository.getUser(userId).get(),
            time = time, correct = correct, total = total, score = score,
            difficulty = Difficulty.valueOf(difficulty),
            category = Category.valueOf(category),
            gameMode = GameMode.valueOf(gameMode)
        )
    }

    companion object {
        const val RESULTS_COLLECTION_NAME = "results"
        const val DB_USER_ID_FIELD = "userId"
        const val DB_TIME_FIELD = "time"
        const val DB_CORRECT_FIELD = "correct"
        const val DB_TOTAL_FIELD = "total"
        const val DB_SCORE_FIELD = "score"
        const val DB_DIFFICULTY_FIELD = "difficulty"
        const val DB_CATEGORY_FIELD = "category"
        const val DB_GAME_MODE_FIELD = "gameMode"

        private const val BLITZ_FACTOR = (1).toDouble() / 10 / 5
        private const val MAX_SCORE = 10
    }
}