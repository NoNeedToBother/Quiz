package ru.kpfu.itis.paramonov.questions.presentation.questions.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.QuestionsScreenSideEffect
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.QuestionsScreenState
import ru.kpfu.itis.paramonov.questions.presentation.questions.ui.components.QuestionPage
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.QuestionsViewModel
import ru.kpfu.itis.paramonov.ui.components.ErrorDialog
import ru.kpfu.itis.paramonov.ui.components.Stopwatch
import kotlin.math.abs

@Composable
fun QuestionsScreen() {
    val di = localDI()
    val viewModel: QuestionsViewModel by di.instance()

    val state = viewModel.container.stateFlow.collectAsState()
    val effect = viewModel.container.sideEffectFlow

    var error by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getQuestions()
        viewModel.getMaxScore()

        effect.collect {
            when(it) {
                is QuestionsScreenSideEffect.ShowError -> {
                    val errorMessage = it.message
                    val errorTitle = it.title

                    error = errorTitle to errorMessage
                }
            }
        }
    }

    state.value.result?.let {
        ResultScreen(
            modifier = Modifier.fillMaxSize(),
            resultData = it,
            maxScore = state.value.maxScore
        )
    } ?: ScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state.value,
        onAnswerSelected = { question, answerPos ->
            viewModel.updateChosenAnswers(question, answerPos)
        },
        onEndClick = { viewModel.onQuestionsEnd() }
    )

    Box {
        error?.let {
            ErrorDialog(
                onDismiss = { error = null },
                title = it.first,
                text = it.second
            )
        }
    }
}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    state: QuestionsScreenState,
    onAnswerSelected: (question: Int, answerPos: Int) -> Unit,
    onEndClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.questions.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.questions.isNotEmpty()) {
            Text(
                text = stringResource(
                    id = R.string.question_num,
                    pagerState.currentPage + 1,
                    state.questions.size
                ),
                fontSize = 24.sp,
            )
            Time(
                modifier = Modifier.fillMaxWidth(),
                time = state.time
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val configuration = LocalConfiguration.current
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val minScale = 0.8f

            val alpha = when {
                pageOffset < -1f -> 0f
                pageOffset <= 0f -> 1 - pageOffset * pageOffset
                pageOffset <= 1f -> 1 - pageOffset
                else -> 0f
            }

            val rotation = if (pageOffset <= 0) pageOffset * -45f else 0f
            val translationX = if (pageOffset <= 1) pageOffset * configuration.screenWidthDp * -1 else 0f
            val scale = if (pageOffset <= 1) (minScale + (1 - minScale) * (1 - abs(pageOffset))) else 1f

            var correct by remember { mutableStateOf(
                state.questions[page].answers.find { it.chosen }?.correct
            ) }

            QuestionPage(
                modifier = Modifier
                    .graphicsLayer {
                        this.alpha = alpha
                        this.rotationZ = rotation
                        this.translationX = translationX
                        this.scaleX = scale
                        this.scaleY = scale
                    },
                question = state.questions[page],
                onAnswerSelected = { answerIndex ->
                    correct = state.questions[page].answers[answerIndex].correct
                    onAnswerSelected(page, answerIndex)
                },
                additionalBottomText = if (page == state.questions.size - 1) stringResource(R.string.end) else "",
                onAdditionalBottomTextClick = {
                    if (page == state.questions.size - 1) onEndClick()
                }
            )
        }
    }
}

@Composable
fun Time(
    time: Int,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    if (!isVisible) return
    val min by remember(time) { mutableIntStateOf(time / 60) }
    val sec by remember(time) { mutableIntStateOf(time % 60) }

    Row(
        modifier = modifier
            .padding(horizontal = 64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Stopwatch(
            time = time,
            clockSize = 60.dp
        )

        Text(
            text = if (min > 0) stringResource(R.string.clock_time_with_min, min, sec)
                else stringResource(R.string.clock_time, sec),
            fontSize = 24.sp,
        )
    }
}
