package ru.kpfu.itis.paramonov.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.ui.views.ErrorBottomSheetDialogFragment
import ru.kpfu.itis.paramonov.ui.views.MessageSnackbar

abstract class MviBaseFragment: Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    protected abstract fun inject()

    protected abstract fun initView(): ComposeView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initView()
    }

    protected open fun showErrorBottomSheetDialog(errorTitle: String, errorMessage: String) {
        ErrorBottomSheetDialogFragment(errorTitle, errorMessage).show(
            parentFragmentManager,
            ErrorBottomSheetDialogFragment.ERROR_BOTTOM_SHEET_DIALOG_TAG
        )
    }

    protected open fun showMessageSnackbar(anchor: View, message: String) {
        val messageSnackbar = MessageSnackbar(requireContext(), anchor, message)
        val displayMetrics = requireContext().resources.displayMetrics
        val marginTop = displayMetrics.heightPixels / 20
        val defaultMargin =
            requireContext().resources.getDimensionPixelSize(com.google.android.material.R.dimen.mtrl_snackbar_margin)
        messageSnackbar.adjustPosition(defaultMargin, marginTop = marginTop)
        messageSnackbar.show()

    }
}