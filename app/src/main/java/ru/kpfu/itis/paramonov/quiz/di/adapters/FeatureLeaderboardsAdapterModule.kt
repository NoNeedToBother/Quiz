package ru.kpfu.itis.paramonov.quiz.di.adapters

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.leaderboards.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository as DatabaseQuestionSettingsRepository
import ru.kpfu.itis.paramonov.leaderboards.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository as FirebaseResultRepository
import ru.kpfu.itis.paramonov.quiz.mapper.leaderboards.ResultToFeatureLeaderboardsResultMapper

val featureLeaderboardsAdapterModule = DI.Module("FeatureLeaderboardsAdapterModule") {
    bind<ResultRepository>() with provider {
        val resultRepository: FirebaseResultRepository = instance()
        val resultToFeatureLeaderboardsResultMapper: ResultToFeatureLeaderboardsResultMapper
            = instance()
        object : ResultRepository {
            override suspend fun getGlobalResults(
                gameMode: GameMode,
                difficulty: Difficulty?,
                category: Category?,
                max: Int,
                afterScore: Double?
            ): List<ru.kpfu.itis.paramonov.leaderboards.api.model.Result> =
                resultRepository.getGlobalResults(
                    gameMode, difficulty, category, max, afterScore
                ).map { resultToFeatureLeaderboardsResultMapper.map(it) }

            override suspend fun getFriendsResults(
                gameMode: GameMode,
                difficulty: Difficulty?,
                category: Category?,
                max: Int,
                afterScore: Double?
            ): List<ru.kpfu.itis.paramonov.leaderboards.api.model.Result> =
                resultRepository.getFriendsResults(
                    gameMode, difficulty, category, max, afterScore
                ).map { resultToFeatureLeaderboardsResultMapper.map(it) }
        }
    }

    bind<QuestionSettingsRepository>() with provider {
        val sharedPreferencesRepository: DatabaseQuestionSettingsRepository = instance()
        object : QuestionSettingsRepository {
            override fun getDifficulty(): Difficulty = sharedPreferencesRepository.getDifficulty()

            override fun getGameMode(): GameMode = sharedPreferencesRepository.getGameMode()
        }
    }
}
