package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.feature_questions.databinding.AnswerItemBinding
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerData

class AnswerViewHolder(
    private val binding: AnswerItemBinding,
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
    }

    fun onChosen(item: AnswerData) {
        this.item = item
        if (item.chosen) binding.tvAnswer.setTextColor(Color.RED)
        else binding.tvAnswer.setTextColor(Color.BLACK)
    }
}