package ru.kpfu.itis.paramonov.questions.presentation.settings.model.items

import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.questions.presentation.settings.model.GameModeUiModel

class GameModeItem(
    val gameMode: GameModeUiModel
) {

    override fun toString(): String {
        return gameMode.name.normalizeEnumName()
    }
}