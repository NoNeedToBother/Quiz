package ru.kpfu.itis.paramonov.questions.presentation.questions.fragments

import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.core.resources.ResourceManager
import ru.kpfu.itis.paramonov.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.databinding.FragmentQuestionBinding
import ru.kpfu.itis.paramonov.questions.presentation.questions.adapter.AnswerAdapter
import ru.kpfu.itis.paramonov.questions.presentation.questions.adapter.diffutil.AnswerDataDiffUtilCallback
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.BaseQuestionsViewModel
import javax.inject.Inject

abstract class BaseQuestionFragment<VM: BaseQuestionsViewModel>: BaseFragment(R.layout.fragment_question) {
    protected val binding: FragmentQuestionBinding by viewBinding(FragmentQuestionBinding::bind)

    lateinit var viewModel: VM

    @Inject
    lateinit var resourceManager: ResourceManager

    protected var adapter: AnswerAdapter? = null

    override fun initView() {
        init()
        initRecyclerView()
    }

    abstract fun init()

    private fun initRecyclerView() {
        with(binding) {
            val adapter = AnswerAdapter(AnswerDataDiffUtilCallback(), resourceManager, ::onAnswerChosen)
            this@BaseQuestionFragment.adapter = adapter
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

    abstract fun onAnswerChosen(chosenPos: Int)

    abstract fun collectQuestionData(data: QuestionDataUiModel)

    companion object {
        const val POS_KEY = "pos"
    }
}