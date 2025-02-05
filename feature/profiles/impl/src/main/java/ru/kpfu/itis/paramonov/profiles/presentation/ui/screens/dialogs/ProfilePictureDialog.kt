package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.placeholder
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.DialogWithTitle

@Composable
fun ProfilePictureDialog(
    uri: Uri,
    onPositivePressed: () -> Unit,
    onDismiss: () -> Unit
) {
    DialogWithTitle(
        title = stringResource(R.string.preview),
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri)
                    .placeholder(ru.kpfu.itis.paramonov.ui.R.drawable.default_pfp)
                    .build(),
                contentDescription = stringResource(R.string.preview),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp)
                    .size(200.dp)
                    .clip(CircleShape)
            )
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = onPositivePressed
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
