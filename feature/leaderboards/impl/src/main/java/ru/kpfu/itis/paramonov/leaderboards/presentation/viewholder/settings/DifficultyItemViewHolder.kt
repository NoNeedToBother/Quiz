package ru.kpfu.itis.paramonov.leaderboards.presentation.viewholder.settings

import android.widget.TextView
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.items.DifficultyItem

class DifficultyItemViewHolder() {
    var tvDifficulty: TextView? = null

    fun bindItem(item: DifficultyItem) {
        tvDifficulty?.text = item.difficulty?.name?.normalizeEnumName() ?: "Any"
    }
}