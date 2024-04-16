package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.DifficultySpinnerItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.viewholder.DifficultySpinnerItemViewHolder

class DifficultySpinnerAdapter(
    ctx: Context,
    private val items: List<DifficultySpinnerItem>,
    private val resourceManager: ResourceManager
): ArrayAdapter<DifficultySpinnerItem>(ctx, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getDifficultyView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getDifficultyView(position, convertView, parent)
    }

    private fun getDifficultyView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val viewHolder = DifficultySpinnerItemViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.difficulty_spinner_item, parent, false)

            viewHolder.ivDifficulty = view.findViewById(R.id.iv_difficulty)
            viewHolder.tvDifficulty = view.findViewById(R.id.tv_difficulty)

            view!!.tag = viewHolder
        }
        val holder = view.tag as DifficultySpinnerItemViewHolder
        val item = items[position]
        holder.tvDifficulty?.text = item.difficulty.name.lowercase()
        when(item.difficulty) {
            DifficultyUiModel.EASY -> {
                val circle = resourceManager.getDrawable(R.drawable.difficulty_easy_circle)
                holder.ivDifficulty?.setImageDrawable(circle)
            }
            DifficultyUiModel.MEDIUM -> {
                val circle = resourceManager.getDrawable(R.drawable.difficulty_medium_circle)
                holder.ivDifficulty?.setImageDrawable(circle)
            }
            DifficultyUiModel.HARD -> {
                val circle = resourceManager.getDrawable(R.drawable.difficulty_hard_circle)
                holder.ivDifficulty?.setImageDrawable(circle)
            }
        }
        return view
    }
}