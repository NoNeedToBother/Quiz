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
import ru.kpfu.itis.paramonov.feature_authentication.databinding.FragmentRegisterBinding
import ru.kpfu.itis.paramonov.feature_authentication.presentation.viewmodel.RegisterViewModel

@AndroidEntryPoint
class RegisterFragment: BaseFragment(R.layout.fragment_register) {

    private val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)

    private val viewModel: RegisterViewModel by viewModels()

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

    private suspend fun collectUserData() {
        viewModel.userDataFlow.collect { user ->
            user?.run {
                showMessageSnackbar(
                    binding.tvLogo,
                    getString(R.string.welcome_user, username)
                )
            }
        }
    }

    private suspend fun checkErrors() {
        for (error in viewModel.errorsChannel) {
            val errorMessage = error.message ?: getString(commonR.string.default_error_msg)
            val errorTitle = getString(R.string.registration_failed)

            showErrorBottomSheetDialog(errorTitle, errorMessage)
        }
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
                parentFragmentManager.beginTransaction()
                    .add(SignInFragment(), SignInFragment.SIGN_IN_FRAGMENT_TAG)
                    .commit()
            }
        }
    }

    companion object {
        const val REGISTER_FRAGMENT_TAG = "REGISTER_FRAGMENT"
    }
}
