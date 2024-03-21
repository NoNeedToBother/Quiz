package ru.kpfu.itis.paramonov.feature_authentication.presentation.fragment

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.common_android.ui.fragments.BaseFragment
import ru.kpfu.itis.paramonov.feature_authentication.R
import ru.kpfu.itis.paramonov.common_android.R as commonR
import ru.kpfu.itis.paramonov.feature_authentication.databinding.FragmentSignInBinding
import ru.kpfu.itis.paramonov.feature_authentication.presentation.viewmodel.SignInViewModel

@AndroidEntryPoint
class SignInFragment(): BaseFragment(R.layout.fragment_sign_in) {

    private val binding: FragmentSignInBinding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun initView() {
        setOnClickListeners()
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    checkErrors()
                }
                launch {
                    collectUserData()
                }
            }
        }
    }

    private suspend fun checkErrors() {
        for (error in viewModel.errorsChannel) {
            val errorMessage = error.message ?: getString(commonR.string.default_error_msg)
            val errorTitle = getString(R.string.login_failed)

            showErrorBottomSheetDialog(errorTitle, errorMessage)
        }
    }

    private suspend fun collectUserData() {
        viewModel.userDataFlow.collect { user ->
            user?.run {
                showMessageSnackbar(
                    binding.tvLogo,
                    getString(R.string.welcome_back_user, username)
                )
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                viewModel.authenticateUser(email, password)
            }

            btnGoRegister.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .add(RegisterFragment(), RegisterFragment.REGISTER_FRAGMENT_TAG)
                    .commit()
            }
        }
    }

    companion object {
        const val SIGN_IN_FRAGMENT_TAG = "SIGN_IN_FRAGMENT"
    }
}