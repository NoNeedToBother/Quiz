package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.fragments

import android.content.Context
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionsSettingsBinding
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter.DifficultySpinnerAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.DifficultySpinnerItem
import javax.inject.Inject

class QuestionSettingsFragment: BaseFragment(R.layout.fragment_questions_settings) {

    private val binding: FragmentQuestionsSettingsBinding by viewBinding(FragmentQuestionsSettingsBinding::bind)

    @Inject
    lateinit var resourceManager: ResourceManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .inject(this)

    }
    override fun initView() {
        initDifficultySpinner()
    }

    private fun initDifficultySpinner() {
        val difficulties = getDifficultyList()
        val adapter = DifficultySpinnerAdapter(requireContext(), difficulties, resourceManager)
        binding.spinnerDifficulty.adapter = adapter
    }

    private fun getDifficultyList(): List<DifficultySpinnerItem> {
        return resources.getStringArray(R.array.difficulties)
            .asList()
            .map { difficulty ->
                val model = DifficultyUiModel.valueOf(difficulty.uppercase())
                DifficultySpinnerItem(model)
            }
    }

    override fun observeData() {
    }
}