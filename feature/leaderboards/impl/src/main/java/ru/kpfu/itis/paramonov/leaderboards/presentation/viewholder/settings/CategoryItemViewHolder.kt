package ru.kpfu.itis.paramonov.leaderboards.presentation.viewholder.settings

import android.widget.TextView
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.CategoryUiModel
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.items.CategoryItem

class CategoryItemViewHolder {

    var tvDifficulty: TextView? = null

    fun bindItem(item: CategoryItem) {
        if (item.category?.name == CategoryUiModel.TV.name) tvDifficulty?.text = item.category.name
        else tvDifficulty?.text = item.category?.name?.normalizeEnumName() ?: "Any"
    }
}