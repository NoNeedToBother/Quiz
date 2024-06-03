package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments

import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.utils.collect
import ru.kpfu.itis.paramonov.common_android.utils.gone
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.common_android.utils.startPostponedTransition
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.FragmentLeaderboardBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.LeaderboardResultAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.diffutil.ResultDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.LeaderboardsViewModel
import javax.inject.Inject

class LeaderboardFragment: BaseFragment(R.layout.fragment_leaderboard) {

    private val binding: FragmentLeaderboardBinding by viewBinding(FragmentLeaderboardBinding::bind)

    private var adapter: LeaderboardResultAdapter? = null

    @Inject
    lateinit var viewModel: LeaderboardsViewModel

    @Inject
    lateinit var resourceManager: ResourceManager

    private var currentResultCount = 0

    private val type: LeaderboardType get() {
        return LeaderboardType.valueOf(requireArguments().getString(TYPE_KEY)
            ?: LeaderboardType.GLOBAL.name
        )
    }

    override fun inject() {
        (parentFragment as LeaderboardsViewPagerFragment).leaderboardsComponent.inject(this)
    }

    override fun initView() {
        postponeEnterTransition()
        initRecyclerView()
        startPostponedTransition()
    }

    private fun onResultClicked(id: String, sharedView: ImageView) {
        viewModel.navigateToUser(id, sharedView)
    }

    private fun initRecyclerView() {
        with(binding) {
            val adapter = LeaderboardResultAdapter(
                ResultDiffUtilCallback(), resourceManager, ::onResultClicked)
            this@LeaderboardFragment.adapter = adapter
            rvResults.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext())
            rvResults.layoutManager = layoutManager
            addLastElementRecyclerViewListener()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun addLastElementRecyclerViewListener() {
        with(binding.rvResults) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (layoutManager.itemCount > 0) {
                        val lastItem = layoutManager.findLastVisibleItemPosition()
                        if (lastItem >= layoutManager.itemCount - 1
                            && currentResultCount <= LEADERBOARD_ABSOLUTE_MAX
                        ) {
                            viewModel.loadNextResults(
                                type, LEADERBOARD_MAX_AT_ONCE,
                                (adapter as ListAdapter<ResultUiModel, *>).currentList[lastItem].score
                            )
                        }
                    }
                }
            })
        }
    }

    override fun observeData() {
        viewModel.getResultsOnStart(type, LEADERBOARD_MAX_AT_ONCE)

        viewModel.getDataFlow(type)
            .collect(lifecycleOwner = viewLifecycleOwner) {
                collectResultData(it)
            }

        viewModel.clearLeaderboardFlow
            .collect(lifecycleOwner = viewLifecycleOwner) {
                collectClearingLeaderboard(it)
            }
    }

    private fun collectClearingLeaderboard(cleared: Boolean) {
        if (cleared) {
            adapter?.submitList(null)
            viewModel.onLeaderboardCleared()
            viewModel.getResultsAfterCleared(type, LEADERBOARD_MAX_AT_ONCE)
        }
    }

    private fun collectResultData(result: LeaderboardsViewModel.LeaderboardDataResult?) {
        result?.let {
            when (result) {
                is LeaderboardsViewModel.LeaderboardDataResult.Success -> addResults(result.getValue())
                is LeaderboardsViewModel.LeaderboardDataResult.Failure -> onGetResultsFail(result.getException())
            }
        }
    }

    private fun onGetResultsFail(ex: Throwable) {
        showErrorBottomSheetDialog(
            getString(R.string.get_results_fail),
            ex.message ?: getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg)
        )
    }

    private fun addResults(results: List<ResultUiModel>) {
        binding.layoutProceeding.root.gone()
        val adapterList = adapter?.currentList ?: mutableListOf()
        var newList: MutableList<ResultUiModel> = ArrayList(adapterList)
        newList.addAll(results)
        newList = ArrayList(newList.distinctBy {
            it.score
        })
        currentResultCount = newList.size
        if (newList.isEmpty()) binding.layoutEmptyResults.root.show()
        else binding.layoutEmptyResults.root.gone()
        adapter?.submitList(newList)
    }

    enum class LeaderboardType {
        GLOBAL, FRIENDS
    }

    companion object {
        private const val TYPE_KEY = "type"

        fun newInstance(type: LeaderboardType): LeaderboardFragment = LeaderboardFragment().apply {
            arguments = bundleOf(TYPE_KEY to type.name)
        }

        private const val LEADERBOARD_MAX_AT_ONCE = 10

        private const val LEADERBOARD_ABSOLUTE_MAX = 500
    }
}