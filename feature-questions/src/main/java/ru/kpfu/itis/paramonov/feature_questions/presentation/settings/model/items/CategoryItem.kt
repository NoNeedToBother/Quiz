package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items

import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel

class CategoryItem(
    val category: CategoryUiModel
) {

    override fun toString(): String {
        return category.name.normalizeEnumName()
    }
}