package ru.kpfu.itis.paramonov.authentication.presentation.components

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.kpfu.itis.paramonov.authentication.R
import ru.kpfu.itis.paramonov.ui.theme.logoFont

@Composable
fun InputSection(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(R.drawable.edit),
                    contentDescription = "Edit"
                )
            }
        }
    )
}

@Composable
fun PasswordSection(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit
) {
    var showPassword by remember { mutableStateOf(false) }
    if (showPassword) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    Icon(
                        painter = painterResource(R.drawable.not_visible),
                        contentDescription = "Edit"
                    )
                }
            }
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    showPassword = !showPassword
                }) {
                    Icon(
                        painter = painterResource(R.drawable.visible),
                        contentDescription = "Edit"
                    )
                }
            }
        )
    }
}

@Composable
fun Logo(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(ru.kpfu.itis.paramonov.ui.R.string.app_name),
        textAlign = TextAlign.Center,
        modifier = modifier,
        fontSize = 92.sp,
        style = TextStyle(fontFamily = logoFont)
    )
}