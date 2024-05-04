package ru.kpfu.itis.paramonov.feature_users.presentation.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_users.R
import ru.kpfu.itis.paramonov.feature_users.databinding.FragmentProfileOtherUserBinding
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersComponent
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersDependencies
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.BaseProfileViewModel
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.OtherUserProfileViewModel
import javax.inject.Inject

class OtherUserProfileFragment: BaseFragment(R.layout.fragment_profile_other_user) {

    private val binding: FragmentProfileOtherUserBinding by viewBinding(FragmentProfileOtherUserBinding::bind)

    private val id: String? get() {
        val args = requireArguments()
        return args.getString(USER_ID_KEY)
    }

    @Inject
    lateinit var viewModel: OtherUserProfileViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureUsersComponent>(this, FeatureUsersDependencies::class.java)
            .otherUserProfileComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
    }

    override fun observeData() {
        id?.let {
            viewModel.getUser(it)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectUserData()
                }
            }
        }
    }

    private suspend fun collectUserData() {
        viewModel.userDataFlow.collect {
            it?.let {  result ->
                when(result) {
                    is BaseProfileViewModel.UserDataResult.Success -> showUserInfo(result.getValue())
                    is BaseProfileViewModel.UserDataResult.Failure -> showErrorBottomSheetDialog(
                        getString(R.string.user_data_fail_title),
                        result.getException().message ?:
                        getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg)
                    )
                }
            }
        }
    }

    private fun showUserInfo(user: UserModel) {
        with(binding) {
            etUsername.setText(user.username)
            etInfo.setText(user.info)
            etRegistrationDate.setText(
                getString(R.string.registration_date, user.dateRegistered)
            )
            loadProfilePicture(user.profilePictureUrl)
        }
    }

    private fun loadProfilePicture(url: String) {
        Glide.with(requireContext())
            .load(url)
            .placeholder(ru.kpfu.itis.paramonov.common_android.R.drawable.default_pfp)
            .error(ru.kpfu.itis.paramonov.common_android.R.drawable.default_pfp)
            .centerCrop()
            .into(binding.ivProfilePicture)
    }

    companion object {
        const val USER_ID_KEY = "id"
    }
}