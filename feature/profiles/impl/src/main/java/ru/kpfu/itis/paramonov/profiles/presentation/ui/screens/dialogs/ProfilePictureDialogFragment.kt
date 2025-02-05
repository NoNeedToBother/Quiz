package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import ru.kpfu.itis.paramonov.profiles.R

class ProfilePictureDialogFragment: DialogFragment() {

    private var onPositivePressed: (() -> Unit)? = null

    private var uri: Uri? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = requireContext()
        return AlertDialog.Builder(context)
            .setView(
                layoutInflater.inflate(R.layout.fragment_profile_picture_dialog, null).apply {
                    uri?.let {
                        val iv = findViewById<ShapeableImageView>(R.id.iv_profile_picture)
                        Glide.with(context)
                            .load(it)
                            .centerCrop()
                            .into(iv)
                    }
                }
            )
            .setTitle(R.string.preview)
            .setPositiveButton(getString(R.string.dialog_pos)) { _, _ ->
                onPositivePressed?.invoke()
            }
            .setNegativeButton(getString(R.string.dialog_neg)) { _, _ -> }
            .create()
    }

    class Builder {
        private val dialog = ProfilePictureDialogFragment()

        fun setOnPositivePressed(onPositivePressed: () -> Unit): Builder = this.apply {
            dialog.onPositivePressed = onPositivePressed
        }

        fun setImageSource(uri: Uri): Builder = this.apply {
            dialog.uri = uri
        }

        fun create(): ProfilePictureDialogFragment {
            return dialog
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }

        const val PROFILE_PICTURE_DIALOG_TAG = "PROFILE_PICTURE_DIALOG"
    }
}