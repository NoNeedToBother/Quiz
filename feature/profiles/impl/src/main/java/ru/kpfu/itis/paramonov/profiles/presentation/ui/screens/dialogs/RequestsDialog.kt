package ru.kpfu.itis.paramonov.profiles.presentation.ui.screens.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import ru.kpfu.itis.paramonov.core.model.presentation.UserModel
import ru.kpfu.itis.paramonov.profiles.R
import ru.kpfu.itis.paramonov.profiles.presentation.ui.components.DialogWithTitle
import ru.kpfu.itis.paramonov.ui.views.EmptyResults

@Composable
fun RequestsDialog(
    initialRequests: List<UserModel>,
    onDismiss: () -> Unit,
    onDenyClick: (String) -> Unit,
    onAcceptClick: (String) -> Unit
) {
    var requests by remember { mutableStateOf(initialRequests) }
    DialogWithTitle(
        title = stringResource(R.string.requests),
        onDismiss = onDismiss,
    ) {
        if (requests.isNotEmpty())
            RequestList(
                requests = requests,
                onAcceptClick = { pos, userId ->
                    requests = requests.toMutableList().apply { removeAt(pos) }
                    onAcceptClick(userId)
                },
                onDenyClick = { pos, userId ->
                    requests = requests.toMutableList().apply { removeAt(pos) }
                    onDenyClick(userId)
                }
            )
        else EmptyResults()
    }
}

@Composable
fun RequestList(
    modifier: Modifier = Modifier,
    requests: List<UserModel>,
    onAcceptClick: (Int, String) -> Unit,
    onDenyClick: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(requests) { idx, req ->
            RequestItem(
                user = req,
                onAcceptClick = { onAcceptClick(idx, req.id) },
                onDenyClick = { onDenyClick(idx, req.id) }
            )
        }

    }
}

@Composable
fun RequestItem(
    modifier: Modifier = Modifier,
    user: UserModel,
    onAcceptClick: () -> Unit,
    onDenyClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.profilePictureUrl),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.username,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user.id,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.accept),
                contentDescription = "Accept",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onAcceptClick() }
            )
            Image(
                painter = painterResource(id = R.drawable.deny),
                contentDescription = "Deny",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onDenyClick() }
            )
        }
    }
}
