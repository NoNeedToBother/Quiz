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

const val USERNAME_UPDATE_KEY = "username"
const val INFO_UPDATE_KEY = "info"

@Composable
fun ProfileSettingsDialog(
    onDismiss: () -> Unit,
    onSave: (username: String?, info: String?) -> Unit,
    checkUsername: (username: String?) -> Unit,
    usernameError: String? = null
) {
    var username by remember { mutableStateOf("") }
    var info by remember { mutableStateOf("") }

    DialogWithTitle(
        title = stringResource(R.string.settings),
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputSection(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onInput = {
                    username = it
                    checkUsername(it)
                },
                label = stringResource(R.string.username),
                value = username,
                error = usernameError
            )
            InputSection(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                onInput = { info = it },
                label = stringResource(R.string.info),
                value = info
            )
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = {
                        val resUsername = username.ifEmpty { null }
                        val resInfo = info.ifEmpty { null }
                        onSave(resUsername, resInfo)
                    }
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
