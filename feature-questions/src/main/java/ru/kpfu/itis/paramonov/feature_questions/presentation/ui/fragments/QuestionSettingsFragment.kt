package ru.kpfu.itis.paramonov.feature_questions.presentation.ui.fragments

import android.content.Context
import android.widget.ArrayAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionsSettingsBinding
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter.DifficultyArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.DifficultyItem
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
        initDifficultiesTextView()
        initCategoriesTextView()
        initGameModesTextView()
    }

    private fun initDifficultiesTextView() {
        val difficulties = getDifficultyList()
        val adapter = DifficultyArrayAdapter(requireContext(), difficulties, resourceManager)
        binding.tvDifficulties.setAdapter(adapter)
    }

    private fun initCategoriesTextView() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.tvCategories.setAdapter(adapter)
    }

    private fun initGameModesTextView() {}

    private fun getDifficultyList(): List<DifficultyItem> {
        return resources.getStringArray(R.array.difficulties)
            .asList()
            .map { difficulty ->
                val model = DifficultyUiModel.valueOf(difficulty.uppercase())
                DifficultyItem(model)
            }
    }

    override fun observeData() {
    }
}