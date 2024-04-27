package ru.kpfu.itis.paramonov.feature_questions.presentation.questions.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentTrainingQuestionsBinding
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.adapter.QuestionsViewPagerAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.di.TrainingQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.model.QuestionDataUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.utils.QuestionViewPagerTransformer
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.BaseQuestionsViewModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.questions.viewmodel.TrainingQuestionsViewModel
import javax.inject.Inject

class TrainingQuestionsFragment: BaseFragment(R.layout.fragment_training_questions) {

    private val binding: FragmentTrainingQuestionsBinding by viewBinding(FragmentTrainingQuestionsBinding::bind)

    @Inject
    lateinit var viewModel: TrainingQuestionsViewModel

    @Inject
    lateinit var questionViewPagerTransformer: QuestionViewPagerTransformer

    lateinit var trainingQuestionsComponent: TrainingQuestionsComponent

    override fun inject() {
        trainingQuestionsComponent = FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .trainingQuestionsComponentFactory()
            .create(this)

        trainingQuestionsComponent.inject(this)
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
            }
        }
    }

    private suspend fun collectQuestionsData() {
        viewModel.questionsDataFlow.collect { result ->
            result?.let {
                when(it) {
                    is BaseQuestionsViewModel.QuestionDataResult.Success ->
                        onGetQuestionsSuccess(it.getValue())
                    is BaseQuestionsViewModel.QuestionDataResult.Failure ->
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

    private fun onGetQuestionsSuccess(questions: List<QuestionDataUiModel>) {
        initViewPager(questions)
    }

    private fun initViewPager(questions: List<QuestionDataUiModel>) {
        with(binding) {
            val adapter = QuestionsViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = lifecycle,
                questionList = questions,
                fragmentType = TrainingQuestionFragment::class)

            vpQuestions.adapter = adapter
            vpQuestions.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    tvQuestionNum.text = getString(
                        R.string.question_num, position + 1, questions.size
                    )
                }
            })
            vpQuestions.setPageTransformer(questionViewPagerTransformer)
        }
    }
}