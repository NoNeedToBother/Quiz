package ru.kpfu.itis.paramonov.feature_authentication.presentation.signing_in

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common.model.UserModel
import ru.kpfu.itis.paramonov.common_android.ui.base.BaseFragment
import ru.kpfu.itis.paramonov.common_android.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.feature_authentication.R
import ru.kpfu.itis.paramonov.common_android.R as commonR
import ru.kpfu.itis.paramonov.feature_authentication.databinding.FragmentSignInBinding
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationComponent
import ru.kpfu.itis.paramonov.feature_authentication.di.FeatureAuthenticationDependencies
import ru.kpfu.itis.paramonov.feature_authentication.presentation.registration.RegisterFragment
import javax.inject.Inject

class SignInFragment: BaseFragment(R.layout.fragment_sign_in) {

    private val binding: FragmentSignInBinding by viewBinding(FragmentSignInBinding::bind)

    @Inject
    lateinit var viewModel: SignInViewModel

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
            }
        }
    }

    companion object {
        const val SIGN_IN_FRAGMENT_TAG = "SIGN_IN_FRAGMENT"
    }
}