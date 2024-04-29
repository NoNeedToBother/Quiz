package ru.kpfu.itis.paramonov.firebase.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.data.GameMode
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.firebase.data.utils.waitResult
import ru.kpfu.itis.paramonov.firebase.domain.model.Result
import ru.kpfu.itis.paramonov.firebase.domain.repository.ResultRepository
import kotlin.math.pow

class ResultRepositoryImpl(
    private val database: FirebaseFirestore,
    private val dispatcher: CoroutineDispatcher,
    private val resourceManager: ResourceManager
): ResultRepository {
    override suspend fun getByGameMode(gameMode: GameMode) {
    }

    override suspend fun save(result: Result): Double {
        return withContext(dispatcher) {
            val resultDocument = database.collection(RESULTS_COLLECTION_NAME)
            val score = calculateScore(
                time = result.time, correct = result.correct,
                total = result.total, gameMode = result.gameMode
            )
            val values = mutableMapOf<String, Any>(
                DB_USER_ID_FIELD to result.userId,
                DB_TIME_FIELD to result.time,
                DB_CORRECT_FIELD to result.correct,
                DB_TOTAL_FIELD to result.total,
                DB_SCORE_FIELD to score,
                DB_DIFFICULTY_FIELD to result.difficulty.name,
                DB_CATEGORY_FIELD to result.category.name,
                DB_GAME_MODE_FIELD to result.gameMode.name
            )
            resultDocument.add(values).waitResult()
            score
        }
    }

    private fun calculateScore(time: Int, correct: Int, total: Int, gameMode: GameMode): Double {
        val ratio = correct.toDouble() / total
        val ratioValue = Math.E.pow(ratio)
        val gameModeFactor = when(gameMode) {
            GameMode.BLITZ -> BLITZ_FACTOR
        }
        val timeValue = Math.E.pow((-1) * (time * gameModeFactor).pow(4))

        return ratioValue * (timeValue + 1) * 5 / (1 + Math.E)
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
    }
}