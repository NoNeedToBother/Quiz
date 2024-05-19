package ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import ru.kpfu.itis.paramonov.common.validators.PasswordValidator
import ru.kpfu.itis.paramonov.feature_users.R

class ProfileCredentialsDialogFragment: DialogFragment() {

    private var passwordValidator: PasswordValidator? = null

    private var etEmail: EditText? = null

    private var etPassword: EditText? = null

    private var tilPassword: TextInputLayout? = null

    private var etConfirmPassword: EditText? = null

    private var tilConfirmPassword: TextInputLayout? = null

    private var onDismiss: (() -> Unit)? = null

    interface OnCredentialsChangedListener {
        fun onCredentialsChanged(email: String?, password: String?, confirmPassword: String?)
    }

    private var onPositivePressed: OnCredentialsChangedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(initView())
            .setTitle(getString(R.string.settings))
            .setPositiveButton(R.string.dialog_pos) { _, _ ->
                setPositiveButton()
            }
            .setNegativeButton(R.string.dialog_neg) { _, _ -> }
            .create()
    }

    private fun setPositiveButton() {
        onDismiss = null
        val password = etPassword?.text?.let {
            if (it.isNotEmpty()) it.toString()
            else null
        }
        val email = etEmail?.text?.let {
            if (it.isNotEmpty()) it.toString()
            else null
        }
        val confirmPassword = etConfirmPassword?.text?.let {
            if (it.isNotEmpty()) it.toString()
            else null
        }
        onPositivePressed?.onCredentialsChanged(password = password,
            email = email, confirmPassword = confirmPassword)
    }

    private fun initView(): View = layoutInflater.inflate(R.layout.fragment_profile_credentials_dialog, null).apply {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        tilPassword = findViewById(R.id.til_password)
        tilConfirmPassword = findViewById(R.id.til_confirm_password)

        addTextChangedListeners()
    }

    private fun addTextChangedListeners() {
        etPassword?.addTextChangedListener {
            it?.let { password ->
                passwordValidator?.let {  validator ->
                    if (!validator.validate(password.toString()))
                        tilPassword?.error = validator.getMessage()
                    else tilPassword?.error = null
                }
            }
        }
        etConfirmPassword?.addTextChangedListener {
            it?.let { password ->
                passwordValidator?.let { validator ->
                    if (!validator.validate(password.toString()))
                        tilConfirmPassword?.error = validator.getMessage()
                    else tilConfirmPassword?.error = null
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    class Builder {
        private val dialog = ProfileCredentialsDialogFragment()

        fun setOnPositivePressed(onPositivePressed: OnCredentialsChangedListener): Builder = this.apply {
            dialog.onPositivePressed = onPositivePressed
        }

        fun setPasswordValidator(passwordValidator: PasswordValidator): Builder = this.apply {
            dialog.passwordValidator = passwordValidator
        }

        fun setOnDismiss(onDismiss: () -> Unit): Builder = this.apply {
            dialog.onDismiss = onDismiss
        }

        fun build(): ProfileCredentialsDialogFragment = dialog
    }

    companion object {
        fun builder() = Builder()

        const val CREDENTIALS_DIALOG_TAG = "CREDENTIALS_DIALOG"
    }
}