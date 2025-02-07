package ru.kpfu.itis.paramonov.quiz.presentation.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.closestDI
import org.kodein.di.instance
import ru.kpfu.itis.paramonov.quiz.R
import ru.kpfu.itis.paramonov.quiz.navigation.Navigator
import ru.kpfu.itis.paramonov.ui.base.MviBaseFragment
import ru.kpfu.itis.paramonov.ui.theme.AppTheme
import ru.kpfu.itis.paramonov.ui.theme.StardosFont
import ru.kpfu.itis.paramonov.ui.theme.Typography
class MainMenuFragment: MviBaseFragment(), DIAware {

    override val di: DI by closestDI()

    private val navigator: Navigator by instance()

    override fun initView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    Screen(
                        onBeginClick = { navigator.goToQuestions() },
                        onTrainingModeClick = { navigator.goToTraining() },
                        onQuestionsSettingsClick = { navigator.goToQuestionSettings() }
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(
    onBeginClick: () -> Unit,
    onTrainingModeClick: () -> Unit,
    onQuestionsSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = Typography.bodyLarge,
            fontSize = 92.sp,
            fontFamily = StardosFont,
        )

        Spacer(modifier = Modifier.height(32.dp))
        MainMenuTextButton(
            text = stringResource(id = R.string.begin),
            onClick = onBeginClick
        )

        Spacer(modifier = Modifier.height(20.dp))
        MainMenuTextButton(
            text = stringResource(id = R.string.training),
            onClick = onTrainingModeClick
        )

        Spacer(modifier = Modifier.height(20.dp))
        MainMenuTextButton(
            text = stringResource(id = R.string.question_settings),
            onClick = onQuestionsSettingsClick
        )
    }
}

@Composable
fun MainMenuTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    Text(
        modifier = modifier.clickable {
            onClick()
        },
        text = text,
        fontFamily = StardosFont,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline,
        style = TextStyle.Default.copy(
            fontSize = 52.sp,
            lineHeight = 60.sp
        )
    )
}
