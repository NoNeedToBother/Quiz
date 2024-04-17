package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.DifficultyItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.viewholder.DifficultyItemViewHolder

class DifficultyArrayAdapter(
    ctx: Context,
    private val items: List<DifficultyItem>,
    private val resourceManager: ResourceManager
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
            val viewHolder = DifficultyItemViewHolder(resourceManager)
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.difficulty_item, parent, false)

            viewHolder.ivDifficulty = view.findViewById(R.id.iv_difficulty)
            viewHolder.tvDifficulty = view.findViewById(R.id.tv_difficulty)

            view!!.tag = viewHolder
        }
        val holder = view.tag as DifficultyItemViewHolder
        val item = items[position]
        holder.bindItem(item)

        return view
    }
}