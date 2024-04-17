package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewholder

import android.view.View
import android.widget.TextView
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items.GameModeItem

class GameModeItemViewHolder {

    var divider: View? = null

    var tvGameMode: TextView? = null

    fun bindItem(item: GameModeItem) {
        tvGameMode?.text = item.gameMode.name.normalizeEnumName()
    }
}