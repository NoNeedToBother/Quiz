package ru.kpfu.itis.paramonov.quiz.di.adapters

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider
import ru.kpfu.itis.paramonov.core.model.data.Category
import ru.kpfu.itis.paramonov.core.model.data.Difficulty
import ru.kpfu.itis.paramonov.core.model.data.GameMode
import ru.kpfu.itis.paramonov.questions.api.model.Question
import ru.kpfu.itis.paramonov.questions.api.model.Result
import ru.kpfu.itis.paramonov.questions.api.model.User
import ru.kpfu.itis.paramonov.questions.api.model.database.DatabaseQuestion
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.QuestionSettingsRepository
import ru.kpfu.itis.paramonov.questions.api.repository.ResultRepository
import ru.kpfu.itis.paramonov.questions.api.repository.SavedQuestionRepository
import ru.kpfu.itis.paramonov.questions.api.repository.UserRepository
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FeatureQuestionsResultToResultMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.FirebaseUserToFeatureQuestionsUserMapper
import ru.kpfu.itis.paramonov.quiz.mapper.feature_questions.QuestionToFeatureQuestionsQuestionMapper

val featureQuestionsAdapterModule = DI.Module("FeatureQuestionsAdapterModule") {
    bind<UserRepository>() with provider {
        val userRepository: ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
            = instance()
        val firebaseUserToFeatureQuestionsUserMapper: FirebaseUserToFeatureQuestionsUserMapper = instance()

        object : UserRepository {
            override suspend fun getCurrentUser(): User? =
                userRepository.getCurrentUser()?.let {
                    firebaseUserToFeatureQuestionsUserMapper.map(it)
                }
        }
    }

    bind<QuestionRepository>() with provider {
        val questionRepository: ru.kpfu.itis.paramonov.network.external.domain.repository.QuestionRepository
            = instance()
        val questionToFeatureQuestionsQuestionMapper: QuestionToFeatureQuestionsQuestionMapper = instance()

        object : QuestionRepository {
            override suspend fun getCategoryCode(category: Category): Int {
                return questionRepository.getCategoryCode(category)
            }

            override suspend fun getQuestions(
                amount: Int,
                difficulty: Difficulty,
                category: Int
            ): Question {
                return questionToFeatureQuestionsQuestionMapper.map(
                    questionRepository.getQuestions(amount, difficulty, category)
                )
            }
        }
    }

    bind<QuestionSettingsRepository>() with provider {
        val sharedPreferencesRepository: ru.kpfu.itis.paramonov.database.external.domain.repository.QuestionSettingsRepository
            = instance()
        object : QuestionSettingsRepository {
            override fun getDifficulty(): Difficulty = sharedPreferencesRepository.getDifficulty()

            override fun saveDifficulty(difficulty: Difficulty) {
                sharedPreferencesRepository.saveDifficulty(difficulty)
            }

            override fun getCategory(): Category = sharedPreferencesRepository.getCategory()

            override fun saveCategory(category: Category) {
                sharedPreferencesRepository.saveCategory(category)
            }

            override fun getGameMode(): GameMode = sharedPreferencesRepository.getGameMode()

            override fun saveGameMode(gameMode: GameMode) {
                sharedPreferencesRepository.saveGameMode(gameMode)
            }

            override fun getLimit(): Int = sharedPreferencesRepository.getLimit()

            override fun saveLimit(limit: Int) {
                sharedPreferencesRepository.saveLimit(limit)
            }
        }
    }

    bind<ResultRepository>() with provider {
        val resultRepository: ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
            = instance()
        val featureQuestionsResultToResultMapper: FeatureQuestionsResultToResultMapper = instance()
        object : ResultRepository {
            override suspend fun getMaxScore(): Double = resultRepository.getMaxScore()

            override suspend fun save(result: Result): Double {
                return resultRepository.save(featureQuestionsResultToResultMapper.map(result))
            }
        }
    }

    bind<SavedQuestionRepository>() with provider {
        val savedQuestionRepository: ru.kpfu.itis.paramonov.database.external.domain.repository.SavedQuestionRepository
            = instance()
        val databaseQuestionToFeatureQuestionsDatabaseQuestionMapper: DatabaseQuestionToFeatureQuestionsDatabaseQuestionMapper
            = instance()
        val featureQuestionsDatabaseQuestionToDatabaseQuestionMapper: FeatureQuestionsDatabaseQuestionToDatabaseQuestionMapper
            = instance()

        object : SavedQuestionRepository {
            override suspend fun getQuestions(limit: Int): List<DatabaseQuestion> =
                savedQuestionRepository.getQuestions(limit)
                    .map { databaseQuestionToFeatureQuestionsDatabaseQuestionMapper.map(it) }

            override suspend fun saveQuestions(questions: List<DatabaseQuestion>) {
                savedQuestionRepository.saveQuestions(
                    questions.map { featureQuestionsDatabaseQuestionToDatabaseQuestionMapper.map(it) }
                )
            }
        }
    }
}