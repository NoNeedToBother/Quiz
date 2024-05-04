package ru.kpfu.itis.paramonov.feature_users.presentation.fragments

import android.net.Uri
import android.widget.PopupMenu
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
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs.ConfirmCredentialsDialogFragment
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs.ProfileCredentialsDialogFragment
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs.ProfilePictureDialogFragment
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs.ProfileSettingsDialogFragment
import ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs.RequestsDialogFragment
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
        initSettingsMenu()
    }

    private val pickProfilePictureIntent = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()) {
            it?.let {
                uri -> onProfilePictureChosen(uri)
            }
    }

    private fun setOnClickListeners() {
        with(binding) {
            fabChangePfp.setOnClickListener {
                pickProfilePictureIntent.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            btnRequests.setOnClickListener {
                onRequestsClicked()
            }
        }
    }

    private fun onRequestsClicked() {
        viewModel.getFriendRequests()
    }

    private fun onProfilePictureChosen(uri: Uri) {
        ProfilePictureDialogFragment.builder()
            .setOnPositivePressed {
                viewModel.saveProfilePicture(uri)
            }
            .setImageSource(uri)
            .create()
            .show(childFragmentManager,
                ProfilePictureDialogFragment.PROFILE_PICTURE_DIALOG_TAG)
    }

    override fun observeData() {
        viewModel.getCurrentUser()
        viewModel.subscribeToProfileUpdates()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectUserData()
                }
                launch {
                    collectConfirmCredentialsData()
                }
                launch {
                    collectChangeCredentialsErrors()
                }
                launch {
                    collectProcessingCredentialEvents()
                }
                launch {
                    collectRequestsData()
                }
            }
        }
    }

    private suspend fun collectRequestsData() {
        viewModel.friendRequestsDataFlow.collect {
            it?.let {
                when(it) {
                    is ProfileViewModel.FriendRequestResult.Success -> onRequestsDataReceived(it.getValue())
                    is ProfileViewModel.FriendRequestResult.Failure ->
                        showErrorBottomSheetDialog(
                            getString(ru.kpfu.itis.paramonov.common_android.R.string.empty),
                            it.getException().message ?:
                            getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg)
                        )
                }
            }
        }
    }

    private fun onRequestsDataReceived(list: List<UserModel>) {
        RequestsDialogFragment.builder()
            .setRequestList(list)
            .setOnRequestAccepted {
                viewModel.acceptFriendRequest(it)
            }
            .setOnRequestDenied {
                viewModel.denyFriendRequest(it)
            }
            .build()
            .show(childFragmentManager, RequestsDialogFragment.REQUESTS_DIALOG_TAG)
    }

    private suspend fun collectProcessingCredentialEvents() {
        viewModel.processingCredentialEvents.collect {
            setChangeCredentialMenuItemEnabled(!it)
        }
    }

    private suspend fun collectChangeCredentialsErrors() {
        viewModel.changeCredentialsErrorFlow.collect {
            it?.let {
                showErrorBottomSheetDialog(
                    getString(R.string.dialog_incorrect_credentials),
                    getString(R.string.credentials_change_failed)
                )
            }
        }
    }

    private suspend fun collectConfirmCredentialsData() {
        viewModel.confirmCredentialsFlow.collect {
            it?.let {
                if (it) onCredentialsConfirmed()
                else onConfirmCredentialsFailed()
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

    private fun onUserSettingsClicked() {
        ProfileSettingsDialogFragment.builder()
            .setOnPositivePressed {
                viewModel.saveUserSettings(it)
            }
            .build()
            .show(childFragmentManager, ProfileSettingsDialogFragment.SETTINGS_DIALOG_TAG)
    }

    private fun onLogoutClicked() {
        viewModel.logout()
    }

    private fun setChangeCredentialMenuItemEnabled(enabled: Boolean) {
        popupMenu.menu.findItem(R.id.item_credentials).setEnabled(enabled)
    }

    private fun onDialogDismiss() {
        setChangeCredentialMenuItemEnabled(true)
    }

    private fun onConfirmCredentialsFailed() {
        showErrorBottomSheetDialog(
            getString(R.string.dialog_incorrect_credentials),
            getString(R.string.dialog_incorrect_credentials_expanded)
        )
    }

    private fun onChangeCredentialsClicked() {
        setChangeCredentialMenuItemEnabled(false)
        ConfirmCredentialsDialogFragment.builder()
            .setOnPositivePressed(object : ConfirmCredentialsDialogFragment.OnCredentialsChangedListener {
                override fun onCredentialsChanged(email: String?, password: String?) {
                    if (email != null && password != null) viewModel.confirmCredentials(email, password)
                    else onConfirmCredentialsFailed()
                }
            })
            .setOnDismiss {
                onDialogDismiss()
            }
            .build()
            .show(childFragmentManager, ConfirmCredentialsDialogFragment.CONFIRM_CREDENTIALS_DIALOG_TAG)
    }

    private fun onCredentialsConfirmed() {
        ProfileCredentialsDialogFragment.builder()
            .setOnPositivePressed(object : ProfileCredentialsDialogFragment.OnCredentialsChangedListener {
                override fun onCredentialsChanged(email: String?, password: String?) {
                    viewModel.changeCredentials(email, password)
                }
            })
            .setOnDismiss {
                onDialogDismiss()
            }
            .build()
            .show(childFragmentManager, ProfileCredentialsDialogFragment.CREDENTIALS_DIALOG_TAG)
    }

    private val popupMenu: PopupMenu by lazy {
        PopupMenu(requireContext(), binding.ivSettings).apply {
            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.item_settings -> {
                        onUserSettingsClicked()
                        true
                    }
                    R.id.item_logout -> {
                        onLogoutClicked()
                        true
                    }
                    R.id.item_credentials -> {
                        onChangeCredentialsClicked()
                        true
                    }
                    else -> {true}
                }
            }
            inflate(R.menu.settings_menu)
        }
    }

    private fun initSettingsMenu() {
        with(binding) {
            ivSettings.setOnClickListener {
                popupMenu.show()
            }
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
}