package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model

import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.CategoryUiModel

class CategoryItem(
    val category: CategoryUiModel
) {

    override fun toString(): String {
        return category.name.normalizeEnumName()
    }
}