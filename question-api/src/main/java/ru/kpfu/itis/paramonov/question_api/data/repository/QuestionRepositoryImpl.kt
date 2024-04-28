package ru.kpfu.itis.paramonov.question_api.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.common.model.data.Category
import ru.kpfu.itis.paramonov.common.model.data.Difficulty
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.question_api.R
import ru.kpfu.itis.paramonov.question_api.data.exceptions.UnknownParameterException
import ru.kpfu.itis.paramonov.question_api.data.handler.QuestionExceptionHandler
import ru.kpfu.itis.paramonov.question_api.data.mapper.QuestionDomainModelMapper
import ru.kpfu.itis.paramonov.question_api.data.remote.QuestionApi
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDomainModel
import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository

class QuestionRepositoryImpl(
    private val api: QuestionApi,
    private val mapper: QuestionDomainModelMapper,
    private val resourceManager: ResourceManager,
    private val exceptionHandler: QuestionExceptionHandler,
    private val dispatcher: CoroutineDispatcher
): QuestionRepository {
    override suspend fun getQuestions(amount: Int, difficulty: Difficulty, category: Int): QuestionDomainModel {
        return withContext(dispatcher) {
            try {
                val result = api.getQuestions(amount,
                    difficulty.name.normalizeEnumName().lowercase(), category)
                mapper.map(result)
            } catch (ex: Exception) {
                throw exceptionHandler.handle(ex)
            }
        }
    }

    override suspend fun getCategoryCode(category: Category): Int {
        return withContext(dispatcher) {
            try {
                val categories = api.getCategoriesId().info
                var res: Int? = null
                for (categoryInfo in categories) {
                    val name = categoryInfo.name
                    val categoryName = getCategoryByName(name)
                    if (category == categoryName) {
                        res = categoryInfo.id
                    }
                }
                res ?: throw UnknownParameterException(
                    resourceManager.getString(R.string.unknown_parameter)
                )
            } catch (ex: Exception) {
                throw exceptionHandler.handle(ex)
            }
        }
    }

    private fun getCategoryByName(categoryName: String): Category? {
        return when (categoryName) {
            GENERAL_CATEGORY_NAME -> Category.GENERAL
            BOOKS_CATEGORY_NAME -> Category.BOOK
            FILM_CATEGORY_NAME -> Category.FILM
            MUSIC_CATEGORY_NAME -> Category.MUSIC
            TV_CATEGORY_NAME -> Category.TV
            VIDEO_GAMES_CATEGORY_NAME -> Category.VIDEO_GAMES
            SPORTS_CATEGORY_NAME -> Category.SPORTS
            GEOGRAPHY_CATEGORY_NAME -> Category.GEOGRAPHY
            HISTORY_CATEGORY_NAME -> Category.HISTORY
            ANIMALS_CATEGORY_NAME -> Category.ANIMALS
            else -> null
        }
    }

    companion object {
        const val GENERAL_CATEGORY_NAME = "General Knowledge"
        const val BOOKS_CATEGORY_NAME = "Entertainment: Books"
        const val FILM_CATEGORY_NAME = "Entertainment: Film"
        const val MUSIC_CATEGORY_NAME = "Entertainment: Music"
        const val TV_CATEGORY_NAME = "Entertainment: Television"
        const val VIDEO_GAMES_CATEGORY_NAME = "Entertainment: Video Games"
        const val SPORTS_CATEGORY_NAME = "Sports"
        const val GEOGRAPHY_CATEGORY_NAME = "Geography"
        const val HISTORY_CATEGORY_NAME = "History"
        const val ANIMALS_CATEGORY_NAME = "Animals"
    }
}