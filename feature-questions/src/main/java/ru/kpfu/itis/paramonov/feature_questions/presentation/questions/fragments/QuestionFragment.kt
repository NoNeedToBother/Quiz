package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionBinding
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionViewModel
import javax.inject.Inject

class QuestionFragment: BaseFragment(R.layout.fragment_question) {

    private val binding: FragmentQuestionBinding by viewBinding(FragmentQuestionBinding::bind)

    /*private val parent: QuestionsFragment = parentFragment as QuestionsFragment

    @Inject
    lateinit var viewModel: QuestionViewModel

    override fun inject() {
        parent.questionsComponent
            .inject(this)
    }*/

    override fun inject() {

    }

    override fun initView() {
        showQuestionTitle()
    }

    override fun observeData() {

    }

    /*
    override fun observeData() {
        val args = requireArguments()
        viewModel.getShuffledQuestions(
            args.getString(ANSWER_KEY) ?: throw RuntimeException("BAD"),
            args.getStringArrayList(INCORRECT_ANSWER_KEYS) ?: throw RuntimeException("BAD")
        )
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectAnswersData()
                }
            }
        }
    }

    private suspend fun collectAnswersData() {
        viewModel.answerDataFlow.collect { res ->
            if (res.isNotEmpty()) {
                with(binding) {
                    val answerTextViews = listOf(tvAnswer1, tvAnswer2, tvAnswer3, tvAnswer4)
                    for (i in answerTextViews.indices) {
                        answerTextViews[i].text = res[i].answer
                    }
                }
            }
        }
    }*/

    private fun showQuestionTitle() {
        with(binding) {
            val args = requireArguments()
            tvText.text = args.getString(TEXT_KEY)
        }
    }

    companion object {
        fun newInstance(
            questionText: String,
            questionAnswer: String,
            questionIncorrectAnswers: List<String>): QuestionFragment = QuestionFragment().apply {
            arguments = Bundle().apply {
                putString(TEXT_KEY, questionText)
                putString(ANSWER_KEY, questionAnswer)
                putStringArrayList(INCORRECT_ANSWER_KEYS, ArrayList(questionIncorrectAnswers))
            }
        }

        private const val TEXT_KEY = "text"
        private const val ANSWER_KEY = "answer"
        private const val INCORRECT_ANSWER_KEYS = "incorrect_answers"
    }
}