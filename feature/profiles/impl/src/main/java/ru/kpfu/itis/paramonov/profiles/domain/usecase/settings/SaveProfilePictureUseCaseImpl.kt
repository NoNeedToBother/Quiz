package ru.kpfu.itis.paramonov.profiles.domain.usecase.settings

import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.model.UpdateKeys
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.settings.SaveProfilePictureUseCase

class SaveProfilePictureUseCaseImpl(
    private val dispatcher: CoroutineDispatcher,
    private val repository: UserRepository
): SaveProfilePictureUseCase {

    override suspend operator fun invoke(uri: Uri) {
        withContext(dispatcher) {
            repository.updateUser(
                UpdateKeys.UPDATE_PROFILE_PICTURE_KEY to uri
            )
        }
    }
}
