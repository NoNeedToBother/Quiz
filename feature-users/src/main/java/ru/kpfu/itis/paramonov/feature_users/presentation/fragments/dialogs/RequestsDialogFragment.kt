package ru.kpfu.itis.paramonov.feature_users.presentation.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.common.model.presentation.UserModel
import ru.kpfu.itis.paramonov.feature_users.R
import ru.kpfu.itis.paramonov.feature_users.presentation.adapter.RequestAdapter
import ru.kpfu.itis.paramonov.feature_users.presentation.adapter.diffutil.RequestDiffUtilCallback

class RequestsDialogFragment : DialogFragment() {

    private var onRequestAccepted: ((String) -> Unit)? = null

    private var onRequestDenied: ((String) -> Unit)? = null

    private var requests: List<UserModel>? = null


    private var adapter: RequestAdapter? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(
                layoutInflater.inflate(R.layout.fragment_requests_dialog, null).apply {
                    onViewSet(this)
                }
            )
            .setTitle(R.string.requests)
            .create()
    }

    private fun onViewSet(view: View) {
        if (onRequestAccepted != null && onRequestDenied != null && requests != null) {
            val adapter = RequestAdapter(
                diffUtilCallback = RequestDiffUtilCallback(),
                onRequestAccepted = onRequestAccepted!!,
                onRequestDenied = onRequestDenied!!,
                onRequestResolved = ::onRequestResolved)
            this.adapter = adapter
            val rvRequests = view.findViewById<RecyclerView>(R.id.rv_requests)
            rvRequests.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext())
            rvRequests.layoutManager = layoutManager
            adapter.submitList(requests)
        }
    }

    private fun onRequestResolved(pos: Int) {
        val requests = ArrayList(adapter?.currentList ?: listOf())
        requests.removeAt(pos)
        adapter?.submitList(requests)
    }

    class Builder {

        private val dialog = RequestsDialogFragment()

        fun setOnRequestAccepted(onRequestAccepted: (String) -> Unit): Builder = this.apply {
            dialog.onRequestAccepted = onRequestAccepted
        }

        fun setOnRequestDenied(onRequestDenied: (String) -> Unit): Builder = this.apply {
            dialog.onRequestDenied = onRequestDenied
        }

        fun setRequestList(requests: List<UserModel>): Builder = this.apply {
            dialog.requests = requests
        }

        fun build(): RequestsDialogFragment = dialog
    }

    companion object {
        fun builder(): Builder = Builder()

        const val REQUESTS_DIALOG_TAG = "REQUESTS_DIALOG"
    }
}