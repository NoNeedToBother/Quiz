package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.items.DifficultyItem
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewholder.settings.DifficultyItemViewHolder

class DifficultyArrayAdapter(
    ctx: Context,
    private val items: List<DifficultyItem>
): ArrayAdapter<DifficultyItem>(ctx, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getDifficultyView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getDifficultyView(position, convertView, parent)
    }

    private fun getDifficultyView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val viewHolder = DifficultyItemViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.difficulty_item, parent, false)

            viewHolder.tvDifficulty = view.findViewById(R.id.tv_difficulty)

            view!!.tag = viewHolder
        }
        val holder = view.tag as DifficultyItemViewHolder
        val item = items[position]
        holder.bindItem(item)

        return view
    }
}