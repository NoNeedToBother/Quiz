package ru.kpfu.itis.paramonov.core.resources

interface ResourceManager {
    fun getString(stringId: Int): String

    fun getString(stringId: Int, vararg args: Any?): String

}
