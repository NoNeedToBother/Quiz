package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionBinding
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel
import javax.inject.Inject

class QuestionFragment: BaseFragment(R.layout.fragment_question) {

    private val binding: FragmentQuestionBinding by viewBinding(FragmentQuestionBinding::bind)

    @Inject
    lateinit var viewModel: QuestionsViewModel

    override fun inject() {
        (parentFragment as QuestionsFragment).questionsComponent.inject(this)
    }

    override fun initView() {}

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectQuestionData()
                }
            }
        }
    }

    private suspend fun collectQuestionData() {
        val position = requireArguments().getInt(POS_KEY)
        viewModel.getQuestionFlow(position).collect { data ->
            with(binding) {
                tvText.text = data.text
                val answerTextViews = listOf(tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4)
                for (i in answerTextViews.indices) {
                    answerTextViews[i].text = data.answers[i].answer
                }
            }
        }
    }

    companion object {
        fun newInstance(position: Int): QuestionFragment = QuestionFragment().apply {
            arguments = bundleOf(POS_KEY to position)
        }

        private const val POS_KEY = "pos"
    }
}