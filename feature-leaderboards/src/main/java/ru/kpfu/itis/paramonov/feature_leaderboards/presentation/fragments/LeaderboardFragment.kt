package ru.kpfu.itis.paramonov.feature_leaderboards.presentation.fragments

import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.resources.ResourceManager
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.utils.collect
import ru.kpfu.itis.paramonov.feature_leaderboards.R
import ru.kpfu.itis.paramonov.feature_leaderboards.databinding.FragmentLeaderboardBinding
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.LeaderboardResultPagingAdapter
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.adapter.diffutil.ResultDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.feature_leaderboards.presentation.viewmodel.LeaderboardViewModel
import javax.inject.Inject

class LeaderboardFragment: BaseFragment(R.layout.fragment_leaderboard) {

    private val binding: FragmentLeaderboardBinding by viewBinding(FragmentLeaderboardBinding::bind)

    private var adapter: LeaderboardResultPagingAdapter? = null

    @Inject
    lateinit var viewModel: LeaderboardViewModel

    @Inject
    lateinit var resourceManager: ResourceManager

    private val type: LeaderboardType get() {
        return LeaderboardType.valueOf(requireArguments().getString(TYPE_KEY)
            ?: LeaderboardType.GLOBAL.name
        )
    }

    override fun inject() {
        (parentFragment as LeaderboardsViewPagerFragment).leaderboardsComponent.inject(this)
    }

    override fun initView() {
        initRecyclerView()
    }

    private fun onResultClicked(id: String) {
        viewModel.navigateToUser(id)
    }

    private fun initRecyclerView() {
        with(binding) {
            val adapter = LeaderboardResultPagingAdapter(
                ResultDiffUtilCallback(), resourceManager, ::onResultClicked)
            this@LeaderboardFragment.adapter = adapter
            rvResults.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext())
            rvResults.layoutManager = layoutManager
        }
    }

    /*private fun addLastElementRecyclerViewListener() {
        with(binding.rvResults) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (layoutManager.itemCount > 0) {
                        val lastItem = layoutManager.findLastVisibleItemPosition()
                        if (lastItem >= layoutManager.itemCount - 1
                           && currentResultCount <= LEADERBOARD_ABSOLUTE_MAX
                        ) {
                            (adapter as LeaderboardResultPagingAdapter).peek(lastItem)?.let {  last ->
                                viewModel.loadNextResults(type, last.score)
                            }
                        }
                    }
                }
            })
        }
    }*/

    override fun observeData() {
        viewModel.getResultsOnStart(type)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                viewModel.getDataFlow(type)?.collectLatest {
                    adapter?.submitData(it)
                }
            }
        }

        viewModel.clearLeaderboardFlow
            .collect(lifecycleOwner = viewLifecycleOwner) {
                collectClearingLeaderboard(it)
            }
        }

    private fun collectClearingLeaderboard(cleared: Boolean) {
        if (cleared) {
            viewModel.onLeaderboardCleared()
            viewModel.getResultsAfterCleared(type)
        }
    }

    private fun collectResultData(result: LeaderboardViewModel.LeaderboardDataResult?) {
        result?.let {
            when (result) {
                is LeaderboardViewModel.LeaderboardDataResult.Success -> addResults(result.getValue())
                is LeaderboardViewModel.LeaderboardDataResult.Failure -> onGetResultsFail(result.getException())
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
        //binding.layoutProceeding.root.gone()
        /*adapter?.
        val adapterList = adapter?.currentList ?: mutableListOf()
        var newList: MutableList<ResultUiModel> = ArrayList(adapterList)
        newList.addAll(results)
        newList = ArrayList(newList.distinctBy {
            it.score
        })
        currentResultCount = newList.size
        adapter?.submitList(newList)*/
    }

    enum class LeaderboardType {
        GLOBAL, FRIENDS
    }

    companion object {
        private const val TYPE_KEY = "type"

        fun newInstance(type: LeaderboardType): LeaderboardFragment = LeaderboardFragment().apply {
            arguments = bundleOf(TYPE_KEY to type.name)
        }

        private const val LEADERBOARD_ABSOLUTE_MAX = 500
    }
}