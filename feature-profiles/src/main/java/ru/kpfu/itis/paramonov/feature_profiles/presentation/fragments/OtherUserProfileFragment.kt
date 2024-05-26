package ru.kpfu.itis.paramonov.feature_profiles.presentation.fragments

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeTransform
import android.transition.Transition
import android.transition.TransitionSet
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.common_android.utils.collect
import ru.kpfu.itis.paramonov.common_android.utils.gone
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.common_android.utils.startPostponedTransition
import ru.kpfu.itis.paramonov.feature_profiles.R
import ru.kpfu.itis.paramonov.feature_profiles.databinding.FragmentProfileOtherUserBinding
import ru.kpfu.itis.paramonov.feature_profiles.di.FeatureProfilesComponent
import ru.kpfu.itis.paramonov.feature_profiles.di.FeatureProfilesDependencies
import ru.kpfu.itis.paramonov.feature_profiles.presentation.fragments.dialogs.StatsDialogFragment
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.FriendStatusUiModel
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.feature_profiles.presentation.viewmodel.BaseProfileViewModel
import ru.kpfu.itis.paramonov.feature_profiles.presentation.viewmodel.OtherUserProfileViewModel
import javax.inject.Inject

class OtherUserProfileFragment: BaseFragment(R.layout.fragment_profile_other_user) {

    private val binding: FragmentProfileOtherUserBinding by viewBinding(FragmentProfileOtherUserBinding::bind)

    private val id: String? get() {
        val args = requireArguments()
        return args.getString(USER_ID_KEY)
    }

    private val transitionName = arguments?.getString(SHARED_ELEMENT_TRANSITION_NAME_KEY)

    @Inject
    lateinit var viewModel: OtherUserProfileViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureProfilesComponent>(this, FeatureProfilesDependencies::class.java)
            .otherUserProfileComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = getSharedElementTransition()
        sharedElementReturnTransition = getSharedElementTransition()
    }

    override fun initView() {
        showSharedElementTransition()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnStats.setOnClickListener {
                onStatsClicked()
            }
        }
    }

    private fun onStatsClicked() {
        viewModel.getLastResults(MAX_RESULTS_AMOUNT, id ?: "")
    }

    private fun showSharedElementTransition() {
        postponeEnterTransition()
        transitionName?.let {
            ViewCompat.setTransitionName(binding.ivProfilePicture, it)
        }
    }

    private fun getSharedElementTransition(): Transition {
        return TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
    }

    override fun observeData() {
        id?.let {
            viewModel.getUser(it)
            viewModel.checkFriendStatus(it)
        }

        viewModel.friendStatusDataFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectFriendStatusData(it)
        }
        viewModel.userDataFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectUserData(it)
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
                        getString(ru.kpfu.itis.paramonov.common_android.R.string.default_error_msg)
                    )
            }
        }
    }

    private fun onLastResultsDataReceived(list: List<ResultUiModel>) {
        StatsDialogFragment.builder()
            .provideResultList(list)
            .build()
            .show(childFragmentManager, StatsDialogFragment.STATS_DIALOG_TAG)
    }

    private fun collectFriendStatusData(result: OtherUserProfileViewModel.FriendStatusDataResult?) {
        result?.let {
            when(result) {
                is OtherUserProfileViewModel.FriendStatusDataResult.Success ->
                    onFriendStatusReceived(result.getValue())
                is OtherUserProfileViewModel.FriendStatusDataResult.Failure -> {}
            }
        }
    }

    private fun onFriendStatusReceived(friendStatusUiModel: FriendStatusUiModel) {
        with(binding) {
            when(friendStatusUiModel) {
                FriendStatusUiModel.SAME_USER -> btnAddFriend.gone()
                FriendStatusUiModel.NOT_FRIEND -> {
                    btnAddFriend.setOnClickListener {
                        id?.let { viewModel.sendFriendRequest(it) }
                    }
                    btnAddFriend.setIconAndDrawableAndShow(
                        R.drawable.add_friend, R.string.add_friend)
                }
                FriendStatusUiModel.REQUEST_SENT -> btnAddFriend.setIconAndDrawableAndShow(
                    R.drawable.request_sent, R.string.request_sent)
                FriendStatusUiModel.FRIEND -> btnAddFriend.setIconAndDrawableAndShow(
                    R.drawable.is_friend, R.string.is_friend)
            }
        }
    }

    private fun MaterialButton.setIconAndDrawableAndShow(@DrawableRes drawableId: Int, @StringRes textId: Int) {
        icon = AppCompatResources.getDrawable(requireContext(), drawableId)
        text = getString(textId)
        show()
    }

    private fun collectUserData(result: BaseProfileViewModel.UserDataResult?) {
        result?.let {
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

    private fun showUserInfo(user: UserModel) {
        with(binding) {
            etUsername.setText(user.username)
            etInfo.setText(user.info)
            etRegistrationDate.setText(
                getString(R.string.registration_date, user.dateRegistered)
            )
            btnStats.show()
            loadProfilePicture(user.profilePictureUrl)
            startPostponedTransition()
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
        const val SHARED_ELEMENT_TRANSITION_NAME_KEY = "profile_picture_key"

        const val PROFILE_PICTURE_TRANSITION_NAME = "profile_picture"
        private const val MAX_RESULTS_AMOUNT = 10
    }
}