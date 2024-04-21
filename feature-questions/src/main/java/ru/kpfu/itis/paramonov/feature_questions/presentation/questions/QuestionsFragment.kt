package ru.kpfu.itis.paramonov.feature_questions.presentation.questions

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import javax.inject.Inject

class QuestionsFragment: BaseFragment(R.layout.fragment_questions) {

    @Inject
    lateinit var viewModel: QuestionsViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .questionsComponentFactory()
            .create(this)
            .inject(this)
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
                    is QuestionsViewModel.QuestionDataResult.Success ->
                        Log.i("DATA", it.getValue().questions.toString())
                    is QuestionsViewModel.QuestionDataResult.Failure ->
                        showErrorBottomSheetDialog("FAIL",
                            it.getException().message ?:
                            getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg))
                }
            }
        }
    }
}