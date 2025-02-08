package ru.kpfu.itis.paramonov.profiles.api.usecase.settings

import android.net.Uri

interface SaveProfilePictureUseCase {

    suspend operator fun invoke(uri: Uri)

}
