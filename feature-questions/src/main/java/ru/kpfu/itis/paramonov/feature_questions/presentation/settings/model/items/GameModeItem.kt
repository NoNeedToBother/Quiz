package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items

import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel

class GameModeItem(
    val gameMode: GameModeUiModel
) {

    override fun toString(): String {
        return gameMode.name.normalizeEnumName()
    }
}