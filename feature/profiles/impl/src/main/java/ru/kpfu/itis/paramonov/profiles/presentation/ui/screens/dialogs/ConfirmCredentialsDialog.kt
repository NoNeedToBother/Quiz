package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.DialogWithTitle
import ru.kpfu.itis.paramonov.ui.components.InputSection
import ru.kpfu.itis.paramonov.ui.components.PasswordInputSection

@Composable
fun ConfirmCredentialsDialog(
    onDismiss: () -> Unit,
    onSave: (email: String?, password: String?) -> Unit,
    checkEmail: (email: String?) -> Unit,
    checkPassword: (password: String?) -> Unit,
    emailError: String? = null,
    passwordError: String? = null
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    DialogWithTitle(
        title = stringResource(R.string.confirm_credentials),
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputSection(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onInput = {
                    email = it
                    checkEmail(it)
                },
                label = stringResource(R.string.email),
                value = email,
                error = emailError
            )
            PasswordInputSection(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onInput = {
                    password = it
                    checkPassword(it)
                },
                label = stringResource(R.string.password),
                value = password,
                error = passwordError
            )
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = {
                        val resEmail = email.ifEmpty { null }
                        val resPassword = password.ifEmpty { null }
                        onSave(resEmail, resPassword)
                        onDismiss()
                    },
                    enabled = emailError == null && passwordError == null && email.isNotEmpty() && password.isNotEmpty()
                ) {
                    Text(text = stringResource(R.string.confirm_dialog_pos))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(R.string.confirm_dialog_neg))
                }
            }
        }
    }
}
