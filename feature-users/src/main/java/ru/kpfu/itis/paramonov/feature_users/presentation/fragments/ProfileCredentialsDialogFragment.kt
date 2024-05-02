package ru.kpfu.itis.paramonov.feature_users.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import ru.kpfu.itis.paramonov.feature_users.R

class ProfileCredentialsDialogFragment: DialogFragment() {

    private var etEmail: EditText? = null

    private var etPassword: EditText? = null

    private var etConfirmPassword: EditText? = null

    interface OnCredentialsChangedListener {
        fun onCredentialsChanged(email: String?, password: String?)
    }

    private var onPositivePressed: OnCredentialsChangedListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(layoutInflater.inflate(R.layout.fragment_profile_credentials_dialog, null).apply {
                etEmail = findViewById(R.id.et_email)
                etPassword = findViewById(R.id.et_password)
            })
            .setTitle(getString(R.string.settings))
            .setPositiveButton(R.string.dialog_pos) { _, _ ->
                val password = etPassword?.text?.let {
                    if (it.isNotEmpty()) it.toString()
                    else null
                }
                val email = etEmail?.text?.let {
                    if (it.isNotEmpty()) it.toString()
                    else null
                }
                onPositivePressed?.onCredentialsChanged(password = password, email = email)
            }
            .setNegativeButton(R.string.dialog_neg) { _, _ -> }
            .create()
    }

    class Builder {
        private val dialog = ProfileCredentialsDialogFragment()

        fun setOnPositivePressed(onPositivePressed: OnCredentialsChangedListener): Builder = this.apply {
            dialog.onPositivePressed = onPositivePressed
        }

        fun build(): ProfileCredentialsDialogFragment = dialog
    }

    companion object {
        fun builder() = Builder()

        const val CREDENTIALS_DIALOG_TAG = "CREDENTIALS_DIALOG"
    }
}