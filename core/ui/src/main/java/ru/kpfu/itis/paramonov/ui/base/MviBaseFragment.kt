package ru.kpfu.itis.paramonov.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.ui.components.ErrorBottomSheetDialogFragment

abstract class MviBaseFragment: Fragment() {

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
}