package ru.kpfu.itis.paramonov.leaderboards.presentation.model.items

import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.CategoryUiModel

class CategoryItem(
    val category: CategoryUiModel?
) {
    val any: Boolean = (category == null)

    override fun toString(): String {
        if (any) return "Any"
        return category?.name?.normalizeEnumName() ?: "Any"
    }
}