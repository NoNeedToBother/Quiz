package ru.kpfu.itis.paramonov.firebase.data.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.delay

suspend fun <T> Task<T>.waitResult(): Task<T> {
    while (true) {
        if (isComplete) break
        else delay(100L)
    }
    return this
}