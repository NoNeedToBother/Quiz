package ru.kpfu.itis.paramonov.questions.presentation.questions.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.TrainingQuestionsScreenSideEffect
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.TrainingQuestionsScreenState
import ru.kpfu.itis.paramonov.questions.presentation.questions.ui.components.QuestionPage
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.TrainingQuestionsViewModel
import ru.kpfu.itis.paramonov.ui.components.ErrorDialog
import kotlin.math.abs

@Composable
fun TrainingQuestionsScreen() {
    val di = localDI()
    val viewModel: TrainingQuestionsViewModel by di.instance()

    val state = viewModel.container.stateFlow.collectAsState()
    val effect = viewModel.container.sideEffectFlow

    var error by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getQuestions()

        effect.collect {
            when(it) {
                is TrainingQuestionsScreenSideEffect.ShowError -> {
                    val errorTitle = it.title
                    val errorMessage = it.message

                    error = errorTitle to errorMessage
                }
            }
        }
    }

    ScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state.value,
        onAnswerSelected = { question, answerPos ->
            viewModel.updateChosenAnswers(question, answerPos)
        },
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
    state: TrainingQuestionsScreenState,
    onAnswerSelected: (question: Int, answerPos: Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.questions.size })

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(
                id = R.string.question_num,
                pagerState.currentPage + 1,
                state.questions.size
            ),
            fontSize = 24.sp,
        )

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
                additionalBottomText = correct?.let {
                    if (it) stringResource(R.string.correct_guess)
                    else stringResource(R.string.incorrect_guess)
                } ?: ""
            )
        }
    }
}
