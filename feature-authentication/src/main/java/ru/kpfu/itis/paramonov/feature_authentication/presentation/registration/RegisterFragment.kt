package ru.kpfu.itis.paramonov.feature_authentication.presentation.registration

import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.common_android.utils.collect
import ru.kpfu.itis.paramonov.common_android.utils.gone
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.feature_authentication.R
import ru.kpfu.itis.paramonov.common_android.R as commonR
import ru.kpfu.itis.paramonov.feature_authentication.databinding.FragmentRegisterBinding
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationComponent
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependencies
import javax.inject.Inject

class RegisterFragment: BaseFragment(R.layout.fragment_register) {

    private val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)

    @Inject
    lateinit var viewModel: RegisterViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureAuthenticationComponent>(this, FeatureAuthenticationDependencies::class.java)
            .registrationComponentFactory()
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

        viewModel.registerProceedingFlow.collect(lifecycleOwner = viewLifecycleOwner) {
            checkRegisterProceeding(it)
        }

    }

    private fun checkRegisterProceeding(proceeding: Boolean) {
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

    private fun collectUserData(result: RegisterViewModel.UserDataResult?) {
        result?.run {
            when (this) {
                is RegisterViewModel.UserDataResult.Success ->
                    onRegistrationSuccess(getValue())
                is RegisterViewModel.UserDataResult.Failure ->
                    onRegistrationFail(getException())
            }
        }
    }

    private fun onRegistrationSuccess(user: UserModel) {
        showMessageSnackbar(
            binding.tvLogo,
            getString(R.string.welcome_user, user.username)
        )
    }

    private fun onRegistrationFail(exception: Throwable) {
        val errorMessage = exception.message ?: getString(commonR.string.default_error_msg)
        val errorTitle = getString(R.string.registration_failed)

        showErrorBottomSheetDialog(errorTitle, errorMessage)
    }


    private fun setOnClickListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                val username = etUsername.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                viewModel.registerUser(username, email, password, confirmPassword)
            }

            btnGoSignIn.setOnClickListener {
                viewModel.goToSignIn()
            }
        }
    }
}
