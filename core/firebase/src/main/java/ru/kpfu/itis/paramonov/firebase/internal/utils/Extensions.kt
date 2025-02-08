package ru.kpfu.itis.paramonov.firebase.internal.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.delay

private const val TASK_WAITING_TIME = 100L

internal suspend fun <T> Task<T>.waitResult(): Task<T> {
    while (true) {
        if (isComplete) break
        else delay(TASK_WAITING_TIME)
    }
    return this
}
