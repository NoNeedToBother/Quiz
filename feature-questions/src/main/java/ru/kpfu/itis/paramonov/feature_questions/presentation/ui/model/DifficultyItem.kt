package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model

import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.DifficultyUiModel

class DifficultyItem(
    val difficulty: DifficultyUiModel
) {
    override fun toString(): String {
        return difficulty.name.lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}