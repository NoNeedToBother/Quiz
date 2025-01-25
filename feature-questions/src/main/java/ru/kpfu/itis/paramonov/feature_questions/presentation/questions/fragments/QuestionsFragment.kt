package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.core.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.core.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.core.utils.collect
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionsBinding
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter.QuestionsViewPagerAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di.QuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.utils.QuestionViewPagerTransformer
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.BaseQuestionsViewModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel
import javax.inject.Inject

class QuestionsFragment: BaseFragment(R.layout.fragment_questions) {

    private val binding: FragmentQuestionsBinding by viewBinding(FragmentQuestionsBinding::bind)

    @Inject
    lateinit var viewModel: QuestionsViewModel

    @Inject
    lateinit var questionViewPagerTransformer: QuestionViewPagerTransformer

    lateinit var questionsComponent: QuestionsComponent

    override fun inject() {
        questionsComponent = FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .questionsComponentFactory()
            .create(this)

        questionsComponent.inject(this)
    }

    override fun initView() {
    }

    override fun observeData() {
        viewModel.getQuestions()

        viewModel.currentTimeFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectTimerData(it)
        }
        viewModel.questionsDataFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectQuestionsData(it)
        }
    }

    private fun collectTimerData(time: Int) {
        with(binding) {
            swvClock.time = time
            val min = time / 60
            val sec = time % 60
            if (min > 0) tvTime.text = getString(R.string.clock_time_with_min, min, sec)
            else tvTime.text = getString(R.string.clock_time, sec)
        }
    }

    private fun collectQuestionsData(result: BaseQuestionsViewModel.QuestionDataResult?) {
        result?.let {
            when(it) {
                is BaseQuestionsViewModel.QuestionDataResult.Success ->
                    onGetQuestionsSuccess(it.getValue())
                is BaseQuestionsViewModel.QuestionDataResult.Failure ->
                    onGetQuestionsFail(it.getException())
            }
        }
    }

    private fun onGetQuestionsFail(ex: Throwable) {
        /*showErrorBottomSheetDialog(getString(R.string.get_questions_fail_msg),
            ex.message ?:
            getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg))*/
    }

    private fun onGetQuestionsSuccess(questions: List<QuestionDataUiModel>) {
        initViewPager(questions)
        binding.llTime.show()
    }

    private fun initViewPager(questions: List<QuestionDataUiModel>) {
        with(binding) {
            val adapter = QuestionsViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = lifecycle,
                questionList = questions,
                fragmentType = QuestionFragment::class)

            vpQuestions.adapter = adapter
            vpQuestions.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    tvQuestionNum.text = getString(
                        R.string.question_num, position + 1, questions.size
                    )
                    if (position == adapter.itemCount - 1) {
                        (childFragmentManager.findFragmentByTag(
                            "f" + adapter.getItemId(position)
                        ) as QuestionFragment).showEndButton()
                    }
                }
            })
            vpQuestions.setPageTransformer(questionViewPagerTransformer)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }
}