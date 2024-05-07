package ru.kpfu.itis.paramonov.common.validators

import ru.kpfu.itis.paramonov.common.R
import ru.kpfu.itis.paramonov.common.resources.ResourceManager

class PasswordValidator(
    private val resourceManager: ResourceManager
): Validator {

    override fun validate(param: String): Boolean {
        var hasDigit = false
        var hasUpperCase = false
        var hasLowerCase = false
        for (letter in param) {
            if (letter.isDigit()) hasDigit = true
            if (letter.isLowerCase()) hasLowerCase = true
            if (letter.isUpperCase()) hasUpperCase = true
        }
        return hasDigit && hasUpperCase && hasLowerCase && (param.length >= MIN_PASSWORD_LENGTH)
    }

    override fun getRequirements(): String {
        return resourceManager.getString(R.string.weak_password)
    }

    override fun getMessage(): String {
        return resourceManager.getString(R.string.weak_password_msg)
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 6
    }
}