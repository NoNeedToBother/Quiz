package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.items

import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.GameModeUiModel

class GameModeItem(
    val gameMode: GameModeUiModel
) {

    override fun toString(): String {
        return gameMode.name.normalizeEnumName()
    }
}