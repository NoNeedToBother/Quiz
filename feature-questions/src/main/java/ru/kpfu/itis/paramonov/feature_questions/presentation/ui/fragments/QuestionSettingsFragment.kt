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
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.model.settings.GameModeUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter.CategoryArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter.DifficultyArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.adapter.GameModeArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.CategoryItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.DifficultyItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.ui.model.GameModeItem
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
        val categories = getCategoryList()
        val adapter = CategoryArrayAdapter(requireContext(), categories)
        binding.tvCategories.setAdapter(adapter)
    }

    private fun initGameModesTextView() {
        val gameModes = getGameModeList()
        val adapter = GameModeArrayAdapter(requireContext(), gameModes)
        binding.tvGameModes.setAdapter(adapter)
    }

    private fun getDifficultyList(): List<DifficultyItem> {
        return getItemList(
            arrayId = R.array.difficulties,
            modelMapper = ::getDifficultyModel,
            itemMapper = ::getDifficultyItem
        )
    }

    private fun getCategoryList(): List<CategoryItem> {
        return getItemList(
            arrayId = R.array.categories,
            modelMapper = ::getCategoryModel,
            itemMapper = ::getCategoryItem,
        )
    }

    private fun getGameModeList(): List<GameModeItem> {
        return getItemList(
            arrayId = R.array.game_modes,
            modelMapper = ::getGameModeModel,
            itemMapper = ::getGameModeItem,
        )
    }

    private fun getCategoryModel(str: String): CategoryUiModel = CategoryUiModel.valueOf(str.uppercase())
    private fun getCategoryItem(model: CategoryUiModel): CategoryItem = CategoryItem(model)

    private fun getDifficultyModel(str: String): DifficultyUiModel = DifficultyUiModel.valueOf(str.uppercase())
    private fun getDifficultyItem(model: DifficultyUiModel): DifficultyItem = DifficultyItem(model)

    private fun getGameModeModel(str: String): GameModeUiModel = GameModeUiModel.valueOf(str.uppercase())
    private fun getGameModeItem(model: GameModeUiModel): GameModeItem = GameModeItem(model)

    private fun <M, I> getItemList(arrayId: Int, modelMapper: (String) -> M, itemMapper: (M) -> I): List<I> {
        return resources.getStringArray(arrayId)
            .asList()
            .map {  str ->
                val model = modelMapper.invoke(str)
                itemMapper.invoke(model)
            }
    }

    override fun observeData() {
    }
}