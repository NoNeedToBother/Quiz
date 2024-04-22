package ru.kpfu.itis.paramonov.question_api.data.repository

import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.Categories
import ru.kpfu.itis.paramonov.question_api.R
import ru.kpfu.itis.paramonov.question_api.data.exceptions.UnknownParameterException
import ru.kpfu.itis.paramonov.question_api.data.mapper.QuestionDomainModelMapper
import ru.kpfu.itis.paramonov.question_api.data.remote.QuestionApi
import ru.kpfu.itis.paramonov.question_api.domain.model.QuestionDomainModel
import ru.kpfu.itis.paramonov.question_api.domain.repository.QuestionRepository

class QuestionRepositoryImpl(
    private val api: QuestionApi,
    private val mapper: QuestionDomainModelMapper,
    private val resourceManager: ResourceManager
): QuestionRepository {
    override suspend fun getQuestions(amount: Int, difficulty: String, category: Int): QuestionDomainModel {
        val result = api.getQuestions(amount, difficulty, category)
        return mapper.map(result)
    }

    override suspend fun getCategoryCode(category: String): Int {
        val categories = api.getCategoriesId().info
        for (categoryInfo in categories) {
            val name = categoryInfo.name
            val categoryName = getCategoryByName(name)
            if (category == categoryName) {
                return categoryInfo.id
            }
        }
        throw UnknownParameterException(
            resourceManager.getString(R.string.unknown_parameter)
        )
    }

    private fun getCategoryByName(categoryName: String): String? {
        return when (categoryName) {
            GENERAL_CATEGORY_NAME -> Categories.GENERAL_CATEGORY
            BOOKS_CATEGORY_NAME -> Categories.BOOK_CATEGORY
            FILM_CATEGORY_NAME -> Categories.FILM_CATEGORY
            MUSIC_CATEGORY_NAME -> Categories.MUSIC_CATEGORY
            TV_CATEGORY_NAME -> Categories.TV_CATEGORY
            VIDEO_GAMES_CATEGORY_NAME -> Categories.VIDEO_GAMES_CATEGORY
            SPORTS_CATEGORY_NAME -> Categories.SPORTS_CATEGORY
            GEOGRAPHY_CATEGORY_NAME -> Categories.GEOGRAPHY_CATEGORY
            HISTORY_CATEGORY_NAME -> Categories.HISTORY_CATEGORY
            ANIMALS_CATEGORY_NAME -> Categories.ANIMALS_CATEGORY
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