package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewholder

import android.view.View
import android.widget.TextView
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items.CategoryItem

class CategoryItemViewHolder {

    var divider: View? = null

    var tvDifficulty: TextView? = null

    fun bindItem(item: CategoryItem) {
        if (item.category.name == CategoryUiModel.TV.name) tvDifficulty?.text = item.category.name
        else tvDifficulty?.text = item.category.name.normalizeEnumName()
    }
}