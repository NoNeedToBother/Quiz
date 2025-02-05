package ru.kpfu.itis.paramonov.questions.presentation.questions.fragments

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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsComponent
import ru.kpfu.itis.paramonov.questions.di.FeatureQuestionsDependencies
import ru.kpfu.itis.paramonov.questions.presentation.questions.mvi.TrainingQuestionsScreenState
import ru.kpfu.itis.paramonov.questions.presentation.questions.ui.components.QuestionPage
import ru.kpfu.itis.paramonov.questions.presentation.questions.viewmodel.TrainingQuestionsViewModel
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.di.FeatureUtils
import ru.kpfu.itis.paramonov.ui.theme.AppTheme
import javax.inject.Inject
import kotlin.math.abs

class TrainingQuestionsScreen: MviBaseFragment() {

    @Inject
    lateinit var viewModel: TrainingQuestionsViewModel

    override fun inject() {
        FeatureUtils.getFeature<FeatureQuestionsComponent>(this, FeatureQuestionsDependencies::class.java)
            .trainingQuestionsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    val state = viewModel.container.stateFlow.collectAsState()
                    val effect = viewModel.container.sideEffectFlow

                    LaunchedEffect(Unit) {
                        viewModel.getQuestions()
                    }

                    Screen(
                        state = state.value,
                        onAnswerSelected = { question, answerPos ->
                            viewModel.updateChosenAnswers(question, answerPos)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(
    state: TrainingQuestionsScreenState,
    onAnswerSelected: (question: Int, answerPos: Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.questions.size })

    Column(
        modifier = Modifier
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
