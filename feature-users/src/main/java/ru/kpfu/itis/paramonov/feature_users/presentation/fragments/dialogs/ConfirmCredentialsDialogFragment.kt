package ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import ru.kpfu.itis.paramonov.feature_users.R

class ConfirmCredentialsDialogFragment: DialogFragment() {

    private var etEmail: EditText? = null

    private var etPassword: EditText? = null

    interface OnCredentialsChangedListener {
        fun onCredentialsChanged(email: String?, password: String?)
    }

    private var onPositivePressed: OnCredentialsChangedListener? = null

    private var onDismiss: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(layoutInflater.inflate(R.layout.fragment_confirm_credentials_dialog, null).apply {
                etEmail = findViewById(R.id.et_email)
                etPassword = findViewById(R.id.et_password)
            })
            .setTitle(getString(R.string.confirm_credentials))
            .setPositiveButton(R.string.confirm_dialog_pos) { _, _ ->
                onDismiss = null
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
            .setNegativeButton(R.string.confirm_dialog_neg) { _, _ -> }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    class Builder {
        private val dialog = ConfirmCredentialsDialogFragment()

        fun setOnPositivePressed(onPositivePressed: OnCredentialsChangedListener): Builder = this.apply {
            dialog.onPositivePressed = onPositivePressed
        }

        fun setOnDismiss(onDismiss: () -> Unit): Builder = this.apply {
            dialog.onDismiss = onDismiss
        }

        fun build(): ConfirmCredentialsDialogFragment = dialog
    }

    companion object {
        fun builder() = Builder()

        const val CONFIRM_CREDENTIALS_DIALOG_TAG = "CONFIRM_CREDENTIALS_DIALOG"
    }
}