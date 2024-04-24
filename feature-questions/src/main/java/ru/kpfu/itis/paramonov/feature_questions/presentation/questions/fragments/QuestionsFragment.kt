package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionsBinding
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter.QuestionsViewPagerAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di.QuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.QuestionsViewModel
import javax.inject.Inject

class QuestionsFragment: BaseFragment(R.layout.fragment_questions) {

    private val binding: FragmentQuestionsBinding by viewBinding(FragmentQuestionsBinding::bind)

    @Inject
    lateinit var viewModel: QuestionsViewModel

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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectQuestionsData()
                }
                launch {
                    collectTimerData()
                }
            }
        }
    }

    private suspend fun collectTimerData() {
        viewModel.currentTimeFlow.collect { time ->
            with(binding) {
                swvClock.time = time
                val min = time / 60
                val sec = time % 60
                if (min > 0) tvTime.text = getString(R.string.clock_time_with_min, min, sec)
                else tvTime.text = getString(R.string.clock_time, sec)
            }
        }
    }

    private suspend fun collectQuestionsData() {
        viewModel.questionsDataFlow.collect { result ->
            result?.let {
                when(it) {
                    is QuestionsViewModel.QuestionDataResult.Success ->
                        onGetQuestionsSuccess(it.getValue())
                    is QuestionsViewModel.QuestionDataResult.Failure ->
                        onGetQuestionsFail(it.getException())
                }
            }
        }
    }

    private fun onGetQuestionsFail(ex: Throwable) {
        showErrorBottomSheetDialog(getString(R.string.get_questions_fail_msg),
            ex.message ?:
            getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg))
    }

    private fun onGetQuestionsSuccess(questionUiModel: QuestionUiModel) {
        initViewPager(questionUiModel)
        binding.llTime.show()
    }

    private fun initViewPager(questionUiModel: QuestionUiModel) {
        with(binding) {
            val adapter = QuestionsViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = lifecycle,
                questionList = questionUiModel.questions)

            vpQuestions.adapter = adapter
            vpQuestions.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    tvQuestionNum.text = getString(
                        R.string.question_num, position + 1, questionUiModel.questions.size
                    )
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopTimer()
    }
}