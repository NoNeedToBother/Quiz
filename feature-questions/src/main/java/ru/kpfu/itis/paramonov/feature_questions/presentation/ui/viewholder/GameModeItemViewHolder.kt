package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.viewholder

import android.widget.TextView
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.GameModeItem

class GameModeItemViewHolder {

    var tvGameMode: TextView? = null

    fun bindItem(item: GameModeItem) {
        tvGameMode?.text = item.gameMode.name.normalizeEnumName()
    }
}