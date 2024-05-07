package ru.kpfu.itis.paramonov.common.validators

import ru.kpfu.itis.paramonov.common.R
import ru.kpfu.itis.paramonov.common.resources.ResourceManager

class UsernameValidator(
    private val resourceManager: ResourceManager
): Validator {

    override fun validate(param: String): Boolean {
        return param.length > 6
    }

    override fun getRequirements(): String {
        return resourceManager.getString(R.string.invalid_username)
    }

    override fun getMessage(): String {
        return resourceManager.getString(R.string.invalid_username_msg)
    }
}