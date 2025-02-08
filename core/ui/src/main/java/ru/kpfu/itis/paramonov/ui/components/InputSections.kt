package ru.kpfu.itis.paramonov.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import ru.kpfu.itis.paramonov.ui.R

@Composable
fun InputSection(
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
fun PasswordInputSection(
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
