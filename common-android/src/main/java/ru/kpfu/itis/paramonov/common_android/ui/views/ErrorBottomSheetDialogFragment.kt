package ru.kpfu.itis.paramonov.common_android.ui.views

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.paramonov.common_android.R
import ru.kpfu.itis.paramonov.common_android.databinding.DialogErrorBottomSheetBinding

class ErrorBottomSheetDialogFragment(
    private val title: String,
    private val message: String
): BottomSheetDialogFragment(R.layout.dialog_error_bottom_sheet) {

    private val binding: DialogErrorBottomSheetBinding by viewBinding(
        DialogErrorBottomSheetBinding::bind
    )

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initViews()
        adjustHeight()
    }

    private fun initViews() {
        with (binding) {
            tvTitle.text = title
            tvMessage.text = message
        }
    }

    private fun adjustHeight() {
        val displayMetrics = requireContext().resources.displayMetrics
        val dialogHeight = displayMetrics.heightPixels / 3.0

        val layoutParams =
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                height = dialogHeight.toInt()
            }

        binding.root.layoutParams = layoutParams
    }

    companion object {
        const val ERROR_BOTTOM_SHEET_DIALOG_TAG = "ERROR_BOTTOM_SHEET_DIALOG"
    }

}