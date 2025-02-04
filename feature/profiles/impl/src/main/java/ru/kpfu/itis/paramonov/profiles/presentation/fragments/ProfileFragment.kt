package ru.kpfu.itis.paramonov.profiles.presentation.fragments

import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.core.utils.collect
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.core.utils.startPostponedTransition
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.databinding.FragmentProfileBinding
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesComponent
import ru.kpfu.itis.paramonov.profiles.di.FeatureProfilesDependencies
import ru.kpfu.itis.paramonov.profiles.presentation.fragments.dialogs.ConfirmCredentialsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.fragments.dialogs.ProfileCredentialsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.fragments.dialogs.ProfilePictureDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.fragments.dialogs.ProfileSettingsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.fragments.dialogs.RequestsDialogFragment
import ru.kpfu.itis.paramonov.profiles.presentation.fragments.dialogs.StatsDialog
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.BaseProfileViewModel
import ru.kpfu.itis.paramonov.profiles.presentation.viewmodel.ProfileViewModel
import javax.inject.Inject

class ProfileFragment: BaseFragment(R.layout.fragment_profile) {

    private val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)

    @Inject
    lateinit var viewModel: ProfileViewModel

    @Inject
    lateinit var usernameValidator: UsernameValidator

    @Inject
    lateinit var passwordValidator: PasswordValidator

    override fun inject() {
        FeatureUtils.getFeature<FeatureProfilesComponent>(this, FeatureProfilesDependencies::class.java)
            .profileComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        setOnClickListeners()
        initSettingsMenu()
        postponeEnterTransition()
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
            btnStats.setOnClickListener {
                onStatsClicked()
            }
        }
    }

    private fun onStatsClicked() {
        viewModel.getLastResults(MAX_RESULTS_AMOUNT)
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

        viewModel.userDataFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectUserData(it)
        }
        viewModel.confirmCredentialsFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectConfirmCredentialsData(it)
        }
        viewModel.changeUserDataErrorFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectChangeUserDataErrors(it)
        }
        viewModel.friendRequestsDataFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectRequestsData(it)
        }
        viewModel.processingCredentialEvents.collect(lifecycleOwner = viewLifecycleOwner) {
            collectProcessingCredentialEvents(it)
        }
        viewModel.resultsDataFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectResultsData(it)
        }
    }

    private fun collectResultsData(results: BaseProfileViewModel.LastResultsDataResult?) {
        results?.let {
            when(it) {
                is BaseProfileViewModel.LastResultsDataResult.Success -> onLastResultsDataReceived(it.getValue())
                is BaseProfileViewModel.LastResultsDataResult.Failure ->
                    showErrorBottomSheetDialog(
                        getString(R.string.get_results_fail),
                        it.getException().message ?:
                        getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
                    )
            }
        }
    }

    private fun onLastResultsDataReceived(list: List<ResultUiModel>) {
        StatsDialog.builder()
            .provideResultList(list)
            .build()
            .show(childFragmentManager, StatsDialog.STATS_DIALOG_TAG)
    }

    private fun collectRequestsData(requests: ProfileViewModel.FriendRequestResult?) {
        requests?.let {
            when(it) {
                is ProfileViewModel.FriendRequestResult.Success -> onRequestsDataReceived(it.getValue())
                is ProfileViewModel.FriendRequestResult.Failure ->
                    showErrorBottomSheetDialog(
                        getString(R.string.get_requests_fail),
                        it.getException().message ?:
                        getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
                    )
            }
        }
    }

    private fun onRequestsDataReceived(list: List<UserModel>) {
        RequestsDialogFragment.builder()
            .provideRequestList(list)
            .setOnRequestAccepted {
                viewModel.acceptFriendRequest(it)
            }
            .setOnRequestDenied {
                viewModel.denyFriendRequest(it)
            }
            .build()
            .show(childFragmentManager, RequestsDialogFragment.REQUESTS_DIALOG_TAG)
    }

    private fun collectProcessingCredentialEvents(processing: Boolean) {
        setChangeCredentialMenuItemEnabled(!processing)
    }

    private fun collectChangeUserDataErrors(ex: Throwable?) {
        ex?.let {
            showErrorBottomSheetDialog(
                getString(R.string.dialog_incorrect_credentials),
                getString(R.string.credentials_change_failed)
            )
        }
    }

    private fun collectConfirmCredentialsData(confirm: Boolean?) {
        confirm?.let {
            if (confirm) onCredentialsConfirmed()
            else onConfirmCredentialsFailed()
        }
    }

    private fun collectUserData(result: BaseProfileViewModel.UserDataResult?) {
        result?.let {
            when(result) {
                is BaseProfileViewModel.UserDataResult.Success -> showUserInfo(result.getValue())
                is BaseProfileViewModel.UserDataResult.Failure -> showErrorBottomSheetDialog(
                    getString(R.string.user_data_fail_title),
                    result.getException().message ?:
                    getString(ru.kpfu.itis.paramonov.core.R.string.default_error_msg)
                )
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
            btnRequests.show()
            btnStats.show()
            with(user.friendRequestFromList) {
                if (isNotEmpty()) {
                    ctvRequests.show()
                    ctvRequests.text = size.toString()
                }
            }
            startPostponedTransition()
        }
    }

    private fun onUserSettingsClicked() {
        ProfileSettingsDialogFragment.builder()
            .setOnPositivePressed {
                viewModel.saveUserSettings(it)
            }
            .setUsernameValidator(usernameValidator)
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
                override fun onCredentialsChanged(
                    email: String?,
                    password: String?,
                    confirmPassword: String?
                ) {
                    if (password != null && confirmPassword != null) {
                        if (password == confirmPassword) viewModel.changeCredentials(email, password)
                        else showErrorBottomSheetDialog(
                            getString(R.string.dialog_incorrect_credentials),
                            getString(R.string.password_confirm_password_not_match)
                        )
                    }
                    else if (password == null && confirmPassword == null) {
                        viewModel.changeCredentials(email, null)
                    } else showErrorBottomSheetDialog(
                        getString(R.string.credentials_change_failed),
                        getString(R.string.dialog_incorrect_credentials)
                    )
                }
            })
            .setOnDismiss {
                onDialogDismiss()
            }
            .setPasswordValidator(passwordValidator)
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
                popupMenu.setOnDismissListener {
                    ivSettings.apply {
                        setImageDrawable(
                            AppCompatResources.getDrawable(requireContext(), R.drawable.reverse_settings_anim)
                        )
                        (drawable as AnimatedVectorDrawable).start()
                    }
                }
                (it as ImageView).apply {
                    setImageDrawable(
                        AppCompatResources.getDrawable(requireContext(), R.drawable.settings_anim)
                    )
                    (drawable as AnimatedVectorDrawable).start()
                }
            }
        }
    }

    private fun loadProfilePicture(url: String) {
        Glide.with(requireContext())
            .load(url)
            .placeholder(ru.kpfu.itis.paramonov.core.R.drawable.default_pfp)
            .error(ru.kpfu.itis.paramonov.core.R.drawable.default_pfp)
            .centerCrop()
            .into(binding.ivProfilePicture)
    }

    companion object {
        private const val MAX_RESULTS_AMOUNT = 10
    }
}