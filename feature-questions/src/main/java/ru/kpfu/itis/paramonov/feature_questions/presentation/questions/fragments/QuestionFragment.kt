package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel
import javax.inject.Inject

class QuestionFragment: BaseQuestionFragment<QuestionsViewModel>() {

    @Inject
    fun setViewModel(viewModel: QuestionsViewModel) {
        this.viewModel = viewModel
    }

    override fun init() {
        binding.tvInfo.setOnClickListener {
            viewModel.onQuestionsEnd()
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectQuestionData()
                }
                launch {
                    collectProceedingData()
                }
            }
        }
    }

    private suspend fun collectProceedingData() {
        viewModel.resultProceedingFlow.collect {
            binding.tvInfo.isEnabled = !it
        }
    }

    override fun onAnswerChosen(chosenPos: Int) {
        val pos = requireArguments().getInt(POS_KEY)
        viewModel.updateChosenAnswers(pos, chosenPos)
    }

    override suspend fun collectQuestionData() {
        val position = requireArguments().getInt(POS_KEY)
        viewModel.getQuestionFlow(position).collect { data ->
            with(binding) {
                tvText.text = data.text
                adapter?.submitList(data.answers)
            }
        }
    }

    override fun inject() {
        (parentFragment as QuestionsFragment).questionsComponent.inject(this)
    }

    fun showEndButton() {
        with(binding.tvInfo) {
            text = getString(R.string.end)
            show()
        }
    }

    companion object {
        fun newInstance(position: Int): QuestionFragment = QuestionFragment().apply {
            arguments = bundleOf(POS_KEY to position)
        }
    }
}