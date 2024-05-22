package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.ItemResultBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.diffutil.ResultDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewholder.ResultViewHolder

class LeaderboardResultPagingAdapter(
    diffUtilCallback: ResultDiffUtilCallback,
    private val resourceManager: ResourceManager,
    private val onResultClicked: (String) -> Unit
): PagingDataAdapter<ResultUiModel, ResultViewHolder>(diffUtilCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        return ResultViewHolder(
            binding = ItemResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            resourceManager = resourceManager,
            onResultClicked = onResultClicked
        )
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bindItem(it)
        }
    }
}