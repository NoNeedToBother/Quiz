package ru.kpfu.itis.paramonov.feature_users.presentation.fragments

import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.common_android.utils.collect
import ru.kpfu.itis.paramonov.feature_users.R
import ru.kpfu.itis.paramonov.feature_users.databinding.FragmentSearchUsersBinding
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersComponent
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersDependencies
import ru.kpfu.itis.paramonov.feature_users.presentation.adapter.UserAdapter
import ru.kpfu.itis.paramonov.feature_users.presentation.adapter.diffutil.UserDiffUtilCallback
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.SearchUsersViewModel
import javax.inject.Inject

class SearchUsersFragment: BaseFragment(R.layout.fragment_search_users) {

    private val binding by viewBinding(FragmentSearchUsersBinding::bind)

    @Inject
    lateinit var viewModel: SearchUsersViewModel

    private var adapter: UserAdapter? = null

    override fun inject() {
        FeatureUtils.getFeature<FeatureUsersComponent>(this, FeatureUsersDependencies::class.java)
            .searchUsersComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        initSearchView()
        initRecyclerView()
    }

    private fun initSearchView() {
        binding.svUsername.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchUsers(it)
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
        }
    }

    private fun onUserClicked(user: UserModel) {
        viewModel.navigateToUser(user.id)
    }

    override fun observeData() {
        viewModel.searchUsersFLow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectUsersData(it)
        }
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
    }

    private fun onSearchUsersFail(ex: Throwable) {
        showErrorBottomSheetDialog(
            getString(R.string.search_users_fail),
            ex.message ?: getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg)
        )
    }
}