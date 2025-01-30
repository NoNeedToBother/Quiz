package ru.kpfu.itis.paramonov.questions.presentation.questions.viewholder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.databinding.AnswerItemBinding
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.AnswerDataUiModel

class AnswerViewHolder(
    private val binding: AnswerItemBinding,
    private val resourceManager: ResourceManager,
    onAnswerChosen: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    private var item: AnswerDataUiModel? = null

    init {
        binding.root.setOnClickListener {
            onAnswerChosen(adapterPosition)
        }
    }

    fun bindItem(item: AnswerDataUiModel) {
        this.item = item
        binding.tvAnswer.text = item.answer
        setChosenMark(item.chosen)
    }

    fun onChosen(item: AnswerDataUiModel) {
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