package ru.kpfu.itis.paramonov.questions.presentation.settings.model.items

import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.CategoryUiModel

class CategoryItem(
    val category: CategoryUiModel
) {

    override fun toString(): String {
        return category.name.normalizeEnumName()
    }
}