package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionBinding
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter.AnswerAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter.diffutil.AnswerDataDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel
import javax.inject.Inject

class QuestionFragment: BaseFragment(R.layout.fragment_question) {

    private val binding: FragmentQuestionBinding by viewBinding(FragmentQuestionBinding::bind)

    @Inject
    lateinit var viewModel: QuestionsViewModel

    @Inject
    lateinit var resourceManager: ResourceManager

    private var adapter: AnswerAdapter? = null

    override fun inject() {
        (parentFragment as QuestionsFragment).questionsComponent.inject(this)
    }

    override fun initView() {
        initRecyclerView()
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectQuestionData()
                }
            }
        }
    }

    private fun initRecyclerView() {
        with(binding) {
            val adapter = AnswerAdapter(AnswerDataDiffUtilCallback(), resourceManager, ::onAnswerChosen)
            this@QuestionFragment.adapter = adapter
            rvAnswers.adapter = adapter
            val gridLayoutManager = object: GridLayoutManager(
                requireContext(), 2, VERTICAL, false
            ) {
                override fun canScrollVertically(): Boolean = false
                override fun canScrollHorizontally(): Boolean = false
            }
            rvAnswers.layoutManager = gridLayoutManager
        }
    }

    private fun onAnswerChosen(chosenPos: Int) {
        val pos = requireArguments().getInt(POS_KEY)
        viewModel.updateChosenAnswers(pos, chosenPos)
    }

    private suspend fun collectQuestionData() {
        val position = requireArguments().getInt(POS_KEY)
        viewModel.getQuestionFlow(position).collect { data ->
            with(binding) {
                Log.i("a", data.answers.toString())
                tvText.text = data.text
                adapter?.submitList(data.answers)
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