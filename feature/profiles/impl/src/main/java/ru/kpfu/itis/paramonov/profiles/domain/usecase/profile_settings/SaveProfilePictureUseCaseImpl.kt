package ru.kpfu.itis.paramonov.profiles.domain.usecase.profile_settings

import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.kpfu.itis.paramonov.profiles.api.model.UpdateKeys
import ru.kpfu.itis.paramonov.profiles.api.repository.UserRepository
import ru.kpfu.itis.paramonov.profiles.api.usecase.profile_settings.SaveProfilePictureUseCase
import javax.inject.Inject

class SaveProfilePictureUseCaseImpl @Inject constructor(
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