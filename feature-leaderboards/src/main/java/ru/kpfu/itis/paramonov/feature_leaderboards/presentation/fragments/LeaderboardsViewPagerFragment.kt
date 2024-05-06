package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.common.utils.toEnumName
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.FragmentLeaderboardsViewPagerBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FeatureLeaderboardsComponent
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FeatureLeaderboardsDependencies
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.LeaderboardsViewPagerAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.settings.CategoryArrayAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.settings.DifficultyArrayAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.settings.GameModeArrayAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di.LeaderboardsComponent
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.CategoryUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.DifficultyUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.GameModeUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.items.CategoryItem
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.items.DifficultyItem
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.items.GameModeItem
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.LeaderboardsViewModel
import javax.inject.Inject

class LeaderboardsViewPagerFragment: BaseFragment(R.layout.fragment_leaderboards_view_pager) {

    private val binding: FragmentLeaderboardsViewPagerBinding by viewBinding(FragmentLeaderboardsViewPagerBinding::bind)

    lateinit var leaderboardsComponent: LeaderboardsComponent

    @Inject
    lateinit var viewModel: LeaderboardsViewModel

    override fun inject() {
        leaderboardsComponent =
            FeatureUtils.getFeature<FeatureLeaderboardsComponent>(this, FeatureLeaderboardsDependencies::class.java)
            .leaderboardComponentFactory()
            .create(this)
        leaderboardsComponent.inject(this)
    }

    override fun initView() {
        initViewPager()
        initSettingsBottomSheet()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            val bottomSheet = clSettings.findViewById<ConstraintLayout>(R.id.bottom_sheet_container)
            val btnSave = bottomSheet.findViewById<MaterialButton>(R.id.btn_save)
            btnSave.setOnClickListener {
                saveSettings()
                viewModel.clearLeaderboards()
            }
        }
    }

    private fun saveSettings() {
        with(binding) {
            val bottomSheet = clSettings.findViewById<ConstraintLayout>(R.id.bottom_sheet_container)
            val tvCategories =
                bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_categories)
            val tvDifficulties =
                bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_difficulties)
            val tvGameModes =
                bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_game_modes)
            val category = if (tvCategories.text.isEmpty() || tvCategories.text.toString() == "Any") null
            else CategoryUiModel.valueOf(tvCategories.text.toString().toEnumName())
            val difficulty = if (tvDifficulties.text.isEmpty() || tvDifficulties.text.toString() == "Any") null
            else DifficultyUiModel.valueOf(tvDifficulties.text.toString().toEnumName())
            val gameMode = GameModeUiModel.valueOf(tvGameModes.text.toString().toEnumName())
            viewModel.saveSettings(category = category, difficulty = difficulty, gameMode = gameMode)
        }
    }

    private fun initSettingsBottomSheet() {
        with(binding) {
            val bottomSheet = clSettings.findViewById<ConstraintLayout>(R.id.bottom_sheet_container)
            val tvCategories = bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_categories)
            val tvDifficulties = bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_difficulties)
            val tvGameModes = bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_game_modes)
            initAutoCompleteTextView(tvCategories, CategoryArrayAdapter(requireContext(), getCategoryList()))
            initAutoCompleteTextView(tvDifficulties, DifficultyArrayAdapter(requireContext(), getDifficultyList()))
            initAutoCompleteTextView(tvGameModes, GameModeArrayAdapter(requireContext(), getGameModeList()))
        }
    }

    private fun <T> initAutoCompleteTextView(tv: AutoCompleteTextView, adapter: ArrayAdapter<T>) {
        tv.apply {
            setAdapter(adapter)
            setDropDownBackgroundDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.popup_bg, requireContext().theme))
        }
    }

    private fun getDifficultyList(): List<DifficultyItem> {
        return getItemList(
            arrayId = R.array.leaderboard_difficulties,
            modelMapper = ::getDifficultyModel,
            itemMapper = ::getDifficultyItem
        )
    }

    private fun getCategoryList(): List<CategoryItem> {
        return getItemList(
            arrayId = R.array.leaderboard_categories,
            modelMapper = ::getCategoryModel,
            itemMapper = ::getCategoryItem,
        )
    }

    private fun getGameModeList(): List<GameModeItem> {
        return getItemList(
            arrayId = R.array.leaderbaord_game_modes,
            modelMapper = ::getGameModeModel,
            itemMapper = ::getGameModeItem,
        )
    }

    private fun getCategoryModel(str: String): CategoryUiModel? {
        return try {
            CategoryUiModel.valueOf(str.uppercase())
        } catch (_: Throwable) {
            null
        }
    }
    private fun getCategoryItem(model: CategoryUiModel?): CategoryItem = CategoryItem(model)

    private fun getDifficultyModel(str: String): DifficultyUiModel? {
        return try {
            DifficultyUiModel.valueOf(str.uppercase())
        } catch (_: Throwable) {
            null
        }
    }
    private fun getDifficultyItem(model: DifficultyUiModel?): DifficultyItem = DifficultyItem(model)

    private fun getGameModeModel(str: String): GameModeUiModel = GameModeUiModel.valueOf(str.uppercase())
    private fun getGameModeItem(model: GameModeUiModel?): GameModeItem = GameModeItem(model!!)

    private fun <M, I> getItemList(arrayId: Int, modelMapper: (String) -> M?, itemMapper: (M?) -> I): List<I> {
        return resources.getStringArray(arrayId)
            .asList()
            .map {  str ->
                val model = modelMapper.invoke(str)
                itemMapper.invoke(model)
            }
    }

    private fun initViewPager() {
        with(binding) {
            val adapter = LeaderboardsViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = lifecycle
            )
            vpLeaderboards.adapter = adapter
            vpLeaderboards.isUserInputEnabled = false
            vpLeaderboards.setCurrentItem(0, false)
            val tabTitles: List<String> =
                listOf(getString(R.string.global_leaderboard), getString(R.string.friend_leaderboard))
            TabLayoutMediator(tlLabels, vpLeaderboards) {tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                collectSettingsData()
            }
        }
    }

    private suspend fun collectSettingsData() {
        viewModel.settingsDataFlow.collect {
            it?.let {  model ->
                with(binding) {
                    val bottomSheet =
                        clSettings.findViewById<ConstraintLayout>(R.id.bottom_sheet_container)
                    val tvCategories =
                        bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_categories)
                    val tvDifficulties =
                        bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_difficulties)
                    val tvGameModes =
                        bottomSheet.findViewById<MaterialAutoCompleteTextView>(R.id.tv_game_modes)
                    tvCategories.setText(model.category?.name?.normalizeEnumName() ?: "Any", false)
                    tvDifficulties.setText(model.difficulty?.name?.normalizeEnumName() ?: "Any", false)
                    tvGameModes.setText(model.gameMode.name.normalizeEnumName(), false)
                }
            }
        }
    }

}