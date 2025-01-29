package ru.kpfu.itis.paramonov.leaderboards.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.leaderboards.databinding.ItemResultBinding
import ru.kpfu.itis.paramonov.leaderboards.presentation.adapter.diffutil.ResultDiffUtilCallback
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.viewholder.ResultViewHolder

class LeaderboardResultAdapter(
    diffUtilCallback: ResultDiffUtilCallback,
    private val resourceManager: ResourceManager,
    private val onResultClicked: (String, ImageView) -> Unit
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
        holder.binding.root.startAnimation(
            AnimationUtils.loadAnimation(holder.binding.root.context,
                ru.kpfu.itis.paramonov.ui.R.anim.recyler_view_item_enter_anim)
        )
    }
}