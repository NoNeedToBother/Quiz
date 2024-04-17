package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.viewholder

import android.widget.TextView
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.CategoryItem

class CategoryItemViewHolder {

    var tvDifficulty: TextView? = null

    fun bindItem(item: CategoryItem) {
        tvDifficulty?.text = item.category.name.normalizeEnumName()
    }
}