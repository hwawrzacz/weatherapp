package com.wawrzacz.weather.utils

object Validator {
    private val noNumberRegex = Regex("^((?![0-9]).)+$")
    private val noSpecialCharacter = Regex("^((?![!@#\$%^&*()\\[\\]{}\\\\|;':\",./<>?]).)+\$")

    fun isValidNotEmpty(textToValidate: String): Boolean {
        return !textToValidate.isNullOrBlank()
    }

    fun isValidNoNumber(textToValidate: String): Boolean {
        return noNumberRegex.matches(textToValidate)
    }

    fun isValidNoSpecialCharacters(textToValidate: String): Boolean {
        return noSpecialCharacter.matches(textToValidate)
    }

    fun isValid(textToValidate: String): Boolean {
        return isValidNotEmpty(textToValidate) &&
                isValidNoNumber(textToValidate) &&
                isValidNoSpecialCharacters(textToValidate)
    }
}