package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.feature_questions.databinding.AnswerItemBinding
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter.diffutil.AnswerDataDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerData
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewholder.AnswerViewHolder

class AnswerAdapter(
    diffUtilCallback: AnswerDataDiffUtilCallback,
    private val resourceManager: ResourceManager,
    private val onAnswerChosen: (Int) -> Unit
): ListAdapter<AnswerData, AnswerViewHolder>(diffUtilCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            binding = AnswerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onAnswerChosen = onAnswerChosen,
            resourceManager = resourceManager
        )
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    override fun onBindViewHolder(
        holder: AnswerViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val changes = payloads.first() as AnswerDataDiffUtilCallback.AnswerDataPayload
            changes.chosen?.let {
                holder.onChosen(getItem(position))
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}