package ru.kpfu.itis.paramonov.profiles.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.ui.theme.Typography

@Composable
fun ProfileInfoField(
    modifier: Modifier = Modifier,
    label: String,
    value: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        enabled = false,
        modifier = modifier
    )
}

@Composable
fun ProfileInfoInputField(
    modifier: Modifier = Modifier,
    onInput: (String) -> Unit,
    label: String,
    value: String,
    error: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onInput,
        label = { Text(label) },
        modifier = modifier,
        isError = error != null,
        supportingText = { error?.let { Text(text = error) } }
    )
}

@Composable
fun ProfilePasswordInputField(
    modifier: Modifier = Modifier,
    onInput: (String) -> Unit,
    label: String,
    value: String,
    error: String? = null
) {
    var showPassword by remember { mutableStateOf(false) }
    if (showPassword) {
        OutlinedTextField(
            value = value,
            onValueChange = onInput,
            label = { Text(label) },
            modifier = modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    Icon(
                        painter = painterResource(R.drawable.not_visible),
                        contentDescription = stringResource(R.string.toggle)
                    )
                }
            },
            isError = error != null,
            supportingText = { error?.let { Text(text = error) } }
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onInput,
            label = { Text(label) },
            modifier = modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    Icon(
                        painter = painterResource(R.drawable.visible),
                        contentDescription = stringResource(R.string.toggle)
                    )
                }
            },
            isError = error != null,
            supportingText = { error?.let { Text(text = error) } }
        )
    }
}

@Composable
fun DialogWithTitle(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
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
                Text(text = title, style = Typography.bodyLarge, fontSize = 24.sp)
                content()
            }
        }
    }
}
