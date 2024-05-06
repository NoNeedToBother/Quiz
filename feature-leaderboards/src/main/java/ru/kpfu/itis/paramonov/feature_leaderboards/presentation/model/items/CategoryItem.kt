package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.items

import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.CategoryUiModel

class CategoryItem(
    val category: CategoryUiModel?
) {
    val any: Boolean = (category == null)

    override fun toString(): String {
        if (any) return "Any"
        return category?.name?.normalizeEnumName() ?: "Any"
    }
}