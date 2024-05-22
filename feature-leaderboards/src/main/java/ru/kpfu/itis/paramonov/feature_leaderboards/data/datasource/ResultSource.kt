package ru.kpfu.itis.paramonov.feature_leaderboards.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.ResultRepository
import ru.kpfu.itis.paramonov.firebase.external.domain.model.Result


class ResultSource(
    private val resultRepository: ResultRepository,
    private val sourceType: SourceType
): PagingSource<Int, Result>() {

    private var mediator: ResultSettingsMediator? = null

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        try {
            mediator?.let {
                val results = when(sourceType) {
                    SourceType.FRIENDS -> resultRepository.getFriendsResults(
                        gameMode = it.gameMode, difficulty = it.difficulty,
                        category = it.category, afterScore = it.after
                    )
                    SourceType.GLOBAL -> resultRepository.getGlobalResults(
                        gameMode = it.gameMode, difficulty = it.difficulty,
                        category = it.category, afterScore = it.after
                    )
                }
                var nextPage = params.key?.plus(1)
                val prevPage = if (params.key == 1) null else params.key
                if (results.size <= params.loadSize) nextPage = null
                return LoadResult.Page(
                    results, prevPage, nextPage
                )
            } ?: throw RuntimeException("Mediator not set")
        } catch (ex: Throwable) {
            return LoadResult.Error(ex)
        }
    }

    fun withMediator(mediator: ResultSettingsMediator): ResultSource = this.apply {
        this.mediator = mediator
    }

    enum class SourceType {
        FRIENDS, GLOBAL
    }
}