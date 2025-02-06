package ru.kpfu.itis.paramonov.questions.presentation.questions.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.ResultDataUiModel
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.ResultScoreUiModel

private const val F_MAX_RATIO = 0.3
private const val E_MAX_RATIO = 0.4
private const val D_MAX_RATIO = 0.5
private const val C_MAX_RATIO = 0.625
private const val B_MAX_RATIO = 0.75

private const val MINUS_GRADATION_MAX_RATIO = 0.25
private const val PLUS_GRADATION_MIN_RATION = 0.75

@Composable
fun ResultScreen(
    resultData: ResultDataUiModel,
    maxScore: Double,
) {
    val resultScoreUiModel by remember(resultData.score, maxScore) {
        mutableStateOf(getMaxResultScoreUiModel(resultData.score, maxScore))
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.question_page),
                contentScale = ContentScale.FillBounds
            )
    ) {
        val topMargin = maxHeight * 0.05f
        val startMargin = maxWidth * 0.1f

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = startMargin,
                    vertical = topMargin,
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResultText(text = stringResource(R.string.results))
            ResultText(text = stringResource(R.string.res_difficulty, resultData.difficulty))
            ResultText(text = stringResource(R.string.res_category, resultData.category))
            ResultText(text = stringResource(R.string.res_game_mode, resultData.gameMode))
            ResultText(text = stringResource(R.string.res_time, resultData.time))
            ResultText(text = stringResource(R.string.res_ratio, resultData.correct, resultData.total))
            ResultText(text = stringResource(R.string.res_score, resultData.score))
        }

        ScoreImage(
            resultScoreUiModel = resultScoreUiModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
private fun ResultText(text: String) {
    Text(
        text = text,
        fontFamily = FontFamily(Font(R.font.bad_script)),
        fontSize = 24.sp,
        color = Color.Black,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ScoreImage(resultScoreUiModel: ResultScoreUiModel, modifier: Modifier = Modifier) {
    val scoreDrawable = when (resultScoreUiModel) {
        ResultScoreUiModel.A -> R.drawable.a
        ResultScoreUiModel.B -> R.drawable.b
        ResultScoreUiModel.C -> R.drawable.c
        ResultScoreUiModel.D -> R.drawable.d
        ResultScoreUiModel.E -> R.drawable.e
        ResultScoreUiModel.F -> R.drawable.f
    }

    val gradationDrawable = when (resultScoreUiModel.gradation) {
        ResultScoreUiModel.GradationUiModel.PLUS -> R.drawable.plus
        ResultScoreUiModel.GradationUiModel.MINUS -> R.drawable.minus
        ResultScoreUiModel.GradationUiModel.DEFAULT -> null
    }

    Box(
        modifier = modifier
            .size(64.dp)
    ) {
        Image(
            painter = painterResource(id = scoreDrawable),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )

        gradationDrawable?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

private fun getMaxResultScoreUiModel(score: Double, maxScore: Double): ResultScoreUiModel {
    val ratio = score / maxScore
    val model = when {
        ratio <= F_MAX_RATIO -> ResultScoreUiModel.F.apply {
            gradation = getGradation(0.0, F_MAX_RATIO, ratio)
        }
        ratio <= E_MAX_RATIO -> ResultScoreUiModel.E.apply {
            gradation = getGradation(F_MAX_RATIO, E_MAX_RATIO, ratio)
        }
        ratio <= D_MAX_RATIO -> ResultScoreUiModel.D.apply {
            gradation = getGradation(E_MAX_RATIO, D_MAX_RATIO, ratio)
        }
        ratio <= C_MAX_RATIO -> ResultScoreUiModel.C.apply {
            gradation = getGradation(D_MAX_RATIO, C_MAX_RATIO, ratio)
        }
        ratio <= B_MAX_RATIO -> ResultScoreUiModel.B.apply {
            gradation = getGradation(C_MAX_RATIO, B_MAX_RATIO, ratio)
        }
        else -> ResultScoreUiModel.A.apply {
            gradation = getGradation(B_MAX_RATIO, 1.0, ratio)
        }
    }
    return model
}

private fun getGradation(minValue: Double, maxValue: Double, value: Double): ResultScoreUiModel.GradationUiModel {
    val absoluteDelta = maxValue - minValue
    val delta = value - minValue
    return (delta / absoluteDelta).let {
        when {
            it <= MINUS_GRADATION_MAX_RATIO -> ResultScoreUiModel.GradationUiModel.MINUS
            it >= PLUS_GRADATION_MIN_RATION -> ResultScoreUiModel.GradationUiModel.PLUS
            else -> ResultScoreUiModel.GradationUiModel.DEFAULT
        }
    }
}
