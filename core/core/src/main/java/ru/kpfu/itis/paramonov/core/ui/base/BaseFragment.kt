package ru.kpfu.itis.paramonov.core.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
//import ru.kpfu.itis.paramonov.common_android.ui.views.ErrorBottomSheetDialogFragment
//import ru.kpfu.itis.paramonov.common_android.ui.views.MessageSnackbar

abstract class BaseFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    protected abstract fun inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeData()
    }

    protected abstract fun initView()

    protected abstract fun observeData()

    /*protected open fun showErrorBottomSheetDialog(errorTitle: String, errorMessage: String) {
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

    }*/
}