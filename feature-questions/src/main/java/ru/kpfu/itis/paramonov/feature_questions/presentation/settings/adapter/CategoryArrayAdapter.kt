package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items.CategoryItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewholder.CategoryItemViewHolder

class CategoryArrayAdapter(
    ctx: Context,
    private val items: List<CategoryItem>
): ArrayAdapter<CategoryItem>(ctx, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCategoryView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCategoryView(position, convertView, parent)
    }

    private fun getCategoryView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val viewHolder = CategoryItemViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.category_item, parent, false)

            viewHolder.tvDifficulty = view.findViewById(R.id.tv_category)
            viewHolder.divider = view.findViewById(R.id.category_divider)

            view!!.tag = viewHolder
        }
        val holder = view.tag as CategoryItemViewHolder
        val item = items[position]
        holder.bindItem(item)

        return view
    }
}