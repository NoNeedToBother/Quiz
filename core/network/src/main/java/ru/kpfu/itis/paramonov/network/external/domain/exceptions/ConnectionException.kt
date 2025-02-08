package ru.kpfu.itis.paramonov.network.external.domain.exceptions

class ConnectionException @JvmOverloads constructor(
    message: String = ""
): Throwable(message = message)
