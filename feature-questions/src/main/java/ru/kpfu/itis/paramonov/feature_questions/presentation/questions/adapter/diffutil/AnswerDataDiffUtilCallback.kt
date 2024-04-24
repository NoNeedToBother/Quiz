package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.AnswerData

class AnswerDataDiffUtilCallback: DiffUtil.ItemCallback<AnswerData>() {

    override fun areItemsTheSame(oldItem: AnswerData, newItem: AnswerData): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: AnswerData, newItem: AnswerData): Boolean {
        val res = oldItem.let { o ->
            newItem.let {  n ->
                if (o.answer != n.answer) false
                else o.chosen == n.chosen
            }
        }
        return res
    }

    override fun getChangePayload(oldItem: AnswerData, newItem: AnswerData): Any {
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