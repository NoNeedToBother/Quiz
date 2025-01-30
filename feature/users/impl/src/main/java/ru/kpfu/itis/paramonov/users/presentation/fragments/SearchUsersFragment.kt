package ru.kpfu.itis.paramonov.users.presentation.fragments

import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.core.utils.collect
import ru.kpfu.itis.paramonov.core.utils.gone
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.core.utils.startPostponedTransition
import ru.kpfu.itis.paramonov.users.R
import ru.kpfu.itis.paramonov.users.databinding.FragmentSearchUsersBinding
import ru.kpfu.itis.paramonov.users.di.FeatureUsersComponent
import ru.kpfu.itis.paramonov.users.di.FeatureUsersDependencies
import ru.kpfu.itis.paramonov.users.presentation.adapter.UserAdapter
import ru.kpfu.itis.paramonov.users.presentation.adapter.diffutil.UserDiffUtilCallback
import ru.kpfu.itis.paramonov.users.presentation.viewmodel.SearchUsersViewModel
import java.lang.IllegalStateException
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.timerTask

class SearchUsersFragment: BaseFragment(R.layout.fragment_search_users) {

    private val binding by viewBinding(FragmentSearchUsersBinding::bind)

    @Inject
    lateinit var viewModel: SearchUsersViewModel

    private var adapter: UserAdapter? = null

    private var timer: Timer = Timer()

    private var lastTime = DEFAULT_LAST_TIME_VALUE

    override fun inject() {
        FeatureUtils.getFeature<FeatureUsersComponent>(this, FeatureUsersDependencies::class.java)
            .searchUsersComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        postponeEnterTransition()
        initSearchView()
        initRecyclerView()
        startPostponedTransition()
    }

    private fun initSearchView() {
        binding.svUsername.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val currentTime = System.currentTimeMillis()
                    if (lastTime != DEFAULT_LAST_TIME_VALUE &&
                        currentTime - lastTime < MIN_TIME_BETWEEN_REGISTERING) {
                        try {
                            timer.cancel()
                            timer = Timer()
                        } catch (_: IllegalStateException) {}
                    }
                    lastTime = currentTime
                    timer.schedule(timerTask {
                        viewModel.searchUsers(it, MAX_USER_AMOUNT, null)
                    }, MIN_TIME_BETWEEN_REGISTERING)
                }
                return true
            }

        })
    }

    private fun initRecyclerView() {
        with(binding.rvUsers) {
            val userAdapter = UserAdapter(
                diffUtilCallback = UserDiffUtilCallback(),
                onUserClicked = ::onUserClicked
            )
            adapter = userAdapter
            this@SearchUsersFragment.adapter = userAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            this.layoutManager = layoutManager
            addLastElementRecyclerViewListener()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun addLastElementRecyclerViewListener() {
        with(binding.rvUsers) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (layoutManager.itemCount > 0) {
                        val lastItemPos = layoutManager.findLastVisibleItemPosition()
                        val lastItem = (adapter as ListAdapter<UserModel, *>).currentList[lastItemPos]
                        if (lastItemPos >= layoutManager.itemCount - 1) {
                            viewModel.loadNextUsers(lastItem.username, MAX_USER_AMOUNT,  lastItem.id)
                        }
                    }
                }
            })
        }
    }

    private fun onUserClicked(user: UserModel, sharedView: ImageView) {
        viewModel.navigateToUser(user.id, sharedView)
    }

    override fun observeData() {
        viewModel.searchUsersFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectUsersData(it)
        }
        viewModel.searchUsersPagingFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectUsersPagingData(it)
        }
    }

    private fun collectUsersPagingData(result: SearchUsersViewModel.SearchUsersResult?) {
        result?.let {
            when(result) {
                is SearchUsersViewModel.SearchUsersResult.Success ->
                    onSearchUsersPagingSuccess(result.getValue())
                is SearchUsersViewModel.SearchUsersResult.Failure -> {
                    onSearchUsersFail(result.getException())
                }
            }
        }
    }

    private fun onSearchUsersPagingSuccess(users: List<UserModel>) {
        val adapterList = adapter?.currentList ?: mutableListOf()
        var newList: MutableList<UserModel> = ArrayList(adapterList)
        newList.addAll(users)
        newList = ArrayList(newList.distinctBy {
            it.id
        })
        adapter?.submitList(newList)
    }

    private fun collectUsersData(result: SearchUsersViewModel.SearchUsersResult?) {
        result?.let {
            when(result) {
                is SearchUsersViewModel.SearchUsersResult.Success ->
                    onSearchUsersSuccess(result.getValue())
                is SearchUsersViewModel.SearchUsersResult.Failure -> {
                    onSearchUsersFail(result.getException())
                }
            }
        }
    }

    private fun onSearchUsersSuccess(users: List<UserModel>) {
        adapter?.submitList(users)
        if (users.isEmpty()) binding.layoutEmptyResults.root.show()
        else binding.layoutEmptyResults.root.gone()
    }

    private fun onSearchUsersFail(ex: Throwable) {
        showErrorBottomSheetDialog(
            getString(R.string.search_users_fail),
            ex.message ?: getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
        )
    }

    companion object {
        private const val MAX_USER_AMOUNT = 15

        private const val MIN_TIME_BETWEEN_REGISTERING = 400L

        private const val DEFAULT_LAST_TIME_VALUE = -1L
    }
}