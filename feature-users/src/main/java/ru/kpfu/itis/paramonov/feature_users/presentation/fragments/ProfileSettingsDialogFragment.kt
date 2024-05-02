package ru.kpfu.itis.paramonov.feature_users.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import ru.kpfu.itis.paramonov.feature_users.R

class ProfileSettingsDialogFragment: DialogFragment() {
    private var etUsername: EditText? = null

    private var etInfo: EditText? = null

    private var onPositivePressed: ((Map<String, String>) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(layoutInflater.inflate(R.layout.fragment_profile_settings_dialog, null).apply {
                etUsername = findViewById(R.id.et_username)
                etInfo = findViewById(R.id.et_info)
            })
            .setTitle(getString(R.string.settings))
            .setPositiveButton(R.string.dialog_pos) {_, _ ->
                val params = mutableMapOf<String, String>()
                etUsername?.text?.let {
                    if (it.isNotEmpty()) params[USERNAME_KEY] = it.toString()
                }
                etInfo?.text?.let {
                    if (it.isNotEmpty()) params[INFO_KEY] = it.toString()
                }
                onPositivePressed?.invoke(params)
            }
            .setNegativeButton(R.string.dialog_neg) {_, _ -> }
            .create()
    }

    class Builder {
        private val dialog = ProfileSettingsDialogFragment()

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