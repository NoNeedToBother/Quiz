package ru.kpfu.itis.paramonov.feature_questions.presentation.settings.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import ru.kpfu.itis.paramonov.core.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.feature_questions.R
import ru.kpfu.itis.paramonov.feature_questions.databinding.FragmentQuestionSettingsViewPagerBinding
import ru.kpfu.itis.paramonov.feature_questions.presentation.settings.adapter.QuestionSettingsViewPagerAdapter

class QuestionSettingsViewPagerFragment: BaseFragment(R.layout.fragment_question_settings_view_pager) {

    private val binding: FragmentQuestionSettingsViewPagerBinding by viewBinding(FragmentQuestionSettingsViewPagerBinding::bind)

    override fun inject() {
    }

    override fun initView() {
        initViewPager()
    }

    private fun initViewPager() {
        with(binding) {
            val adapter = QuestionSettingsViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = lifecycle
            )
            vpSettings.adapter = adapter
            vpSettings.isUserInputEnabled = false
            vpSettings.setCurrentItem(0, false)
            val tabTitles: List<String> =
                listOf(getString(R.string.standard_settings), getString(R.string.training_settings))
            TabLayoutMediator(tlLabels, vpSettings) {tab, position ->
                tab.text = tabTitles[position]
            }.attach()
        }
    }

    override fun observeData() {
    }
}