package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments.LeaderboardFragment

class LeaderboardsViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = TAB_LEADERBOARDS_COUNT

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            GLOBAL_LEADERBOARD_POS -> LeaderboardFragment.newInstance(LeaderboardFragment.LeaderboardType.GLOBAL)
            FRIENDS_LEADERBOARD_POS -> LeaderboardFragment.newInstance(LeaderboardFragment.LeaderboardType.FRIENDS)
            else -> throw RuntimeException("Unsupported fragment")
        }
    }

    companion object {
        private const val TAB_LEADERBOARDS_COUNT = 2
        private const val GLOBAL_LEADERBOARD_POS = 0
        private const val FRIENDS_LEADERBOARD_POS = 1
    }
}