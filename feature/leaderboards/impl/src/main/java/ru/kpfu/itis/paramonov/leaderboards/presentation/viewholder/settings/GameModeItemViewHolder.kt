package ru.kpfu.itis.paramonov.leaderboards.presentation.viewholder.settings

import android.widget.TextView
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.items.GameModeItem

class GameModeItemViewHolder {

    var tvGameMode: TextView? = null

    fun bindItem(item: GameModeItem) {
        tvGameMode?.text = item.gameMode.name.normalizeEnumName()
    }
}