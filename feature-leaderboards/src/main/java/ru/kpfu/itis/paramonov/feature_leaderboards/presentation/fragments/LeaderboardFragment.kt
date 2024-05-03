package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments

import androidx.core.os.bundleOf
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.FragmentLeaderboardBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.LeaderboardResultAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.diffutil.ResultDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.LeaderboardsViewModel
import javax.inject.Inject

class LeaderboardFragment: BaseFragment(R.layout.fragment_leaderboard) {

    private val binding: FragmentLeaderboardBinding by viewBinding(FragmentLeaderboardBinding::bind)

    private var adapter: LeaderboardResultAdapter? = null

    @Inject
    lateinit var viewModel: LeaderboardsViewModel

    @Inject
    lateinit var resourceManager: ResourceManager

    override fun inject() {
        (parentFragment as LeaderboardsViewPagerFragment).leaderboardsComponent.inject(this)
    }

    override fun initView() {
        initRecyclerView()
    }

    private fun onResultClicked(id: String) {

    }

    private fun initRecyclerView() {
        with(binding) {
            val adapter = LeaderboardResultAdapter(
                ResultDiffUtilCallback(), resourceManager, ::onResultClicked)
            this@LeaderboardFragment.adapter = adapter
            rvResults.adapter = adapter
        }
    }

    override fun observeData() {
        val type = LeaderboardType.valueOf(requireArguments().getString(TYPE_KEY)
            ?: LeaderboardType.GLOBAL.name
        )
        viewModel.getResultsOnStart(type)
    }

    enum class LeaderboardType {
        GLOBAL, FRIENDS
    }

    companion object {
        private const val TYPE_KEY = "type"

        fun newInstance(type: LeaderboardType): LeaderboardFragment = LeaderboardFragment().apply {
            arguments = bundleOf(TYPE_KEY to type.name)
        }
    }
}