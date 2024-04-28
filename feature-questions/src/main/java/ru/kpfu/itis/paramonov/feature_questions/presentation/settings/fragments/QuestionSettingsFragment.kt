package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.fragments

import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionsSettingsBinding
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.feature_questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.viewmodel.QuestionSettingsViewModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.GameModeUiModel
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.adapter.CategoryArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.adapter.DifficultyArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.adapter.GameModeArrayAdapter
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items.CategoryItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items.DifficultyItem
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.model.items.GameModeItem
import javax.inject.Inject

class QuestionSettingsFragment: BaseFragment(R.layout.fragment_questions_settings) {

    private val binding: FragmentQuestionsSettingsBinding by viewBinding(FragmentQuestionsSettingsBinding::bind)

    @Inject
    lateinit var resourceManager: ResourceManager

    @Inject
    lateinit var viewModel: QuestionSettingsViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .questionSettingsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        initDifficultiesTextView()
        initCategoriesTextView()
        initGameModesTextView()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnSave.setOnClickListener {
                viewModel.saveQuestionSettings(
                    difficulty = tvDifficulties.text.toString(),
                    category = tvCategories.text.toString(),
                    gameMode = tvGameModes.text.toString()
                )
            }
        }
    }

    private fun initDifficultiesTextView() {
        val difficulties = getDifficultyList()
        val adapter = DifficultyArrayAdapter(requireContext(), difficulties, resourceManager)
        binding.tvDifficulties.apply {
            setAdapter(adapter)
            setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(
                resources, R.drawable.popup_bg, requireContext().theme))
        }
    }

    private fun initCategoriesTextView() {
        val categories = getCategoryList()
        val adapter = CategoryArrayAdapter(requireContext(), categories)
        binding.tvCategories.apply {
            setAdapter(adapter)
            setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(
                resources, R.drawable.popup_bg, requireContext().theme))
        }
    }

    private fun initGameModesTextView() {
        val gameModes = getGameModeList()
        val adapter = GameModeArrayAdapter(requireContext(), gameModes)
        binding.tvGameModes.apply {
            setAdapter(adapter)
            setDropDownBackgroundDrawable(ResourcesCompat.getDrawable(
                resources, R.drawable.popup_bg, requireContext().theme))
        }
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
        viewModel.getQuestionSettings()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectSettingsData()
                }
            }
        }
    }

    private suspend fun collectSettingsData() {
        viewModel.settingsDataFlow.collect {
            it?.let { model ->
                with(binding) {
                    tvDifficulties.setText(model.difficulty.name.normalizeEnumName(), false)
                    tvGameModes.setText(model.gameMode.name.normalizeEnumName(), false)
                    tvCategories.setText(model.category.name.normalizeEnumName(), false)
                    btnSave.isEnabled = true
                }
            }
        }
    }
}