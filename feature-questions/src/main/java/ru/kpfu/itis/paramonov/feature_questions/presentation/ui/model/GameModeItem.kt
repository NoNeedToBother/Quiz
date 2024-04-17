package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model

import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.GameModeUiModel

class GameModeItem(
    val gameMode: GameModeUiModel
) {

    override fun toString(): String {
        return gameMode.name.normalizeEnumName()
    }
}