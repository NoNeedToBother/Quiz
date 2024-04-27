package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.core.os.bundleOf
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.TrainingQuestionsViewModel
import javax.inject.Inject

class TrainingQuestionFragment: BaseQuestionFragment() {

    @Inject
    fun setViewModel(viewModel: TrainingQuestionsViewModel) {
        this.viewModel = viewModel
    }

    override fun inject() {
        (parentFragment as TrainingQuestionsFragment).trainingQuestionsComponent.inject(this)
    }

    companion object {
        fun newInstance(position: Int): TrainingQuestionFragment = TrainingQuestionFragment().apply {
            arguments = bundleOf(POS_KEY to position)
        }
    }
}