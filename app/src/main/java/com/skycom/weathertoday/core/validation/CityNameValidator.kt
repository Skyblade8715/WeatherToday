package com.skycom.weathertoday.core.validation


object CityNameValidator {

    private val cityRegex = Regex("^[\\p{L}][\\p{L} .'-]{1,49}$")

    fun validate(input: String): ValidationResult {
        val trimmed = input.trim()

        return when {
            trimmed.isBlank() -> ValidationResult.Valid
            trimmed.length < 2 -> ValidationResult.Invalid(ValidationReason.TOO_SHORT)
            !cityRegex.matches(trimmed) -> ValidationResult.Invalid(ValidationReason.INVALID_CHARACTERS)
            else -> ValidationResult.Valid
        }
    }
}