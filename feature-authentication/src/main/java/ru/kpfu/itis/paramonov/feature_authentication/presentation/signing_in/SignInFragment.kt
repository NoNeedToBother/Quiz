package ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in

import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.common_android.utils.collect
import ru.kpfu.itis.paramonov.common_android.utils.gone
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.feature_authentication.R
import ru.kpfu.itis.paramonov.common_android.R as commonR
import ru.kpfu.itis.paramonov.feature_authentication.databinding.FragmentSignInBinding
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationComponent
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependencies
import javax.inject.Inject

class SignInFragment: BaseFragment(R.layout.fragment_sign_in) {

    private val binding: FragmentSignInBinding by viewBinding(FragmentSignInBinding::bind)

    @Inject
    lateinit var viewModel: SignInViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureAuthenticationComponent>(this, FeatureAuthenticationDependencies::class.java)
            .signingInComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        setOnClickListeners()
    }

    override fun observeData() {
        viewModel.checkCurrentUser()
        viewModel.userDataFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            collectUserData(it)
        }

        viewModel.signInProceedingFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            checkSigningInProceeding(it)
        }
    }

    private fun checkSigningInProceeding(proceeding: Boolean) {
        with(binding) {
            if (proceeding) {
                layoutBase.gone()
                layoutProceeding.root.show()
            } else {
                layoutBase.show()
                layoutProceeding.root.gone()
            }
        }
    }

    private fun collectUserData(result: SignInViewModel.UserDataResult?) {
        result?.run {
            when (this) {
                is SignInViewModel.UserDataResult.Success ->
                    onSigningInSuccess(getValue())
                is SignInViewModel.UserDataResult.Failure ->
                    onSigningInFail(getException())
            }
        }
    }

    private fun onSigningInSuccess(user: UserModel) {
        showMessageSnackbar(
            binding.tvLogo,
            getString(R.string.welcome_back_user, user.username)
        )
    }

    private fun onSigningInFail(exception: Throwable) {
        val errorMessage = exception.message ?: getString(commonR.string.default_error_msg)
        val errorTitle = getString(R.string.login_failed)

        showErrorBottomSheetDialog(errorTitle, errorMessage)
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                viewModel.authenticateUser(email, password)
            }

            btnGoRegister.setOnClickListener {
                viewModel.goToRegister()
            }
        }
    }
}