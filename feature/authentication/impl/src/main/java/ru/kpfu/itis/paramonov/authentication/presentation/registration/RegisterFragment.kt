package ru.kpfu.itis.paramonov.authentication.presentation.registration

import androidx.core.widget.addTextChangedListener
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.core.validators.PasswordValidator
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.core.utils.collect
import ru.kpfu.itis.paramonov.core.utils.gone
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.authentication.R
import ru.kpfu.itis.paramonov.ui.R as uiR
import ru.kpfu.itis.paramonov.authentication.databinding.FragmentRegisterBinding
import ru.kpfu.itis.paramonov.authentication.di.FeatureAuthenticationComponent
import ru.kpfu.itis.paramonov.authentication.di.FeatureAuthenticationDependencies
import javax.inject.Inject

class RegisterFragment: BaseFragment(R.layout.fragment_register) {

    private val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)

    @Inject
    lateinit var viewModel: RegisterViewModel

    @Inject
    lateinit var usernameValidator: UsernameValidator

    @Inject
    lateinit var passwordValidator: PasswordValidator

    override fun inject() {
        FeatureUtils.getFeature<FeatureAuthenticationComponent>(this, FeatureAuthenticationDependencies::class.java)
            .registrationComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        setOnClickListeners()
        addTextChangedListeners()
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
        val errorMessage = exception.message ?: getString(uiR.string.default_error_msg)
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

    private fun addTextChangedListeners() {
        with(binding) {
            etUsername.addTextChangedListener {
                it?.let {  username ->
                    if (!usernameValidator.validate(username.toString()))
                        tilUsername.error = usernameValidator.getMessage()
                    else tilUsername.error = null
                }
            }
            etPassword.addTextChangedListener {
                it?.let { password ->
                    if (!passwordValidator.validate(password.toString()))
                        tilPassword.error = passwordValidator.getMessage()
                    else tilPassword.error = null
                }
            }
            etConfirmPassword.addTextChangedListener {
                it?.let { password ->
                    if (!passwordValidator.validate(password.toString()))
                        tilConfirmPassword.error = passwordValidator.getMessage()
                    else tilConfirmPassword.error = null
                }
            }
        }
    }
}
