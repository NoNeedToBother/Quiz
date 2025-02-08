package ru.kpfu.itis.paramonov.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.kpfu.itis.paramonov.ui.theme.Typography

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
    title: String,
    text: String
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, style = Typography.titleMedium,
                    fontSize = 32.sp, textAlign = TextAlign.Center)
                Text(text = text, style = Typography.bodyMedium,
                    fontSize = 24.sp, textAlign = TextAlign.Center)
            }
        }
    }
}
