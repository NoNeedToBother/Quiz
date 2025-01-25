package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items.DifficultyItem

class DifficultyItemViewHolder(
    private val resourceManager: ResourceManager
) {

    var ivDifficulty: ImageView? = null

    var tvDifficulty: TextView? = null

    var divider: View? = null

    fun bindItem(item: DifficultyItem) {
        tvDifficulty?.text = item.difficulty.name.normalizeEnumName()
        when(item.difficulty) {
            DifficultyUiModel.EASY -> onBindDifficultyLevel(R.drawable.difficulty_easy_circle)
            DifficultyUiModel.MEDIUM -> onBindDifficultyLevel(R.drawable.difficulty_medium_circle)
            DifficultyUiModel.HARD -> onBindDifficultyLevel(R.drawable.difficulty_hard_circle)
        }
    }

    private fun onBindDifficultyLevel(@DrawableRes drawableId: Int) {
        val circle = resourceManager.getDrawable(drawableId)
        ivDifficulty?.setImageDrawable(circle)
    }
}