package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.kpfu.itis.paramonov.core.validators.UsernameValidator
import ru.kpfu.itis.paramonov.profiles.R

class ProfileSettingsDialogFragment: DialogFragment() {

    private var etUsername: EditText? = null

    private var tilUsername: TextInputLayout? = null

    private var etInfo: EditText? = null

    private var onPositivePressed: ((Map<String, String>) -> Unit)? = null

    private var usernameValidator: UsernameValidator? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(initView())
            .setTitle(getString(R.string.settings))
            .setPositiveButton(R.string.dialog_pos) {_, _ ->
                setPositiveButton()
            }
            .setNegativeButton(R.string.dialog_neg) {_, _ -> }
            .create()
    }

    private fun setPositiveButton() {
        val params = mutableMapOf<String, String>()
        etUsername?.text?.let {
            if (it.isNotEmpty()) params[USERNAME_KEY] = it.toString()
        }
        etInfo?.text?.let {
            if (it.isNotEmpty()) params[INFO_KEY] = it.toString()
        }
        onPositivePressed?.invoke(params)
    }

    private fun initView(): View {
        return layoutInflater.inflate(R.layout.fragment_profile_settings_dialog, null).apply {
            etUsername = findViewById<TextInputEditText>(R.id.et_username)
            etInfo = findViewById<TextInputEditText>(R.id.et_info)
            tilUsername = findViewById(R.id.til_username)

            etUsername?.addTextChangedListener {
                it?.let { username ->
                    usernameValidator?.let { validator ->
                        if (!validator.validate(username.toString()))
                            tilUsername?.error = validator.getMessage()
                        else tilUsername?.error = null
                    }
                }
            }
        }
    }

    class Builder {
        private val dialog = ProfileSettingsDialogFragment()

        fun setUsernameValidator(usernameValidator: UsernameValidator): Builder = this.apply {
            dialog.usernameValidator = usernameValidator
        }

        fun setOnPositivePressed(onPositivePressed: (Map<String, String>) -> Unit): Builder = this.apply {
            dialog.onPositivePressed = onPositivePressed
        }

        fun build(): ProfileSettingsDialogFragment = dialog
    }

    companion object {
        fun builder() = Builder()

        const val USERNAME_KEY = "username"
        const val INFO_KEY = "info"

        const val SETTINGS_DIALOG_TAG = "SETTINGS_DIALOG"
    }
}