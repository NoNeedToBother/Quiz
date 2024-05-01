package ru.kpfu.itis.paramonov.feature_users.presentation.fragments

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import ru.kpfu.itis.paramonov.feature_users.databinding.FragmentProfileBinding
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersComponent
import ru.kpfu.itis.paramonov.feature_users.di.FeatureUsersDependencies
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.BaseProfileViewModel
import ru.kpfu.itis.paramonov.feature_users.presentation.viewmodel.ProfileViewModel
import javax.inject.Inject

class ProfileFragment: BaseFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureUsersComponent>(this, FeatureUsersDependencies::class.java)
            .profileComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        setOnClickListeners()
    }

    private val pickProfilePictureIntent = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) {
        it?.let { uri -> onProfilePictureChosen(uri)}
    }

    private fun setOnClickListeners() {
        with(binding) {
            fabChangePfp.setOnClickListener {
                pickProfilePictureIntent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }

    private fun onProfilePictureChosen(uri: Uri) {
        viewModel.saveProfilePicture(uri)
    }

    override fun observeData() {
        viewModel.getCurrentUser()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                collectUserData()
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
            fabChangePfp.show()
        }
    }

    private fun loadProfilePicture(url: String) {
        Glide.with(requireContext())
            .load(url)
            .placeholder(R.drawable.default_pfp)
            .error(R.drawable.default_pfp)
            .into(binding.ivProfilePicture)
    }
}