package ru.kpfu.itis.paramonov.firebase.internal.data.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.delay

internal suspend fun <T> Task<T>.waitResult(): Task<T> {
    while (true) {
        if (isComplete) break
        else delay(100L)
    }
    return this
}