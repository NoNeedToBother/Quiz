package ru.kpfu.itis.paramonov.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import ru.kpfu.itis.paramonov.ui.components.ErrorBottomSheetDialogFragment

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

    protected open fun showErrorBottomSheetDialog(errorTitle: String, errorMessage: String) {
        ErrorBottomSheetDialogFragment(errorTitle, errorMessage).show(
            parentFragmentManager,
            ErrorBottomSheetDialogFragment.ERROR_BOTTOM_SHEET_DIALOG_TAG
        )
    }
}