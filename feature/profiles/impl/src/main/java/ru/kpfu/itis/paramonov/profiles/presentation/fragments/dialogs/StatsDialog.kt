package ru.kpfu.itis.paramonov.profiles.presentation.fragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.ui.theme.AppTheme
import ru.kpfu.itis.paramonov.ui.views.EmptyResults
import ru.kpfu.itis.paramonov.ui.views.GraphView

class StatsDialog: DialogFragment() {

    private var results: List<ResultUiModel>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(
                ComposeView(requireContext()).apply {
                    setContent {
                        AppTheme {
                            StatsDialogContent(
                                results = results
                            )
                        }
                    }
                }
            )
            .setTitle(R.string.stats)
            .create()
    }

    class Builder {
        private val dialog = StatsDialog()

        fun provideResultList(results: List<ResultUiModel>): Builder = this.apply {
            dialog.results = results
        }

        fun build(): StatsDialog = dialog
    }

    companion object {
        fun builder(): Builder = Builder()

        const val STATS_DIALOG_TAG = "STATS_DIALOG"
    }
}

@Composable
fun StatsDialogContent(
    results: List<ResultUiModel>?
) {
    results?.let {
        if (results.isNotEmpty()) {
            val list = mutableListOf<Pair<Double, Double>>()
            for (i in results.indices) {
                list.add((i + 1).toDouble() to results[i].score)
            }

            GraphView(
                values = list,
                gradient = true
            )
        } else EmptyResults()
    } ?: EmptyResults()
}