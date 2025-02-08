package ru.kpfu.itis.paramonov.core.validators

interface Validator {

    fun validate(param: String): Boolean

    fun getRequirements(): String

    fun getMessage(): String
}
