package ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import ru.kpfu.itis.paramonov.feature_authentication.databinding.FragmentSignInBinding
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationComponent
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependencies
import ru.kpfu.itis.paramonov.navigation.AuthenticationRouter
import ru.kpfu.itis.paramonov.navigation.MainMenuRouter
import javax.inject.Inject

class SignInFragment: BaseFragment(R.layout.fragment_sign_in) {

    private val binding: FragmentSignInBinding by viewBinding(FragmentSignInBinding::bind)

    @Inject
    lateinit var viewModel: SignInViewModel

    @Inject
    lateinit var authenticationRouter: AuthenticationRouter

    @Inject
    lateinit var mainMenuRouter: MainMenuRouter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        FeatureUtils.getFeature<FeatureAuthenticationComponent>(this, FeatureAuthenticationDependencies::class.java)
            .signingInComponentFactory()
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
                    checkSigningInProceeding()
                }
            }
        }
    }

    private suspend fun checkSigningInProceeding() {
        viewModel.signInProceedingFlow.collect {
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
                    is SignInViewModel.SigningInResult.Success ->
                        onSigningInSuccess(getValue())
                    is SignInViewModel.SigningInResult.Failure ->
                        onSigningInFail(getException())
                }
            }
        }
    }

    private fun onSigningInSuccess(user: UserModel) {
        showMessageSnackbar(
            binding.tvLogo,
            getString(R.string.welcome_back_user, user.username)
        )
        mainMenuRouter.goToMainMenu()
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
                authenticationRouter.goToRegister()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}