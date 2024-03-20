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
            }
        }
    }

    private suspend fun checkErrors() {
        for (error in viewModel.errorsChannel) {
            val errorMessage = error.message ?: ""
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

                viewModel.registerUser(username, email, password)
            }
        }
    }
}
