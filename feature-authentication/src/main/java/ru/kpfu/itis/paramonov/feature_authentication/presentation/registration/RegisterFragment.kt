package ru.kpfu.itis.paramonov.feature_authentication.presentation.registration

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.common_android.utils.gone
import ru.kpfu.itis.paramonov.common_android.utils.show
import ru.kpfu.itis.paramonov.feature_authentication.R
import ru.kpfu.itis.paramonov.common_android.R as commonR
import ru.kpfu.itis.paramonov.feature_authentication.databinding.FragmentRegisterBinding
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationComponent
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependencies
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import javax.inject.Inject

class RegisterFragment: BaseFragment(R.layout.fragment_register) {

    private val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)

    @Inject
    lateinit var viewModel: RegisterViewModel

    @Inject
    lateinit var authenticationRouter: AuthenticationRouter

    @Inject
    lateinit var mainMenuRouter: MainMenuRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FeatureUtils.getFeature<FeatureAuthenticationComponent>(this, FeatureAuthenticationDependencies::class.java)
            .registrationComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView() {
        setOnClickListeners()
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectUserData()
                }
                launch {
                    checkRegisterProceeding()
                }
            }
        }
    }

    private suspend fun checkRegisterProceeding() {
        viewModel.registerProceedingFlow.collect {
            with(binding) {
                if (it) {
                    layoutBase.gone()
                    layoutProceeding.show()
                } else {
                    layoutBase.show()
                    layoutProceeding.gone()
                }
            }
        }
    }

    private suspend fun collectUserData() {
        viewModel.userDataFlow.collect { result ->
            result?.run {
                when (this) {
                    is RegisterViewModel.RegistrationResult.Success ->
                        onRegistrationSuccess(getValue())
                    is RegisterViewModel.RegistrationResult.Failure ->
                        onRegistrationFail(getException())
                }
            }
        }
    }

    private fun onRegistrationSuccess(user: UserModel) {
        showMessageSnackbar(
            binding.tvLogo,
            getString(R.string.welcome_user, user.username)
        )
        mainMenuRouter.goToMainMenu()
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
                authenticationRouter.goToSignIn()
            }
        }
    }
}
