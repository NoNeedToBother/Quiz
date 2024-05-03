package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.ItemResultBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.diffutil.ResultDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewholder.ResultViewHolder

class LeaderboardResultAdapter(
    diffUtilCallback: ResultDiffUtilCallback,
    private val resourceManager: ResourceManager,
    private val onResultClicked: (String) -> Unit
): ListAdapter<ResultUiModel, ResultViewHolder>(diffUtilCallback) {
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
        holder.bindItem(getItem(position))
    }

    override fun onBindViewHolder(
        holder: ResultViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {

        } else super.onBindViewHolder(holder, position, payloads)
    }
}