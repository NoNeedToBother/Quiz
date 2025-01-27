package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.core.os.bundleOf
import ru.kpfu.itis.paramonov.core.utils.collect
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.TrainingQuestionsViewModel
import javax.inject.Inject

class TrainingQuestionFragment: BaseQuestionFragment<TrainingQuestionsViewModel>() {

    @Inject
    fun setViewModel(viewModel: TrainingQuestionsViewModel) {
        this.viewModel = viewModel
    }

    override fun init() {}

    override fun onAnswerChosen(chosenPos: Int) {
        val pos = requireArguments().getInt(POS_KEY)
        viewModel.updateChosenAnswers(pos, chosenPos)
    }

    override fun observeData() {
        val position = requireArguments().getInt(POS_KEY)
        viewModel.getQuestionFlow(position).collect(lifecycleOwner = viewLifecycleOwner) {
            collectQuestionData(it)
        }
    }

    override fun collectQuestionData(data: QuestionDataUiModel) {
        with(binding) {
            tvText.text = data.text
            adapter?.submitList(data.answers)
            var anyChosen = false
            var correctlyChosen = false
            for (answer in data.answers) {
                if (answer.chosen) anyChosen = true
                if (answer.chosen && answer.correct) correctlyChosen = true
            }
            if (correctlyChosen) tvInfo.text = getString(R.string.correct_guess)
            else if (anyChosen) tvInfo.text = getString(R.string.incorrect_guess)
            else tvInfo.text = getString(ru.kpfu.itis.paramonov.ui.R.string.empty)
        }
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