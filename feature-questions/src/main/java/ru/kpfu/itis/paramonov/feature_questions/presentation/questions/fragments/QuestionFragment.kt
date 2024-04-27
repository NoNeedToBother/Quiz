package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.core.os.bundleOf
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel
import javax.inject.Inject

class QuestionFragment: BaseQuestionFragment() {

    @Inject
    fun setViewModel(viewModel: QuestionsViewModel) {
        this.viewModel = viewModel
    }

    override fun inject() {
        (parentFragment as QuestionsFragment).questionsComponent.inject(this)
    }

    companion object {
        fun newInstance(position: Int): QuestionFragment = QuestionFragment().apply {
            arguments = bundleOf(POS_KEY to position)
        }
    }
}