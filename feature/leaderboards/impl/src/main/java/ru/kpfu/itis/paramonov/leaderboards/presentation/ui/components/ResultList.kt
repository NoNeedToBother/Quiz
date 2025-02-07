package ru.kpfu.itis.paramonov.leaderboards.presentation.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import ru.kpfu.itis.paramonov.core.utils.normalizeEnumName
import ru.kpfu.itis.paramonov.leaderboards.R
import ru.kpfu.itis.paramonov.leaderboards.presentation.model.ResultUiModel

@Composable
fun ResultItem(
    result: ResultUiModel,
    onProfileClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(result.user.profilePictureUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .clickable { onProfileClick(result.user.id) },
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = result.user.username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = result.score.toString(), fontSize = 14.sp, color = Color.Gray)
            }
        }

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(stringResource(R.string.res_game_mode, result.gameMode.name.normalizeEnumName()), fontSize = 14.sp)
                    Text(stringResource(R.string.res_difficulty, result.difficulty.name.normalizeEnumName()), fontSize = 14.sp)
                    Text(stringResource(R.string.res_category, result.category.name.normalizeEnumName()), fontSize = 14.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    val min = result.time / 60
                    val sec = result.time % 60

                    Text(
                        if (min != 0) stringResource(
                            R.string.time_with_min, min, sec
                        ) else stringResource(R.string.time, sec),
                        fontSize = 14.sp)
                    Text(stringResource(R.string.res_ratio, result.correct, result.total), fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun ResultList(
    modifier: Modifier = Modifier,
    results: List<ResultUiModel>,
    onProfileClick: (String) -> Unit,
    shouldLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(results) { result ->
            ResultItem(result = result, onProfileClick = onProfileClick)
            HorizontalDivider()
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .map { it.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastIndex ->
                if (lastIndex != null && lastIndex >= results.size - 1) {
                    shouldLoadMore()
                }
            }
    }
}
