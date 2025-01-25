package ru.kpfu.itis.paramonov.feature_users.presentation.fragments

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.core.utils.collect
import ru.kpfu.itis.paramonov.core.utils.gone
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.feature_users.R
import ru.kpfu.itis.paramonov.feature_users.databinding.FragmentFriendsBinding
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersComponent
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersDependencies
import ru.kpfu.itis.paramonov.feature_users.presentation.adapter.UserAdapter
import ru.kpfu.itis.paramonov.feature_users.presentation.adapter.diffutil.UserDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.FriendsViewModel
import javax.inject.Inject

class FriendsFragment: BaseFragment(R.layout.fragment_friends) {

    private val binding: FragmentFriendsBinding by viewBinding(FragmentFriendsBinding::bind)

    @Inject
    lateinit var viewModel: FriendsViewModel

    private var adapter: UserAdapter? = null

    override fun inject() {
        FeatureUtils.getFeature<FeatureUsersComponent>(this, FeatureUsersDependencies::class.java)
            .friendsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding.rvFriends) {
            val userAdapter = UserAdapter(
                diffUtilCallback = UserDiffUtilCallback(),
                onUserClicked = ::onUserClicked
            )
            adapter = userAdapter
            this@FriendsFragment.adapter = userAdapter
            val layoutManager = LinearLayoutManager(requireContext())
            this.layoutManager = layoutManager
            addLastElementRecyclerViewListener()
        }
    }

    private fun addLastElementRecyclerViewListener() {
        with(binding.rvFriends) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    if (layoutManager.itemCount > 0) {
                        val lastItemPos = layoutManager.findLastVisibleItemPosition()
                        if (lastItemPos >= layoutManager.itemCount - 1) {
                            viewModel.loadNextFriends(layoutManager.itemCount, MAX_USER_AMOUNT)
                        }
                    }
                }
            })
        }
    }

    private fun onUserClicked(user: UserModel, sharedView: View) {
        viewModel.navigateToUser(user.id, sharedView)
    }

    override fun observeData() {
        viewModel.getFriends(MAX_USER_AMOUNT)

        viewModel.friendsFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectFriendsData(it)
        }
        viewModel.friendsPagingFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectFriendsPagingData(it)
        }
    }

    private fun collectFriendsPagingData(result: FriendsViewModel.GetFriendsResult?) {
        result?.let {
            when(result) {
                is FriendsViewModel.GetFriendsResult.Success ->
                    onGetFriendsPagingSuccess(result.getValue())
                is FriendsViewModel.GetFriendsResult.Failure -> {
                    onGetFriendsFail(result.getException())
                }
            }
        }
    }

    private fun onGetFriendsPagingSuccess(users: List<UserModel>) {
        val adapterList = adapter?.currentList ?: mutableListOf()
        var newList: MutableList<UserModel> = ArrayList(adapterList)
        newList.addAll(users)
        newList = ArrayList(newList.distinctBy {
            it.id
        })
        adapter?.submitList(newList)
    }

    private fun collectFriendsData(result: FriendsViewModel.GetFriendsResult?) {
        result?.let {
            when(result) {
                is FriendsViewModel.GetFriendsResult.Success ->
                    onGetFriendsSuccess(result.getValue())
                is FriendsViewModel.GetFriendsResult.Failure -> {
                    onGetFriendsFail(result.getException())
                }
            }
        }
    }

    private fun onGetFriendsSuccess(users: List<UserModel>) {
        adapter?.submitList(users)
        if (users.isEmpty()) binding.layoutEmptyResults.root.show()
        else binding.layoutEmptyResults.root.gone()
    }

    private fun onGetFriendsFail(ex: Throwable) {
        showErrorBottomSheetDialog(
            getString(R.string.search_users_fail),
            ex.message ?: getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
        )
    }

    companion object {
        private const val MAX_USER_AMOUNT = 15
    }
}