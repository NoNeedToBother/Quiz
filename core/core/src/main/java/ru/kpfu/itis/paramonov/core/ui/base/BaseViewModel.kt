package ru.kpfu.itis.paramonov.core.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel: ViewModel() {
    interface Result {
        interface Success<T> {
            fun getValue(): T
        }
        interface Failure {
            fun getException(): Throwable
        }
    }
}