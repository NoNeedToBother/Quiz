package ru.kpfu.itis.paramonov.questions.presentation.questions.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kpfu.itis.paramonov.questions.R
import ru.kpfu.itis.paramonov.questions.presentation.questions.model.QuestionDataUiModel

@Composable
fun QuestionPage(
    modifier: Modifier = Modifier,
    question: QuestionDataUiModel,
    onAnswerSelected: (Int) -> Unit,
    additionalBottomText: String = "",
    onAdditionalBottomTextClick: (() -> Unit)? = null
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .paint(
                painterResource(id = R.drawable.question_page),
                contentScale = ContentScale.FillBounds,
            )
    ) {
        val topMargin = maxHeight * 0.05f
        val startMargin = maxWidth * 0.1f

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = startMargin,
                    vertical = topMargin,
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = question.text,
                fontFamily = FontFamily(Font(R.font.bad_script)),
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(question.answers) { index, answer ->
                    AnswerItem(
                        checked = answer.chosen,
                        answerText = answer.answer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onAnswerSelected(index) }
                            .padding(vertical = 4.dp)
                    )
                }
            }

            Text(
                text = additionalBottomText,
                fontFamily = FontFamily(Font(R.font.bad_script)),
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp).clickable {
                    onAdditionalBottomTextClick?.let { it() }
                }
            )
        }
    }
}

@Composable
fun AnswerItem(
    modifier: Modifier = Modifier,
    checked: Boolean,
    answerText: String,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (checked) painterResource(R.drawable.mark_checked) else painterResource(R.drawable.mark),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
        )

        Text(
            text = answerText,
            fontFamily = FontFamily(Font(R.font.bad_script)),
            color = Color.Black,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
