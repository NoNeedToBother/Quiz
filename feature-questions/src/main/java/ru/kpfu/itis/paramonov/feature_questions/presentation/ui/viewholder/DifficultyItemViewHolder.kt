package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.viewholder

import android.widget.ImageView
import android.widget.TextView
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.DifficultyItem

class DifficultyItemViewHolder(
    private val resourceManager: ResourceManager
) {
    var ivDifficulty: ImageView? = null

    var tvDifficulty: TextView? = null

    fun bindItem(item: DifficultyItem) {
        tvDifficulty?.text = item.difficulty.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        when(item.difficulty) {
            DifficultyUiModel.EASY -> {
                val circle = resourceManager.getDrawable(R.drawable.difficulty_easy_circle)
                ivDifficulty?.setImageDrawable(circle)
            }
            DifficultyUiModel.MEDIUM -> {
                val circle = resourceManager.getDrawable(R.drawable.difficulty_medium_circle)
                ivDifficulty?.setImageDrawable(circle)
            }
            DifficultyUiModel.HARD -> {
                val circle = resourceManager.getDrawable(R.drawable.difficulty_hard_circle)
                ivDifficulty?.setImageDrawable(circle)
            }
        }
    }
}