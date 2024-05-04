package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel

class ResultDiffUtilCallback: DiffUtil.ItemCallback<ResultUiModel>() {
    override fun areItemsTheSame(oldItem: ResultUiModel, newItem: ResultUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ResultUiModel, newItem: ResultUiModel): Boolean {
        return oldItem == newItem
    }
}