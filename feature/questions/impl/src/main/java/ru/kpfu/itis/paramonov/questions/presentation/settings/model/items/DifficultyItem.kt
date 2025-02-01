package ru.kpfu.itis.paramonov.questions.presentation.settings.model.items

import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.DifficultyUiModel

class DifficultyItem(
    val difficulty: DifficultyUiModel
) {
    override fun toString(): String {
        return difficulty.name.normalizeEnumName()
    }
}