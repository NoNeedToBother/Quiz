package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewholder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.AnswerItemBinding
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerData

class AnswerViewHolder(
    private val binding: AnswerItemBinding,
    private val resourceManager: ResourceManager,
    onAnswerChosen: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var item: AnswerData? = null

    init {
        binding.root.setOnClickListener {
            onAnswerChosen(adapterPosition)
        }
    }

    fun bindItem(item: AnswerData) {
        this.item = item
        binding.tvAnswer.text = item.answer
        setChosenMark(item.chosen)
    }

    fun onChosen(item: AnswerData) {
        this.item = item
        setChosenMark(item.chosen)
    }

    private fun setChosenMark(chosen: Boolean) {
        if (chosen) binding.ivMark.setImageDrawable(
            resourceManager.getDrawable(R.drawable.mark_checked)
        )
        else binding.ivMark.setImageDrawable(
            resourceManager.getDrawable(R.drawable.mark)
        )
    }
}