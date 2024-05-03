package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments

import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FeatureLeaderboardsComponent
import ru.kpfu.itis.paramonov.feature_leaderboards.di.FeatureLeaderboardsDependencies
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.GlobalLeaderboardViewModel
import javax.inject.Inject

class GlobalLeaderboardFragment: BaseFragment(R.layout.fragment_global_leaderboard) {

    @Inject
    lateinit var viewModel: GlobalLeaderboardViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureLeaderboardsComponent>(this, FeatureLeaderboardsDependencies::class.java)
            .globalLeaderboardComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
    }

    override fun observeData() {
    }
}