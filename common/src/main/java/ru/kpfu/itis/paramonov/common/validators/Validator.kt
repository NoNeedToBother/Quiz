package ru.kpfu.itis.paramonov.common.validators

interface Validator {

    fun validate(param: String): Boolean

    fun getRequirements(): String

    fun getMessage(): String
}