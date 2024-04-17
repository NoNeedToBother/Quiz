package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.GameModeItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.viewholder.GameModeItemViewHolder

class GameModeArrayAdapter(ctx: Context,
    private val items: List<GameModeItem>
): ArrayAdapter<GameModeItem>(ctx, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getGameModeView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getGameModeView(position, convertView, parent)
    }

    private fun getGameModeView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val viewHolder = GameModeItemViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.game_mode_item, parent, false)

            viewHolder.tvGameMode = view.findViewById(R.id.tv_game_mode)
            viewHolder.divider = view.findViewById(R.id.game_mode_divider)

            view!!.tag = viewHolder
        }
        val holder = view.tag as GameModeItemViewHolder
        val item = items[position]
        holder.bindItem(item)

        return view
    }
}