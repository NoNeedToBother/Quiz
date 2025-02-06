package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.presentation.model.ResultUiModel
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.DialogWithTitle
import ru.kpfu.itis.paramonov.ui.components.EmptyResults
import ru.kpfu.itis.paramonov.ui.components.Graph

@Composable
fun StatsDialog(
    results: List<ResultUiModel>,
    onDismiss: () -> Unit
) {
    DialogWithTitle(
        title = stringResource(R.string.stats),
        onDismiss = onDismiss
    ) {
        if (results.isNotEmpty()) {
            val list = mutableListOf<Pair<Double, Double>>()
            for (i in results.indices) {
                list.add((i + 1).toDouble() to results[i].score)
            }

            Graph(
                values = list,
                gradient = true
            )
        } else EmptyResults()
    }
}
