package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments

import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.FragmentLeaderboardsViewPagerBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FeatureLeaderboardsComponent
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FeatureLeaderboardsDependencies
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.LeaderboardsViewPagerAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.di.LeaderboardsComponent

class LeaderboardsViewPagerFragment: BaseFragment(R.layout.fragment_leaderboards_view_pager) {

    private val binding: FragmentLeaderboardsViewPagerBinding by viewBinding(FragmentLeaderboardsViewPagerBinding::bind)

    lateinit var leaderboardsComponent: LeaderboardsComponent

    override fun inject() {
        leaderboardsComponent =
            FeatureUtils.getFeature<FeatureLeaderboardsComponent>(this, FeatureLeaderboardsDependencies::class.java)
            .leaderboardComponentFactory()
            .create(this)
        leaderboardsComponent.inject(this)
    }

    override fun initView() {
        initViewPager()
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

    override fun observeData() {}
}