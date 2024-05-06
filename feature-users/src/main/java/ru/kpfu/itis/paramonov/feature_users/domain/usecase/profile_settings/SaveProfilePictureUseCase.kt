package ru.kpfu.itis.paramonov.feature_users.domain.usecase.profile_settings

import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.firebase.external.domain.utils.UpdateKeys
import ru.kpfu.itis.paramonov.firebase.external.domain.repository.UserRepository
import javax.inject.Inject

class SaveProfilePictureUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
) {

    suspend operator fun invoke(uri: Uri) {
        withContext(dispatcher) {
            repository.updateUser(
                UpdateKeys.UPDATE_PROFILE_PICTURE_KEY to uri
            )
        }
    }
}