package ru.kpfu.itis.paramonov.feature_profiles.presentation.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import ru.kpfu.itis.paramonov.ui.views.GraphView
import ru.kpfu.itis.paramonov.core.utils.show
import ru.kpfu.itis.paramonov.feature_profiles.R
import ru.kpfu.itis.paramonov.feature_profiles.presentation.model.ResultUiModel

class StatsDialogFragment: DialogFragment() {

    private var results: List<ResultUiModel>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(
                layoutInflater.inflate(R.layout.fragment_stats_dialog, null).apply {
                    onViewSet(this)
                }
            )
            .setTitle(R.string.stats)
            .create()
    }

    private fun onViewSet(view: View) {
        results?.let { results ->
            val gvResults = view.findViewById<GraphView>(R.id.gv_stats)
            val list = mutableListOf<Pair<Double, Double>>()
            for (i in results.indices) {
                list.add((i + 1).toDouble() to results[i].score)
            }
            gvResults.provideValues(list)
            if (results.isEmpty()) view.findViewById<LinearLayout>(R.id.layout_empty_results).show()
            else gvResults.show()
        }
    }

    class Builder {
        private val dialog = StatsDialogFragment()

        fun provideResultList(results: List<ResultUiModel>): Builder = this.apply {
            dialog.results = results
        }

        fun build(): StatsDialogFragment = dialog
    }

    companion object {
        fun builder(): Builder = Builder()

        const val STATS_DIALOG_TAG = "STATS_DIALOG"
    }
}