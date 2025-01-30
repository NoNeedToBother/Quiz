package ru.kpfu.itis.paramonov.questions.presentation.questions.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.AnswerDataUiModel

class AnswerDataDiffUtilCallback: DiffUtil.ItemCallback<AnswerDataUiModel>() {

    override fun areItemsTheSame(oldItem: AnswerDataUiModel, newItem: AnswerDataUiModel): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: AnswerDataUiModel, newItem: AnswerDataUiModel): Boolean {
        val res = oldItem.let { o ->
            newItem.let {  n ->
                if (o.answer != n.answer) false
                else o.chosen == n.chosen
            }
        }
        return res
    }

    override fun getChangePayload(oldItem: AnswerDataUiModel, newItem: AnswerDataUiModel): Any {
        return oldItem.let { o ->
            newItem.let {  n ->
                val payload = AnswerDataPayload()
                if (o.chosen != n.chosen) payload.chosen = n.chosen
                payload
            }
        }
    }

    class AnswerDataPayload {
        var chosen: Boolean? = null
    }
}