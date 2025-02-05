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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.DialogWithTitle
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.ProfileInfoInputField
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.ProfilePasswordInputField

@Composable
fun CredentialsDialog(
    onDismiss: () -> Unit,
    onSave: (email: String?, password: String?, confirmPassword: String?) -> Unit,
    checkEmail: (email: String?) -> String?,
    checkPassword: (password: String?) -> String?,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    DialogWithTitle(
        title = stringResource(R.string.confirm_credentials),
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings_dialog_warning),
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            ProfileInfoInputField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onInput = {
                    email = it
                    emailError = checkEmail(it)
                },
                label = stringResource(R.string.email),
                value = email,
                error = emailError
            )
            ProfilePasswordInputField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onInput = {
                    password = it
                    passwordError = checkPassword(it)
                },
                label = stringResource(R.string.password),
                value = password,
                error = passwordError
            )
            ProfilePasswordInputField(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onInput = {
                    confirmPassword = it
                    confirmPasswordError = checkPassword(it)
                },
                label = stringResource(R.string.confirm_password),
                value = confirmPassword,
                error = confirmPasswordError
            )

            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = {
                        val resEmail = email.ifEmpty { null }
                        val resPassword = password.ifEmpty { null }
                        val resConfirmPassword = confirmPassword.ifEmpty { null }
                        onSave(resEmail, resPassword, resConfirmPassword)
                    },
                    enabled = emailError == null && passwordError == null && email.isNotEmpty() && password.isNotEmpty()
                ) {
                    Text(text = stringResource(R.string.dialog_pos))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(R.string.dialog_neg))
                }
            }
        }
    }
}
